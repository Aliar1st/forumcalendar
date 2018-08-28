package ru.forumcalendar.forumcalendar.model.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.forumcalendar.forumcalendar.domain.TeamEvent;
import ru.forumcalendar.forumcalendar.validation.annotation.TeamExist;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TeamEventForm {

    private int id;

    @Size(max = 50, message = "Name is too long")
    @Pattern(regexp = "([A-Za-zА-Яа-я0-9]\\s?)+", message = "Name contains invalid characters or too short")
    private String name;

    @NotNull(message = "Enter start date and time")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDatetime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDatetime;

    @Size(max = 500, message = "Description is too long")
    @Pattern(regexp = "([A-Za-zА-Яа-я0-9.,]\\s?)+", message = "Place field is too short or contains invalid characters")
    private String place;

    @Size(max = 5000, message = "Description is too long")
    private String description;

    @TeamExist
    private int teamId;

    public TeamEventForm(TeamEvent teamEvent) {
        this.id = teamEvent.getId();
        teamId = teamEvent.getTeam().getId();
        name = teamEvent.getName();
        startDatetime = teamEvent.getStartDatetime();
        endDatetime = teamEvent.getEndDatetime();
        place = teamEvent.getPlace();
        description = teamEvent.getDescription();
    }
}
