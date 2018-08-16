package ru.forumcalendar.forumcalendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.forumcalendar.forumcalendar.domain.Team;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Integer> {

    List<Team> getAllByShiftIdOrderByCreatedAt(int shift_id);

    @Query("SELECT t FROM Team t " +
           " WHERE t.shift.id = ?1 " +
           "   AND NOT EXISTS ( " +
           "           SELECT 'found' FROM UserTeam ut " +
           "           WHERE ut.userTeamIdentity.team.id = t.id " +
           "             AND ut.teamRole.id = 1 " +
           "       ) " +
           " ORDER BY t.createdAt")
    List<Team> getAllByShiftIdAndWithoutCurator(int shift_id);

    int countAllByShift_id(int shift_id);
}
