package ru.forumcalendar.forumcalendar.validation;

import org.springframework.beans.factory.annotation.Autowired;
import ru.forumcalendar.forumcalendar.service.ActivityService;
import ru.forumcalendar.forumcalendar.service.TeamRoleService;
import ru.forumcalendar.forumcalendar.validation.annotation.TeamRoleExist;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TeamRoleExistValidator implements ConstraintValidator<TeamRoleExist, Integer> {

    private final TeamRoleService teamRoleService;

    @Autowired
    public TeamRoleExistValidator(TeamRoleService teamRoleService) {
        this.teamRoleService = teamRoleService;
    }

    @Override
    public boolean isValid(Integer teamRoleId, ConstraintValidatorContext cvc) {
        return teamRoleService.exist(teamRoleId);
    }
}
