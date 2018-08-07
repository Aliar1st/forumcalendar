package ru.forumcalendar.forumcalendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.forumcalendar.forumcalendar.domain.TeamRole;

import java.util.Optional;

public interface TeamRoleRepository extends JpaRepository<TeamRole, Integer> {
}
