package ru.forumcalendar.forumcalendar.controller.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.forumcalendar.forumcalendar.model.form.ShiftForm;
import ru.forumcalendar.forumcalendar.service.ShiftService;

import javax.validation.Valid;

@Controller
@RequestMapping("editor/activity/{activityId}/shift")
public class ShiftController {

    private static final String HTML_FOLDER = "editor/shift/";

    private final ShiftService shiftService;

    @Autowired
    public ShiftController(
            ShiftService shiftService
    ) {
        this.shiftService = shiftService;
    }

    @PreAuthorize("@baseActivityService.hasPermissionToWrite(#activityId) or hasRole('SUPERUSER')")
    @GetMapping("")
    public String index(
            @P("activityId") @PathVariable int activityId,
            Model model
    ) {

        model.addAttribute("shiftModels", shiftService.getShiftModelsByActivityId(activityId));

        return HTML_FOLDER + "index";
    }

    @PreAuthorize("@baseShiftService.hasPermissionToWrite(#shiftId) or hasRole('SUPERUSER')")
    @GetMapping("{shiftId}")
    public String show(
            @P("shiftId") @PathVariable int shiftId,
            Model model
    ) {

        model.addAttribute(shiftId);

        return HTML_FOLDER + "show";
    }

    @PreAuthorize("@baseActivityService.hasPermissionToWrite(#activityId) or hasRole('SUPERUSER')")
    @GetMapping("add")
    public String add(
            @P("activityId") @PathVariable int activityId,
            Model model
    ) {

        model.addAttribute(new ShiftForm());

        return HTML_FOLDER + "add";
    }

    @PreAuthorize("@baseActivityService.hasPermissionToWrite(#activityId) or hasRole('SUPERUSER')")
    @PostMapping("add")
    public String add(
            @P("activityId") @PathVariable int activityId,
            @Valid ShiftForm shiftForm,
            BindingResult bindingResult
    ) {

        shiftForm.setActivityId(activityId);
        if (bindingResult.hasErrors()) {
            return HTML_FOLDER + "add";
        }

        shiftService.save(shiftForm);

        return "redirect:";
    }

    @PreAuthorize("@baseShiftService.hasPermissionToWrite(#shiftId) or hasRole('SUPERUSER')")
    @GetMapping("{shiftId}/edit")
    public String edit(
            @P("shiftId") @PathVariable int shiftId,
            Model model
    ) {

        ShiftForm shiftForm = new ShiftForm(shiftService.get(shiftId));
        model.addAttribute(shiftForm);

        return HTML_FOLDER + "edit";
    }

    @PreAuthorize("@baseShiftService.hasPermissionToWrite(#shiftId) or hasRole('SUPERUSER')")
    @PostMapping("{shiftId}/edit")
    public String edit(
            @P("shiftId") @PathVariable int shiftId,
            @Valid ShiftForm shiftForm,
            BindingResult bindingResult
    ) {

        shiftForm.setId(shiftId);
        if (bindingResult.hasErrors()) {
            return HTML_FOLDER + "edit";
        }

        shiftService.save(shiftForm);

        return "redirect:..";
    }

    @PreAuthorize("@baseShiftService.hasPermissionToWrite(#shiftId) or hasRole('SUPERUSER')")
    @GetMapping("{shiftId}/delete")
    public String delete(
            @P("shiftId") @PathVariable int shiftId
    ) {

        shiftService.delete(shiftId);

        return "redirect:..";
    }
}
