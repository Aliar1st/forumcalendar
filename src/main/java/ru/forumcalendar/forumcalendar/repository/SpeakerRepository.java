package ru.forumcalendar.forumcalendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.forumcalendar.forumcalendar.domain.Speaker;

import java.util.List;

public interface SpeakerRepository extends JpaRepository<Speaker, Integer> {

    List<Speaker> getAllByActivityIdOrderByCreatedAt(int activity_id);
}
