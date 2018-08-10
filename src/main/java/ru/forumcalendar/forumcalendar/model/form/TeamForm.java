package ru.forumcalendar.forumcalendar.model.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import ru.forumcalendar.forumcalendar.domain.Team;
import ru.forumcalendar.forumcalendar.validation.annotation.ShiftExist;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class TeamForm {

    private int id = -1;

    @ShiftExist
    private int shiftId;

    @Length(max = 50, message = "Team name is too long")
    @Pattern(regexp = "[A-ZА-Я][A-Za-zА-Яа-я]+", message = "Team name is too short or contains invalid characters")
    private String name;

    @Length(max = 50, message = "Direction too long")
    @Pattern(regexp = "[A-ZА-Я][A-Za-zА-Яа-я ]+", message = "Direction is too short or contains invalid characters")
    private String direction;

    @Length(max = 5000, message = "Description is too long")
    @Pattern(regexp = "[A-ZА-Я].+", message = "Description is too short or contains invalid characters")
    private String description;

    public TeamForm(Team team) {
        this.id = team.getId();
        this.shiftId = team.getShift().getId();
        this.name = team.getName();
        this.direction = team.getDirection();
        this.description = team.getDescription();
    }
}
