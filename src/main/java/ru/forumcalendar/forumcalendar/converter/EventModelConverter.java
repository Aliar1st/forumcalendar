package ru.forumcalendar.forumcalendar.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import ru.forumcalendar.forumcalendar.domain.Event;
import ru.forumcalendar.forumcalendar.model.ShiftEventModel;
import ru.forumcalendar.forumcalendar.model.InnerSpeakerModel;
import ru.forumcalendar.forumcalendar.service.LikeService;
import ru.forumcalendar.forumcalendar.service.SubscriptionService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class EventModelConverter implements Converter<Event, ShiftEventModel> {

    private InnerSpeakerModelConverter innerSpeakerModelConverter;

    private LikeService likeService;

    private SubscriptionService subscriptionService;

    @Override
    public ShiftEventModel convert(Event event) {

        ShiftEventModel shiftEventModel = new ShiftEventModel();
        shiftEventModel.setId(event.getId());
        shiftEventModel.setName(event.getName());
        shiftEventModel.setPlace(event.getPlace());
        shiftEventModel.setStartDatetime(event.getStartDatetime());
        shiftEventModel.setEndDatetime(event.getEndDatetime());
        shiftEventModel.setDescription(event.getDescription());
//        shiftEventModel.setShift(event.getShift());

        shiftEventModel.setLikes(likeService.getLikes(event.getId()));
        shiftEventModel.setDislikes(likeService.getDislikes(event.getId()));
        shiftEventModel.setFavorite(subscriptionService.isSubscribed(event.getId()));

        List<InnerSpeakerModel> speakers = event.getSpeakers()
                .stream()
                .map((s) -> innerSpeakerModelConverter.convert(s))
                .collect(Collectors.toList());

        shiftEventModel.setSpeakers(speakers);

        LocalDateTime startEvent = event.getStartDatetime();
        LocalDateTime startShift = event.getShift().getStartDate().atStartOfDay();

        shiftEventModel.setDay((int) ChronoUnit.DAYS.between(startShift, startEvent));

        return shiftEventModel;

    }

    @Autowired
    public void setInnerSpeakerModelConverter(@Lazy InnerSpeakerModelConverter innerSpeakerModelConverter) {
        this.innerSpeakerModelConverter = innerSpeakerModelConverter;
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
