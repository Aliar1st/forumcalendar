package ru.forumcalendar.forumcalendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.forumcalendar.forumcalendar.domain.Activity;

import java.util.List;
import java.util.stream.Stream;

public interface ActivityRepository extends JpaRepository<Activity, Integer> {

    Stream<Activity> getAllByUserId(String user_id);
}
