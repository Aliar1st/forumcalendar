package ru.forumcalendar.forumcalendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.forumcalendar.forumcalendar.domain.Speaker;

import java.util.List;
import java.util.stream.Stream;

public interface SpeakerRepository extends JpaRepository<Speaker, Integer> {

    Stream<Speaker> getAllByActivityId(int activity_id);

    @Query("SELECT s FROM Speaker s " +
           "JOIN Shift sh ON sh.activity.id = s.activity.id " +
           "WHERE sh.id = ?1")
    Stream<Speaker> getAllByShiftId(int shift_id);
}
