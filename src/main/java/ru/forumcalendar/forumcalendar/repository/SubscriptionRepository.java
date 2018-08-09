package ru.forumcalendar.forumcalendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.forumcalendar.forumcalendar.domain.Subscription;
import ru.forumcalendar.forumcalendar.domain.SubscriptionIdentity;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, SubscriptionIdentity> {

    Subscription getBySubscriptionIdentityEventIdAndSubscriptionIdentityUserId(int subscriptionIdentity_event_id, String subscriptionIdentity_user_id);

    List<Subscription> getAllBySubscriptionIdentityUserId(String subscriptionIdentity_user_id);
    // TODO: 8/9/2018 add order by
}
