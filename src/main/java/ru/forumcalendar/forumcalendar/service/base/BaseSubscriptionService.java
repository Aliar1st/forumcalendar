package ru.forumcalendar.forumcalendar.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.forumcalendar.forumcalendar.domain.Subscription;
import ru.forumcalendar.forumcalendar.domain.SubscriptionIdentity;
import ru.forumcalendar.forumcalendar.domain.User;
import ru.forumcalendar.forumcalendar.model.EventModel;
import ru.forumcalendar.forumcalendar.repository.SubscriptionRepository;
import ru.forumcalendar.forumcalendar.service.EventService;
import ru.forumcalendar.forumcalendar.service.SubscriptionService;
import ru.forumcalendar.forumcalendar.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BaseSubscriptionService implements SubscriptionService {

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
    }

    @Override
    public void subscribe(int eventId) {

        String userId = userService.getCurrentId();
        Subscription subscription = subscriptionRepository.getBySubscriptionIdentityEventIdAndSubscriptionIdentityUserId(eventId, userId);

        if (subscription == null) {
            subscribe(eventId, userService.getCurrentUser());
        }
    }

    @Override
    public void unsubscribe(int eventId) {

        String userId = userService.getCurrentId();
        Subscription subscription = subscriptionRepository.getBySubscriptionIdentityEventIdAndSubscriptionIdentityUserId(eventId, userId);

        if (subscription != null) {
            unsubscribe(subscription);
        }
    }

    @Override
    public void toggleSubscribe(int eventId) {

        String userId = userService.getCurrentId();
        Subscription subscription = subscriptionRepository.getBySubscriptionIdentityEventIdAndSubscriptionIdentityUserId(eventId, userId);

        if (subscription == null) {
            subscribe(eventId, userService.getCurrentUser());
        } else {
            unsubscribe(subscription);
        }
    }

    @Override
    public List<EventModel> getEventModelsBySubscription() {
        return subscriptionRepository.getAllBySubscriptionIdentityUserId(userService.getCurrentId())
                .stream()
                .map((s) -> conversionService.convert(s.getSubscriptionIdentity().getEvent(), EventModel.class))
                .collect(Collectors.toList());
    }

    private void subscribe(int eventId, User user) {

        SubscriptionIdentity subscriptionIdentity = new SubscriptionIdentity();
        subscriptionIdentity.setUser(user);
        subscriptionIdentity.setEvent(eventService.get(eventId));

        Subscription subscription = new Subscription();
        subscription.setSubscriptionIdentity(subscriptionIdentity);

        subscriptionRepository.save(subscription);
    }

    private void unsubscribe(Subscription subscription) {
        subscriptionRepository.deleteById(subscription.getSubscriptionIdentity());
    }
}
