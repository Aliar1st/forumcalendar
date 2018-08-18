package ru.forumcalendar.forumcalendar.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TeamEventModel {

    private int id;

    private int day;

    private String name;

    private LocalDateTime startDatetime;

    private LocalDateTime endDatetime;

    private String place;

    private String description;
}
