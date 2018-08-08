package ru.forumcalendar.forumcalendar.model;

import lombok.Getter;
import lombok.Setter;

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
