package ru.forumcalendar.forumcalendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.forumcalendar.forumcalendar.domain.Shift;

import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift, Integer> {

    List<Shift> getAllByActivity_Id(int activity_id);
}
