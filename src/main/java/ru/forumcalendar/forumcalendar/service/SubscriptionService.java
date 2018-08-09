package ru.forumcalendar.forumcalendar.service;

import ru.forumcalendar.forumcalendar.domain.Event;
import ru.forumcalendar.forumcalendar.model.EventModel;

import java.util.List;

public interface SubscriptionService {

    void subscribe(int eventId);

    void unsubscribe(int eventId);

    void toggleSubscribe(int eventId);

    List<EventModel> getEventModelsBySubscription();
}
