package ru.forumcalendar.forumcalendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.forumcalendar.forumcalendar.domain.Like;
import ru.forumcalendar.forumcalendar.domain.LikeIdentity;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, LikeIdentity> {

    int countAllByLikeIdentityEventIdAndIsLikeTrue(int likeIdentity_event_id);

    int countAllByLikeIdentityEventIdAndIsLikeFalse(int likeIdentity_event_id);

    Like getByLikeIdentityEventIdAndLikeIdentityUserId(int likeIdentity_event_id, String likeIdentity_user_id);
}
