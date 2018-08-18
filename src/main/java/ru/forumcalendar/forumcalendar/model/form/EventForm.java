package ru.forumcalendar.forumcalendar.model.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import ru.forumcalendar.forumcalendar.domain.Event;
import ru.forumcalendar.forumcalendar.domain.Speaker;
import ru.forumcalendar.forumcalendar.validation.annotation.ShiftExist;
import ru.forumcalendar.forumcalendar.validation.annotation.SpeakersExist;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class EventForm {

    private int id;

    @Size(min = 2, message = "Name is too short")
    @Size(max = 50, message = "Name is too long")
    @Pattern(regexp = "([A-Za-zА-Яа-я0-9]\\s?)+", message = "Name contains invalid characters")
    private String name;

    @NotNull(message = "Enter start date and time")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDatetime;

    @NotNull(message = "Enter end date and time")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDatetime;

    @Size(max = 500, message = "Description is too long")
    @Pattern(regexp = "([A-Za-zА-Яа-я0-9.,]\\s?)+", message = "Place field is too short or contains invalid characters")
    private String place;

    @Size(max = 5000, message = "Description is too long")
    private String description;

    @ShiftExist
    private int shiftId;

    @SpeakersExist
    private Integer[] speakersId;

    public EventForm(Event event) {
        this.id = event.getId();
        this.name = event.getName();
        this.startDatetime = event.getStartDatetime();
        this.endDatetime = event.getEndDatetime();
        this.place = event.getPlace();
        this.description = event.getDescription();
        this.shiftId = event.getShift().getId();

        Set<Speaker> speakers = event.getSpeakers();
        this.speakersId = new Integer[speakers.size()];

        int i = 0;
        for (Speaker speaker : speakers) {
            this.speakersId[i] = speaker.getId();
            i++;
        }
    }
}