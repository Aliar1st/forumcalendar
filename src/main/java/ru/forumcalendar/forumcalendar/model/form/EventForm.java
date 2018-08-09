package ru.forumcalendar.forumcalendar.model.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.forumcalendar.forumcalendar.domain.Event;
import ru.forumcalendar.forumcalendar.domain.Shift;
import ru.forumcalendar.forumcalendar.domain.Speaker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class EventForm {

    private int id = -1;

    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime datetime;

    private String place;

    private String description;

    private int shiftId;

    private List<SpeakerForm> speakerForms;

    public EventForm(Event event) {
        this.id = event.getId();
        this.name = event.getName();
        this.datetime = event.getDatetime();
        this.place = event.getPlace();
        this.description = event.getDescription();
        this.shiftId = event.getShift().getId();

        Set<Speaker> speakers = event.getSpeakers();
        this.speakerForms = new ArrayList<>(speakers.size());

        for (Speaker speaker : speakers) {
            this.speakerForms.add(new SpeakerForm(speaker));
        }
    }
}
