package ru.forumcalendar.forumcalendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.forumcalendar.forumcalendar.domain.UserTeam;

public interface UserTeamRepository extends JpaRepository<UserTeam, Integer> {
}
