package ru.forumcalendar.forumcalendar.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class ShiftModel {

    private int id;

    private int activityId;

    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    private String description;

    private int teamCount;

    public String getFormatStartDate() {
        return startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getFormatEndDate() {
        return endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
