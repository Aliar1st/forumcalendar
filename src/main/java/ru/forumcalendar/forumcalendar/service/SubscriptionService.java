package ru.forumcalendar.forumcalendar.service;

import org.quartz.SchedulerException;
import ru.forumcalendar.forumcalendar.model.ShiftEventModel;
import ru.forumcalendar.forumcalendar.quartz.NotificationJob;

import java.util.List;

public interface SubscriptionService {

    void subscribe(int eventId, String userId);

    void unsubscribe(int eventId);

    boolean isSubscribed(int eventId);

    boolean toggleSubscribe(int eventId, String userId, NotificationJob.Job jobToDone) throws SchedulerException;

    List<ShiftEventModel> getEventModelsBySubscription(int shiftId);
}
