package ru.forumcalendar.forumcalendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.forumcalendar.forumcalendar.domain.Shift;

import java.util.List;
import java.util.stream.Stream;

public interface ShiftRepository extends JpaRepository<Shift, Integer> {

    void deleteByActivity_Id(int activityId);

    Stream<Shift> getAllByActivityIdOrderByCreatedAt(int activity_id);
}
