package ru.forumcalendar.forumcalendar.model;

import lombok.Getter;
import lombok.Setter;
import ru.forumcalendar.forumcalendar.domain.Shift;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class EventModel {

    private int id;

    private String name;

    private LocalDateTime datetime;

    private String place;

    private String description;

    private Shift shift;

    private int speakersCount;
}
