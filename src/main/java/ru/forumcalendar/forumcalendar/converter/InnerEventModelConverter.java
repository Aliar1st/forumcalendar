package ru.forumcalendar.forumcalendar.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import ru.forumcalendar.forumcalendar.domain.Event;
import ru.forumcalendar.forumcalendar.model.InnerShiftEventModel;
import ru.forumcalendar.forumcalendar.service.LikeService;
import ru.forumcalendar.forumcalendar.service.SubscriptionService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class InnerEventModelConverter implements Converter<Event, InnerShiftEventModel> {

    private LikeService likeService;

    private SubscriptionService subscriptionService;

    @Override
    public InnerShiftEventModel convert(Event event) {
        InnerShiftEventModel innerShiftEventModel = new InnerShiftEventModel();
        innerShiftEventModel.setId(event.getId());
        innerShiftEventModel.setName(event.getName());
        innerShiftEventModel.setPlace(event.getPlace());
        innerShiftEventModel.setStartDatetime(event.getStartDatetime());
        innerShiftEventModel.setEndDatetime(event.getEndDatetime());
        innerShiftEventModel.setDescription(event.getDescription());
//        innerShiftEventModel.setShift(event.getShift());

        innerShiftEventModel.setLikes(likeService.getLikes(event.getId()));
        innerShiftEventModel.setDislikes(likeService.getDislikes(event.getId()));
        innerShiftEventModel.setFavorite(subscriptionService.isSubscribed(event.getId()));

        LocalDateTime startEvent = event.getStartDatetime();
        LocalDateTime startShift = event.getShift().getStartDate().atStartOfDay();

        innerShiftEventModel.setDay((int) ChronoUnit.DAYS.between(startShift, startEvent));

        return innerShiftEventModel;
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
