package ru.forumcalendar.forumcalendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.forumcalendar.forumcalendar.domain.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

public interface EventRepository extends JpaRepository<Event, Integer> {

    @Query("SELECT e FROM Event e " +
           " WHERE e.shift.id = ?1 " +
           "   AND e.startDatetime BETWEEN ?2 AND ?3 " +
           " ORDER BY e.startDatetime")
    Stream<Event> getAllByShiftIdAndStartDatetimeBetween(int shift_id, LocalDateTime startDatetime, LocalDateTime endDatetime);

    Stream<Event> getAllByShiftIdOrderByStartDatetime(int shift_id);

    Stream<Event> getAllByShiftActivityIdOrderByStartDatetime(int activity_id);
}
