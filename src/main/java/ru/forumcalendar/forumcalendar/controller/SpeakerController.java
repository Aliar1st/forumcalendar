package ru.forumcalendar.forumcalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.forumcalendar.forumcalendar.converter.SpeakerModelConverter;
import ru.forumcalendar.forumcalendar.model.SpeakerModel;
import ru.forumcalendar.forumcalendar.model.form.SpeakerForm;
import ru.forumcalendar.forumcalendar.service.PermissionService;
import ru.forumcalendar.forumcalendar.service.SpeakerService;
import ru.forumcalendar.forumcalendar.service.TeamService;
import ru.forumcalendar.forumcalendar.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/speaker")
public class SpeakerController {

    private static final String HTML_FOLDER = "speaker/";
    private static final String REDIRECT_ROOT_MAPPING = "redirect:/speaker/speakers?activityId=";

    private final TeamService teamService;
    private final UserService userService;
    private final SpeakerService speakerService;
    private final PermissionService permissionService;

    private final SpeakerModelConverter speakerModelConverter;

    private final PermissionEvaluator permissionEvaluator;

    @Autowired
    public SpeakerController(
            TeamService teamService,
            UserService userService,
            SpeakerService speakerService,
            PermissionService permissionService,
            SpeakerModelConverter speakerModelConverter,
            PermissionEvaluator permissionEvaluator) {
        this.teamService = teamService;
        this.userService = userService;
        this.speakerService = speakerService;
        this.permissionService = permissionService;
        this.speakerModelConverter = speakerModelConverter;
        this.permissionEvaluator = permissionEvaluator;
    }

    @GetMapping("{id}")
    public String speaker(
            @PathVariable int id,
            Model model
    ) {

        SpeakerModel speaker = speakerModelConverter.convert(speakerService.get(id));

        PermissionService.Identifier identifier = permissionService.identifyUser(speaker.getActivityId());

        model.addAttribute(identifier.getValue(), true);
        model.addAttribute("speaker", speaker);
        model.addAttribute("events", speaker.getEvents());

        return HTML_FOLDER + "speaker";
    }

    @GetMapping("/speakers")
    public String speakers(
            @RequestParam int activityId,
            Model model
    ) {

        PermissionService.Identifier identifier = permissionService.identifyUser(activityId);

        model.addAttribute(identifier.getValue(), true);
        model.addAttribute("curActivityId", activityId);
        model.addAttribute("speakers", speakerService.getSpeakerModelsByActivityId(activityId));

        return HTML_FOLDER + "speakers";
    }

    @GetMapping("add")
    public String add(
            @RequestParam int activityId,
            Authentication authentication,
            Model model
    ) {
        if (!permissionEvaluator.hasPermission(authentication, activityId, "Activity", "w")) {
            return REDIRECT_ROOT_MAPPING + activityId;
        }

        SpeakerForm speakerForm = new SpeakerForm();
        speakerForm.setActivityId(activityId);

        model.addAttribute(speakerForm);

        return HTML_FOLDER + "add";
    }

    @PostMapping("add")
    public String add(
            @Valid SpeakerForm speakerForm,
            BindingResult bindingResult,
            Authentication authentication

    ) {
        if (!permissionEvaluator.hasPermission(authentication, speakerForm.getActivityId(), "Activity", "w")) {
            return REDIRECT_ROOT_MAPPING + speakerForm.getActivityId();
        }

        if (bindingResult.hasErrors()) {
            return HTML_FOLDER + "add";
        }

        speakerService.save(speakerForm);

        return REDIRECT_ROOT_MAPPING + speakerForm.getActivityId();
    }


    @GetMapping("{id}/edit")
    public String edit(
            @PathVariable int id,
            Model model,
            Authentication authentication
    ) {
        int activityId = speakerService.get(id).getActivity().getId();

        if (!permissionEvaluator.hasPermission(authentication, activityId, "Activity", "w")) {
            return REDIRECT_ROOT_MAPPING + activityId;
        }

        SpeakerForm speakerForm = new SpeakerForm(speakerService.get(id));
        model.addAttribute(speakerForm);

        return HTML_FOLDER + "edit";
    }

    @PostMapping("{id}/edit")
    public String edit(
            @PathVariable int id,
            @Valid SpeakerForm speakerForm,
            BindingResult bindingResult,
            Authentication authentication
    ) {
        if (!permissionEvaluator.hasPermission(authentication, speakerForm.getActivityId(), "Activity", "w")) {
            return REDIRECT_ROOT_MAPPING + speakerForm.getActivityId();
        }

        if (bindingResult.hasErrors()) {
            return HTML_FOLDER + "/edit";
        }

        speakerForm.setId(id);
        speakerService.save(speakerForm);

        return REDIRECT_ROOT_MAPPING + speakerForm.getActivityId();
    }


    @PostMapping("/search")
    public String search(
            @RequestParam String q,
            @RequestParam int activityId,
            HttpSession httpSession,
            Principal principal,
            Model model
    ) throws InterruptedException {

        PermissionService.Identifier identifier = permissionService.identifyUser(activityId);

        model.addAttribute(identifier.getValue(), true);
        model.addAttribute("curActivityId", activityId);
        model.addAttribute("speakers", speakerService.searchByName(q, activityId));


        return HTML_FOLDER + "_speakers_list";
    }

    @PostMapping("/partialSpeakers")
    public String partialTeams(
            @RequestParam int activityId,
            Model model
    ) {

        model.addAttribute("speakers", speakerService.getSpeakerModelsByActivityId(activityId));

        return HTML_FOLDER + "/_speakers_list";
    }
}
