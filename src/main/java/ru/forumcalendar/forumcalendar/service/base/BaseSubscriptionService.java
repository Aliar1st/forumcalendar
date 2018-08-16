package ru.forumcalendar.forumcalendar.service.base;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.forumcalendar.forumcalendar.Quartz.NotificationExecutor;
import ru.forumcalendar.forumcalendar.Quartz.NotificationJob;
import ru.forumcalendar.forumcalendar.domain.Event;
import ru.forumcalendar.forumcalendar.domain.Subscription;
import ru.forumcalendar.forumcalendar.domain.SubscriptionIdentity;
import ru.forumcalendar.forumcalendar.model.EventModel;
import ru.forumcalendar.forumcalendar.repository.SubscriptionRepository;
import ru.forumcalendar.forumcalendar.service.EventService;
import ru.forumcalendar.forumcalendar.service.SubscriptionService;
import ru.forumcalendar.forumcalendar.service.UserService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BaseSubscriptionService implements SubscriptionService {

    private final static int TRIGGERING_MINUTES_BEFORE = 10;

    private List<NotificationExecutor> notifyExecs;

    private final SubscriptionRepository subscriptionRepository;

    private final EventService eventService;
    private final UserService userService;
    private final ConversionService conversionService;

    @Autowired
    public BaseSubscriptionService(
            SubscriptionRepository subscriptionRepository,
            EventService eventService,
            UserService userService,
            @Qualifier("mvcConversionService") ConversionService conversionService) {
        this.subscriptionRepository = subscriptionRepository;
        this.eventService = eventService;
        this.userService = userService;
        this.conversionService = conversionService;
        notifyExecs = new ArrayList<>();
    }

    @Override
    public void subscribe(int eventId, String userId) {

        Subscription subscription = subscriptionRepository.getBySubscriptionIdentityEventIdAndSubscriptionIdentityUserId(eventId, userId);

        if (subscription == null) {
            _subscribe(eventId, userId);
        }
    }

    @Override
    public void unsubscribe(int eventId) {

        String userId = userService.getCurrentId();
        Subscription subscription = subscriptionRepository
                .getBySubscriptionIdentityEventIdAndSubscriptionIdentityUserId(eventId, userId);

        if (subscription != null) {
            unsubscribe(subscription);
        }
    }

    @Override
    public boolean isSubscribed(int eventId) {
        return false;
    }

    @Override
    public boolean toggleSubscribe(int eventId, String userId, NotificationJob.Job jobToDone) throws SchedulerException {

        Subscription subscription = subscriptionRepository
                .getBySubscriptionIdentityEventIdAndSubscriptionIdentityUserId(eventId, userId);

        if (subscription == null) {
            Event event = eventService.get(eventId);

            if (event.getStartDatetime().isAfter(LocalDateTime.now().plus(TRIGGERING_MINUTES_BEFORE + 1, ChronoUnit.MINUTES))) {
                NotificationExecutor notifyExec = new NotificationExecutor(eventId);
                notifyExecs.add(notifyExec);
                notifyExec.executeAt(
                        jobToDone,
                        event.getStartDatetime().minus(TRIGGERING_MINUTES_BEFORE, ChronoUnit.MINUTES)
                );
            }

            subscribe(eventId, userId);
            return true;
        } else {
            for (NotificationExecutor ne : notifyExecs) {
                if (ne.getEventId() == eventId) {
                    ne.clear();
                    notifyExecs.remove(ne);
                    break;
                }
            }
            unsubscribe(subscription);
            return false;
        }
    }

    @Override
    public List<EventModel> getEventModelsBySubscription(int shiftId) {
        return subscriptionRepository.getAllByUserIdAndShiftId(userService.getCurrentId(), shiftId)
                .stream()
                .map((s) -> conversionService.convert(s.getSubscriptionIdentity().getEvent(), EventModel.class))
                .collect(Collectors.toList());
    }

    private void _subscribe(int eventId, String userId) {

        SubscriptionIdentity subscriptionIdentity = new SubscriptionIdentity();
        subscriptionIdentity.setUser(userService.get(userId));
        subscriptionIdentity.setEvent(eventService.get(eventId));

        Subscription subscription = new Subscription();
        subscription.setSubscriptionIdentity(subscriptionIdentity);

        subscriptionRepository.save(subscription);
    }

    private void unsubscribe(Subscription subscription) {
        subscriptionRepository.deleteById(subscription.getSubscriptionIdentity());
    }
}
