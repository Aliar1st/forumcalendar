package ru.forumcalendar.forumcalendar.model.form;

import lombok.Getter;
import lombok.Setter;
import ru.forumcalendar.forumcalendar.validation.annotation.ShiftExist;
import ru.forumcalendar.forumcalendar.validation.annotation.TeamRoleExist;

@Getter
@Setter
public class ChoosingTeamRoleForm {

    @ShiftExist
    private int shiftId;

    @TeamRoleExist
    private int teamRoleId;
}
