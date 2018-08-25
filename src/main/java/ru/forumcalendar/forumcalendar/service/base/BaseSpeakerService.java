package ru.forumcalendar.forumcalendar.service.base;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.forumcalendar.forumcalendar.domain.Speaker;
import ru.forumcalendar.forumcalendar.exception.EntityNotFoundException;
import ru.forumcalendar.forumcalendar.model.SpeakerModel;
import ru.forumcalendar.forumcalendar.model.form.SpeakerForm;
import ru.forumcalendar.forumcalendar.repository.SpeakerRepository;
import ru.forumcalendar.forumcalendar.service.ActivityService;
import ru.forumcalendar.forumcalendar.service.SpeakerService;
import ru.forumcalendar.forumcalendar.service.UploadsService;
import ru.forumcalendar.forumcalendar.service.UserService;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class BaseSpeakerService implements SpeakerService {

    @Value("${my.defaultPhoto}")
    private String defaultPhoto;

    private EntityManager entityManager;

    private final SpeakerRepository speakerRepository;

    private final UserService userService;
    private final UploadsService uploadsService;
    private final ActivityService activityService;
    private final ConversionService conversionService;

    @Autowired
    public BaseSpeakerService(
            EntityManager entityManager,
            SpeakerRepository speakerRepository,
            UploadsService uploadsService,
            ActivityService activityService,
            UserService userService,
            @Qualifier("mvcConversionService") ConversionService conversionService
    ) {
        this.entityManager = entityManager;
        this.speakerRepository = speakerRepository;
        this.uploadsService = uploadsService;
        this.activityService = activityService;
        this.userService = userService;
        this.conversionService = conversionService;
    }

    @Override
    public boolean exist(int id) {
        return speakerRepository.findById(id).isPresent();
    }

    @Override
    public Speaker get(int id) {
        return speakerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Speaker with id " + id + " not found"));
    }

    @Override
    public List<SpeakerModel> getAll() {
        return speakerRepository.findAll()
                .stream()
                .map((s) -> conversionService.convert(s, SpeakerModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public Speaker save(SpeakerForm speakerForm) {

        Speaker speaker = speakerRepository.findById(speakerForm.getId()).orElse(new Speaker());
        speaker.setFirstName(speakerForm.getFirstName());
        speaker.setLastName(speakerForm.getLastName());
        speaker.setLink(speakerForm.getLink());
        speaker.setDescription(speakerForm.getDescription());
        speaker.setActivity(activityService.get(speakerForm.getActivityId()));

        //noinspection Duplicates
        if (!speakerForm.getPhoto().isEmpty() ) {
            String photo = uploadsService.upload(speakerForm.getPhoto())
                    .map((f) -> {
                        if (!speaker.getPhoto().equals(f.getName()) &&
                                !speaker.getPhoto().equals(defaultPhoto))
                            uploadsService.delete(speaker.getPhoto());
                        return f.getName();
                    })
                    .orElse(speaker.getPhoto());
            speaker.setPhoto(photo);
        }

        return speakerRepository.save(speaker);
    }

    @Override
    public Speaker delete(int id) {
        Speaker speaker = get(id);
        speakerRepository.deleteById(id);
        return speaker;
    }

    @Override
    public List<SpeakerModel> getSpeakerModelsByActivityId(int id) {
        return speakerRepository.getAllByActivityId(id)
                .map((s) -> conversionService.convert(s, SpeakerModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<SpeakerModel> getSpeakerModelsByShiftId(int id) {
        return speakerRepository.getAllByShiftId(id)
                .map((s) -> conversionService.convert(s, SpeakerModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<SpeakerModel> searchByName(String q, int activityId) throws InterruptedException {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        fullTextEntityManager.createIndexer().startAndWait();

        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Speaker.class)
                .get();


        Query query = queryBuilder
                .bool()
                .must(queryBuilder
                        .keyword()
                        .wildcard()
                        .onFields("firstName", "lastName")
                        .matching(q.toLowerCase())
                        .createQuery())
                .must(queryBuilder
                        .keyword()
                        .wildcard()
                        .onField("activity_id")
                        .matching(activityId)
                        .createQuery())
                .createQuery();
//
//
//
//        Query query = queryBuilder
//                .keyword()
//                .wildcard()
//                .onFields("firstName","lastName")
//                .matching(q.toLowerCase())
//                .createQuery();

        org.hibernate.search.jpa.FullTextQuery jpaQuery
                = fullTextEntityManager.createFullTextQuery(query, Speaker.class);

        return convertSpeakers(jpaQuery.getResultList().stream());
    }

    @Override
    public List<SpeakerForm> getSpeakerFormsBySpeakersId(List<Integer> speakersId) {
        if (speakersId == null) {
            return null;
        }

        List<SpeakerForm> speakerForms = new ArrayList<>(speakersId.size());
        for (int speakerId : speakersId) {
            speakerForms.add(new SpeakerForm(get(speakerId)));
        }

        return speakerForms;
    }

    @Override
    public boolean hasPermissionToWrite(int id) {
        return activityService.hasPermissionToWrite(get(id).getActivity().getId());
    }

    @Override
    public boolean hasPermissionToRead(int id) {
        return true;
    }

    private List<SpeakerModel> convertSpeakers(Stream<Speaker> speakers) {
        return speakers
                .map((t) -> conversionService.convert(t, SpeakerModel.class))
                .collect(Collectors.toList());
    }
}
