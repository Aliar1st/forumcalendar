package ru.forumcalendar.forumcalendar.model.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import ru.forumcalendar.forumcalendar.domain.Team;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class TeamForm {

    private int id;

//    private String userId;

    private int shiftId;

    @Min(value = 2, message = "Name is too short")
    @Max(value = 50, message = "Name is too long")
    @Pattern(regexp = "([A-Za-zА-Яа-я0-9]\\s?)+", message = "Name contains invalid characters")
    private String name;

    @Max(value = 500, message = "Direction is too long")
    @Pattern(regexp = "([A-Za-zА-Яа-я0-9]\\s?)+", message = "Direction contains invalid characters")
    private String direction;

    @Max(value = 5000, message = "Description is too long")
    private String description;

    public TeamForm(Team team) {
        this.id = team.getId();
//        this.userId = team.getUser().getId();
        this.shiftId = team.getShift().getId();
        this.name = team.getName();
        this.direction = team.getDirection();
        this.description = team.getDescription();
    }
}
