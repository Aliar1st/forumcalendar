package ru.forumcalendar.forumcalendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.forumcalendar.forumcalendar.domain.TeamEvent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

public interface TeamEventRepository extends JpaRepository<TeamEvent, Integer> {

    @Query("SELECT te FROM TeamEvent te " +
            " WHERE te.team.id = ?1 " +
            "   AND (te.startDatetime BETWEEN ?2 AND ?3 " +
            "    OR  te.endDatetime   BETWEEN ?2 AND ?3) " +
            " ORDER BY te.startDatetime")
    Stream<TeamEvent> getAllByTeamIdAndStartDatetimeBetween(int team_id, LocalDateTime startDatetime, LocalDateTime endDatetime);

    Stream<TeamEvent> getByTeamIdOrderByStartDatetime(int team_id);
}
