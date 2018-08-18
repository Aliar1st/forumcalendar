package ru.forumcalendar.forumcalendar.service.base;

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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BaseActivityService implements ActivityService {

    private final ActivityRepository activityRepository;
    private final ShiftRepository shiftRepository;
    private final ActivityModeratorRepository activityModeratorRepository;

    private final ConversionService conversionService;
    private final UserService userService;

    @Autowired
    public BaseActivityService(
            ActivityRepository activityRepository,
            ShiftRepository shiftRepository,
            ActivityModeratorRepository activityModeratorRepository,
            @Qualifier("mvcConversionService") ConversionService conversionService,
            UserService userService
    ) {
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
        activity = activityRepository.save(activity);

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

        return activity;
    }

    @Override
    public Activity delete(int id) {
        Activity activity = get(id);
        activityRepository.deleteById(id);
        return activity;
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
        return activity.getUser().getId().equals(userService.getCurrentId()) ||
                activityModerator != null ||
                user.getRole().getId() == Role.ROLE_SUPERUSER_ID;
    }

    @Override
    public boolean hasPermissionToRead(int id) {
        return true;
    }
}
