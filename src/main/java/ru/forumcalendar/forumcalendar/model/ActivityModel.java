package ru.forumcalendar.forumcalendar.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class ActivityModel {

    private int id;

    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    private String place;

    private String description;

    private int shiftCount;

    private int memberCount;

    private int teamCount;

    private int eventCount;

    private int speakerCount;
}
