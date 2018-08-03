package ru.forumcalendar.forumcalendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.forumcalendar.forumcalendar.model.Activity;

public abstract class ActivityRepository implements JpaRepository<Activity, Integer> {
}
