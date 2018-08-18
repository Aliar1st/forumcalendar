package ru.forumcalendar.forumcalendar.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import ru.forumcalendar.forumcalendar.domain.Event;
import ru.forumcalendar.forumcalendar.model.EventModel;
import ru.forumcalendar.forumcalendar.model.SpeakerModel;
import ru.forumcalendar.forumcalendar.service.LikeService;
import ru.forumcalendar.forumcalendar.service.SubscriptionService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class EventModelConverter implements Converter<Event, EventModel> {

    private SpeakerModelConverter speakerModelConverter;

    private LikeService likeService;

    private SubscriptionService subscriptionService;

    @Override
    public EventModel convert(Event event) {

        EventModel eventModel = new EventModel();
        eventModel.setId(event.getId());
        eventModel.setName(event.getName());
        eventModel.setPlace(event.getPlace());
        eventModel.setStartDatetime(event.getStartDatetime());
        eventModel.setEndDatetime(event.getEndDatetime());
        eventModel.setDescription(event.getDescription());
//        eventModel.setShift(event.getShift());

        eventModel.setLikes(likeService.getLikes(event.getId()));
        eventModel.setDislikes(likeService.getDislikes(event.getId()));
        eventModel.setFavorite(subscriptionService.isSubscribed(event.getId()));

        List<SpeakerModel> speakers = event.getSpeakers()
                .stream()
                .map((s) -> speakerModelConverter.convert(s))
                .collect(Collectors.toList());

        eventModel.setSpeakers(speakers);

        LocalDateTime startEvent = event.getStartDatetime();
        LocalDateTime startShift = event.getShift().getStartDate().atStartOfDay();

        eventModel.setDay((int) ChronoUnit.DAYS.between(startShift, startEvent));

        return eventModel;

    }

    @Autowired
    public void setSpeakerModelConverter(@Lazy SpeakerModelConverter speakerModelConverter) {
        this.speakerModelConverter = speakerModelConverter;
    }

    @Autowired
    public void setLikeService(@Lazy LikeService likeService) {
        this.likeService = likeService;
    }

    @Autowired
    public void setSubscriptionService(@Lazy SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }
}
