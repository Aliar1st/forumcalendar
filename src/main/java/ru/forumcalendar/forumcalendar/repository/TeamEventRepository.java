package ru.forumcalendar.forumcalendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.forumcalendar.forumcalendar.domain.TeamEvent;

import java.util.List;

public interface TeamEventRepository extends JpaRepository<TeamEvent, Integer> {

    List<TeamEvent> getByTeamId(int team_id);
}
