package ru.forumcalendar.forumcalendar.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import ru.forumcalendar.forumcalendar.domain.Event;
import ru.forumcalendar.forumcalendar.model.InnerEventModel;
import ru.forumcalendar.forumcalendar.service.LikeService;
import ru.forumcalendar.forumcalendar.service.SubscriptionService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class InnerEventModelConverter implements Converter<Event, InnerEventModel> {

    private LikeService likeService;

    private SubscriptionService subscriptionService;

    @Override
    public InnerEventModel convert(Event event) {
        InnerEventModel innerEventModel = new InnerEventModel();
        innerEventModel.setId(event.getId());
        innerEventModel.setName(event.getName());
        innerEventModel.setPlace(event.getPlace());
        innerEventModel.setStartDatetime(event.getStartDatetime());
        innerEventModel.setEndDatetime(event.getEndDatetime());
        innerEventModel.setDescription(event.getDescription());
//        innerEventModel.setShift(event.getShift());

        innerEventModel.setLikes(likeService.getLikes(event.getId()));
        innerEventModel.setDislikes(likeService.getDislikes(event.getId()));
        innerEventModel.setFavorite(subscriptionService.isSubscribed(event.getId()));

        LocalDateTime startEvent = event.getStartDatetime();
        LocalDateTime startShift = event.getShift().getStartDate().atStartOfDay();

        innerEventModel.setDay((int) ChronoUnit.DAYS.between(startShift, startEvent));

        return innerEventModel;
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
