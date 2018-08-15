package ru.forumcalendar.forumcalendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.forumcalendar.forumcalendar.domain.TeamRole;

public interface TeamRoleRepository extends JpaRepository<TeamRole, Integer> {

    TeamRole getById(int id);
}
