package ru.forumcalendar.forumcalendar.service.base;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.forumcalendar.forumcalendar.domain.*;
import ru.forumcalendar.forumcalendar.exception.EntityNotFoundException;
import ru.forumcalendar.forumcalendar.model.ActivityModel;
import ru.forumcalendar.forumcalendar.model.form.ActivityForm;
import ru.forumcalendar.forumcalendar.model.form.ShiftForm;
import ru.forumcalendar.forumcalendar.repository.ActivityModeratorRepository;
import ru.forumcalendar.forumcalendar.repository.ActivityRepository;
import ru.forumcalendar.forumcalendar.repository.ShiftRepository;
import ru.forumcalendar.forumcalendar.service.ActivityService;
import ru.forumcalendar.forumcalendar.service.UserService;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class BaseActivityService implements ActivityService {

    private EntityManager entityManager;

    private final ActivityRepository activityRepository;
    private final ShiftRepository shiftRepository;
    private final ActivityModeratorRepository activityModeratorRepository;

    private final ConversionService conversionService;
    private final UserService userService;

    @Autowired
    public BaseActivityService(
            EntityManager entityManager, ActivityRepository activityRepository,
            ShiftRepository shiftRepository,
            ActivityModeratorRepository activityModeratorRepository,
            @Qualifier("mvcConversionService") ConversionService conversionService,
            UserService userService
    ) {
        this.entityManager = entityManager;
        this.activityRepository = activityRepository;
        this.shiftRepository = shiftRepository;
        this.activityModeratorRepository = activityModeratorRepository;
        this.conversionService = conversionService;
        this.userService = userService;
    }

    @Override
    public boolean exist(int id) {
        return activityRepository.findById(id).isPresent();
    }

    @Override
    public Activity get(int id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Activity with id " + id + " not found"));
    }

    @Override
    public List<ActivityModel> getAll() {
        return activityRepository.findAll()
                .stream()
                .map((a) -> conversionService.convert(a, ActivityModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public Activity save(ActivityForm activityForm) {

        Activity activity = activityRepository.findById(activityForm.getId()).orElse(new Activity());
        activity.setName(activityForm.getName());
        activity.setUser(userService.getCurrentUser());
        activity.setDescription(activityForm.getDescription());
        activity.setPlace(activityForm.getPlace());
        activity = activityRepository.save(activity);

        if (activityForm.getShiftForms() != null) {
            List<Shift> shifts = new ArrayList<>(activityForm.getShiftForms().size());

            for (ShiftForm sf : activityForm.getShiftForms()) {
                Shift shift = shiftRepository.findById(sf.getId()).orElse(new Shift());
                shift.setName(sf.getName());
                shift.setStartDate(sf.getStartDate());
                shift.setEndDate(sf.getEndDate());
                shift.setActivity(activity);
                shift.setDescription(sf.getDescription());
                shifts.add(shift);
            }

            shiftRepository.saveAll(shifts);
        }
        else {
            shiftRepository.deleteByActivity_Id(activity.getId());
        }

        return activity;
    }

    @Override
    public Activity delete(int id) {
        Activity activity = get(id);
        activityRepository.deleteById(id);
        return activity;
    }


    @Override
    public List<ActivityModel> searchByName(String q) throws InterruptedException {

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        fullTextEntityManager.createIndexer().startAndWait();

        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Activity.class)
                .get();

        Query query = queryBuilder
                .keyword()
                .wildcard()
                .onField("name")
                .matching(q.toLowerCase())
                .createQuery();

        org.hibernate.search.jpa.FullTextQuery jpaQuery
                = fullTextEntityManager.createFullTextQuery(query, Activity.class);

        return convertActivities(jpaQuery.getResultList().stream());
    }

    @Override
    public List<ActivityModel> getCurrentUserActivityModels() {

        return activityRepository.getAllByUserId(userService.getCurrentId())
                .map((a) -> conversionService.convert(a, ActivityModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasPermissionToWrite(int id) {
        User user = userService.getCurrentUser();
        Activity activity = get(id);
        ActivityModerator activityModerator = activityModeratorRepository.getByUserIdAndActivityId(user.getId(), id);
        return activity.getUser().getId().equals(userService.getCurrentId()) || activityModerator != null ||
                user.getRole().getId() == Role.ROLE_SUPERUSER_ID;
    }

    @Override
    public boolean hasPermissionToRead(int id) {
        return true;
    }

    private List<ActivityModel> convertActivities(Stream<Activity> speakers) {
        return speakers
                .map((t) -> conversionService.convert(t, ActivityModel.class))
                .collect(Collectors.toList());
    }
}
