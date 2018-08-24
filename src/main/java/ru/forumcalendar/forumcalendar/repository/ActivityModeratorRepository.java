package ru.forumcalendar.forumcalendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.forumcalendar.forumcalendar.domain.ActivityModerator;
import ru.forumcalendar.forumcalendar.domain.ActivityModeratorIdentity;

public interface ActivityModeratorRepository extends JpaRepository<ActivityModerator, ActivityModeratorIdentity> {

    @Query("SELECT am FROM ActivityModerator am " +
            " WHERE am.activityModeratorIdentity.user.id = ?1 " +
            "   AND am.activityModeratorIdentity.activity.id = ?2")
    ActivityModerator getByUserIdAndActivityId(String user_id, int activity_id);
}
