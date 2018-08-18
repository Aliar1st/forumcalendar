package ru.forumcalendar.forumcalendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.forumcalendar.forumcalendar.domain.Speaker;

import java.util.List;
import java.util.stream.Stream;

public interface SpeakerRepository extends JpaRepository<Speaker, Integer> {

    Stream<Speaker> getAllByActivityId(int activity_id);
}
