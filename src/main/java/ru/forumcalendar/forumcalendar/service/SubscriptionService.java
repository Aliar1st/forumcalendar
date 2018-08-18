package ru.forumcalendar.forumcalendar.service;

import org.quartz.SchedulerException;
import ru.forumcalendar.forumcalendar.quartz.NotificationJob;
import ru.forumcalendar.forumcalendar.model.EventModel;

import java.util.List;

public interface SubscriptionService {

    void subscribe(int eventId, String userId);

    void unsubscribe(int eventId);

    boolean isSubscribed(int eventId);

    boolean toggleSubscribe(int eventId, String userId, NotificationJob.Job jobToDone) throws SchedulerException;

    List<EventModel> getEventModelsBySubscription(int shiftId);
}
