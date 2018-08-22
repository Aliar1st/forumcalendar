package ru.forumcalendar.forumcalendar.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.forumcalendar.forumcalendar.domain.Shift;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public abstract class EventModel implements Comparable<EventModel> {

    private int id;

    private int day;

    private String name;

    private LocalDateTime startDatetime;

    private LocalDateTime endDatetime;

    private String place;

    private String description;

    private boolean shiftEvent;

    public String getFormatStartTime() {
        return startDatetime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public String getFormatEndTime() {
        return endDatetime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    @Override
    public int compareTo(EventModel o) {
        return startDatetime.compareTo(o.startDatetime);
    }
}
