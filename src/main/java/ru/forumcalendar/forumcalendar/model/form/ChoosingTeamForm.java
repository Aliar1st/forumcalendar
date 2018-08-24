package ru.forumcalendar.forumcalendar.model.form;

import lombok.Getter;
import lombok.Setter;
import ru.forumcalendar.forumcalendar.validation.annotation.AvailableTeamRole;
import ru.forumcalendar.forumcalendar.validation.annotation.TeamExist;
import ru.forumcalendar.forumcalendar.validation.annotation.TeamRoleExist;

@Getter
@Setter
@AvailableTeamRole(
        teamId = "teamId",
        roleId = "teamRoleId"
)
public class ChoosingTeamForm {

    @TeamExist
    private int teamId;

    @TeamRoleExist
    private int teamRoleId;
}
