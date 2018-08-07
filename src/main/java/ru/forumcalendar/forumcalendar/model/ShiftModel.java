package ru.forumcalendar.forumcalendar.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.forumcalendar.forumcalendar.domain.Shift;

import java.time.LocalDate;

@Getter
@Setter
public class ShiftModel {

    private int id;

    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    private int teamCount;
}
