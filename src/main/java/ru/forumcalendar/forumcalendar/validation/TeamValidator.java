package ru.forumcalendar.forumcalendar.validation;

import org.springframework.beans.factory.annotation.Autowired;
import ru.forumcalendar.forumcalendar.service.TeamService;
import ru.forumcalendar.forumcalendar.validation.annotation.TeamExist;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TeamValidator implements
        ConstraintValidator<TeamExist, Integer> {

    private final TeamService teamService;

    @Autowired
    public TeamValidator(TeamService teamService) {
        this.teamService = teamService;
    }

    @Override
    public boolean isValid(Integer teamId, ConstraintValidatorContext constraintValidatorContext) {
        return teamService.exist(teamId);
    }
}
