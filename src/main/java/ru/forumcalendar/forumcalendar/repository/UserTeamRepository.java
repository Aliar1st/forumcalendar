package ru.forumcalendar.forumcalendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.forumcalendar.forumcalendar.domain.UserTeam;
import ru.forumcalendar.forumcalendar.domain.UserTeamIdentity;

import java.util.stream.Stream;

public interface UserTeamRepository extends JpaRepository<UserTeam, UserTeamIdentity> {

    @Query("SELECT ut FROM UserTeam ut " +
           " WHERE ut.userTeamIdentity.user.id = ?1 " +
           "   AND ut.userTeamIdentity.team.shift.id = ?2")
    UserTeam getByUserIdAndTeamShiftId(String user_id, int shift_id);

    @Query("SELECT ut FROM UserTeam ut " +
           " WHERE ut.userTeamIdentity.user.id = ?1 " +
           "   AND ut.userTeamIdentity.team.id = ?2")
    UserTeam getByUserIdAndTeamId(String user_id, int team_id);

    @Query("SELECT ut FROM UserTeam ut " +
           " WHERE ut.teamRole.id = ?1 " +
           "   AND ut.userTeamIdentity.team.id = ?2")
    UserTeam getByRoleIdAndTeamId(int role_id, int team_id);

    @Query("SELECT ut FROM UserTeam ut " +
           " WHERE ut.userTeamIdentity.team.shift.id = ?1 " +
           "   AND NOT EXISTS ( " +
           "           SELECT 'found' FROM UserTeam curators " +
           "            WHERE curators.teamRole.id = 1 " +
           "              AND curators.userTeamIdentity.team.shift.id = ?1 " +
           "              AND curators.id = ut.id " +
           "       )")
    Stream<UserTeam> getAllNotCuratorsByShiftId(int shift_id);

    @Query("SELECT ut FROM UserTeam ut " +
            " WHERE ut.userTeamIdentity.team.shift.id = ?1 " +
            "   AND NOT EXISTS ( " +
            "           SELECT 'found' FROM UserTeam curators " +
            "            WHERE curators.teamRole.id = 1 " +
            "              AND curators.userTeamIdentity.team.shift.activity.id = ?1 " +
            "              AND curators.id = ut.id " +
            "       )")
    Stream<UserTeam> getAllNotCuratorsByActivityId(int activity_id);

    @Query("SELECT ut FROM UserTeam ut " +
           " WHERE ut.teamRole.id = 1 " +
           "   AND ut.userTeamIdentity.team.shift.id = ?1")
    Stream<UserTeam> getAllCuratorsByShiftId(int shift_id);

    @Query("SELECT ut FROM UserTeam ut " +
           " WHERE ut.teamRole.id = 1 " +
           "   AND ut.userTeamIdentity.team.shift.activity.id = ?1")
    Stream<UserTeam> getAllCuratorsByActivityId(int activity_id);
}
