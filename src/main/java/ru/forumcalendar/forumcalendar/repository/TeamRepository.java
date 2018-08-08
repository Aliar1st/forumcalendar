package ru.forumcalendar.forumcalendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.forumcalendar.forumcalendar.domain.Team;

public interface TeamRepository extends JpaRepository<Team, Integer> {

    int countAllByShift_id(int shift_id);
}
