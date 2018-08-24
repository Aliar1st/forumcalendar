package ru.forumcalendar.forumcalendar.validation;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import ru.forumcalendar.forumcalendar.domain.TeamRole;
import ru.forumcalendar.forumcalendar.service.TeamRoleService;
import ru.forumcalendar.forumcalendar.validation.annotation.AvailableTeamRole;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AvailableTeamRoleValidator implements ConstraintValidator<AvailableTeamRole, Object> {

    private final TeamRoleService teamRoleService;

    private String roleIdField;
    private String teamIdField;

    @Autowired
    public AvailableTeamRoleValidator(TeamRoleService teamRoleService) {
        this.teamRoleService = teamRoleService;
    }

    @Override
    public void initialize(AvailableTeamRole constraint) {
        this.roleIdField = constraint.roleId();
        this.teamIdField = constraint.teamId();
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {

        int roleId = (int) new BeanWrapperImpl(value).getPropertyValue(this.roleIdField);
        int teamId = (int) new BeanWrapperImpl(value).getPropertyValue(this.teamIdField);

        return !(roleId == TeamRole.ROLE_CURATOR_ID && teamRoleService.isExistCurator(teamId));
    }
}
