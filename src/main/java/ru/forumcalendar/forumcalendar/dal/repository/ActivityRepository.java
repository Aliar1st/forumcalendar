package ru.forumcalendar.forumcalendar.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.forumcalendar.forumcalendar.dal.model.Activity;

public abstract class ActivityRepository implements JpaRepository<Activity, Integer> {
}
