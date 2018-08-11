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

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class EventForm {

    private int id = -1;

    @Length(max = 50, message = "Event name is too long")
    @Pattern(regexp = "[A-ZА-Я][A-Za-zА-Яа-я]+", message = "Event name is too short or contains invalid characters")
    private String name;

    @NotNull(message = "Enter date and time")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime datetime;

    @Length(max = 500, message = "Place field is too long")
    @Pattern(regexp = "[A-ZА-Я][A-Za-zА-Яа-я ]+", message = "Place field is too short or contains invalid characters")
    private String place;

    @Length(max = 5000, message = "Description is too long")
    @Pattern(regexp = "[A-ZА-Я].+", message = "Description is too short or contains invalid characters")
    private String description;

    @ShiftExist
    private int shiftId;

    @SpeakersExist
    private Integer[] speakersId;

    public EventForm(Event event) {
        this.id = event.getId();
        this.name = event.getName();
        this.datetime = event.getDatetime();
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
