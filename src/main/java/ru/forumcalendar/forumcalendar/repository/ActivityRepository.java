package ru.forumcalendar.forumcalendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.forumcalendar.forumcalendar.domain.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Integer> {
}
