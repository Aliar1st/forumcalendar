package ru.forumcalendar.forumcalendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.forumcalendar.forumcalendar.domain.Subscription;
import ru.forumcalendar.forumcalendar.domain.SubscriptionIdentity;

import java.util.List;
import java.util.stream.Stream;

public interface SubscriptionRepository extends JpaRepository<Subscription, SubscriptionIdentity> {

    Subscription getBySubscriptionIdentityEventIdAndSubscriptionIdentityUserId(int event_id, String user_id);

    @Query("SELECT s FROM Subscription s " +
           " WHERE s.subscriptionIdentity.user.id = ?1 " +
           "   AND s.subscriptionIdentity.event.shift.id = ?2 " +
           " ORDER BY s.subscriptionIdentity.event.startDatetime")
    Stream<Subscription> getAllByUserIdAndShiftId(String user_id, int shift_id);
}
