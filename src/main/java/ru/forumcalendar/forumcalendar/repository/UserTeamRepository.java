package ru.forumcalendar.forumcalendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.forumcalendar.forumcalendar.domain.UserTeam;
import ru.forumcalendar.forumcalendar.domain.UserTeamIdentity;

public interface UserTeamRepository extends JpaRepository<UserTeam, UserTeamIdentity> {

    @Query("SELECT u FROM UserTeam u " +
            " WHERE u.userTeamIdentity.user.id = ?1 " +
            "   AND u.userTeamIdentity.team.shift.id = ?2")
    UserTeam getByUserIdAndTeamShiftId(String user_id, int shift_id);

    @Query("SELECT u FROM UserTeam u " +
            " WHERE u.userTeamIdentity.user.id = ?1 " +
            "   AND u.userTeamIdentity.team.id = ?2")
    UserTeam getByUserIdAndTeamId(String user_id, int team_id);
}
