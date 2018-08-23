package ru.forumcalendar.forumcalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.forumcalendar.forumcalendar.config.constt.SessionAttributeName;
import ru.forumcalendar.forumcalendar.domain.Activity;
import ru.forumcalendar.forumcalendar.domain.Shift;
import ru.forumcalendar.forumcalendar.domain.Team;
import ru.forumcalendar.forumcalendar.model.UserModel;
import ru.forumcalendar.forumcalendar.model.form.UserForm;
import ru.forumcalendar.forumcalendar.service.ContactTypeService;
import ru.forumcalendar.forumcalendar.service.TeamMemberStatus;
import ru.forumcalendar.forumcalendar.service.TeamService;
import ru.forumcalendar.forumcalendar.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("user")
public class UserController {

    private static final String HTML_FOLDER = "user/";
    private static final String REDIRECT_ROOT_MAPPING = "redirect:/user/";

    private final TeamService teamService;
    private final UserService userService;
    private final ContactTypeService contactTypeService;
    private final ConversionService conversionService;

    @Autowired
    public UserController(
            TeamService teamService,
            UserService userService,
            ContactTypeService contactTypeService,
            @Qualifier("mvcConversionService") ConversionService conversionService
    ) {
        this.teamService = teamService;
        this.userService = userService;
        this.contactTypeService = contactTypeService;
        this.conversionService = conversionService;
    }

    @GetMapping("{id}")
    public String index(
            @PathVariable String id,
            Model model,
            HttpSession httpSession,
            RedirectAttributes redirectAttributes
    ) {
        int teamId = (int) httpSession.getAttribute(SessionAttributeName.CURRENT_TEAM_ATTRIBUTE);

        TeamMemberStatus teamMemberStatus = teamService.getStatus(teamId);
        if (TeamMemberStatus.OK != teamMemberStatus) {
            return teamService.resolveTeamError(teamMemberStatus, httpSession, redirectAttributes);
        }

        Team team = teamService.get(teamId);
        
        //model.addAttribute("direction", team.getDirection());

        Shift shift = team.getShift();
        model.addAttribute("shift", shift.getName());

        Activity activity = shift.getActivity();
        model.addAttribute("activity", activity.getName());

        model.addAttribute("isMyPage", id.equals(userService.getCurrentId()));
        model.addAttribute("user", conversionService.convert(userService.get(id), UserModel.class));

        return HTML_FOLDER + "index";
    }

    @GetMapping("{id}/edit")
    public String edit(
            @PathVariable String id,
            Model model
    ) {
        if (!id.equals(userService.getCurrentId())) {
            return REDIRECT_ROOT_MAPPING + id;
        }

        model.addAttribute(new UserForm(userService.getCurrentUser()));
        model.addAttribute("contactTypes", contactTypeService.getAll());

        return HTML_FOLDER + "edit";
    }

    @PostMapping("{id}/edit")
    public String edit(
            @PathVariable String id,
            @Valid UserForm userForm,
            BindingResult bindingResult
    ) {
        if (!id.equals(userService.getCurrentId())) {
            return REDIRECT_ROOT_MAPPING + id;
        }

        if (bindingResult.hasErrors()) {
            userForm.setPhotoUrl(userService.getCurrentUser().getPhoto());
            return HTML_FOLDER + "edit";
        }

        userForm.setId(id);
        userService.save(userForm);

        return REDIRECT_ROOT_MAPPING + id;
    }
}
