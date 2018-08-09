package ru.forumcalendar.forumcalendar.model;

import lombok.Getter;
import lombok.Setter;
import ru.forumcalendar.forumcalendar.domain.Shift;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class EventModel {

    private int id;

    private String name;

    private LocalDate date;

    private LocalTime time;

    private String place;

    private String description;

    private Shift shift;

    private int speakersCount;
}
