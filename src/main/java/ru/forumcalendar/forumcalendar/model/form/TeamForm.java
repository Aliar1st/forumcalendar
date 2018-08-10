package ru.forumcalendar.forumcalendar.model.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.forumcalendar.forumcalendar.domain.Team;

@Getter
@Setter
@NoArgsConstructor
public class TeamForm {

    private int id = -1;

    private String userId;

    private int shiftId;

    private String name;

    private String direction;

    private String description;

    public TeamForm(Team team) {
        this.id = team.getId();
        this.userId = team.getUser().getId();
        this.shiftId = team.getShift().getId();
        this.name = team.getName();
        this.direction = team.getDirection();
        this.description = team.getDescription();
    }
}
