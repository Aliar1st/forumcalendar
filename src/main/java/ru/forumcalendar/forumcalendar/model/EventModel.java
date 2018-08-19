package ru.forumcalendar.forumcalendar.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class EventModel implements Comparable<EventModel> {

    private int id;

    private int day;

    private String name;

    private LocalDateTime startDatetime;

    private LocalDateTime endDatetime;

    private String place;

    private String description;

    private boolean shiftEvent;

    @Override
    public int compareTo(EventModel o) {
        if (startDatetime.isAfter(o.startDatetime)) {
            return 1;
        } else {
            return -1;
        }
    }
}
