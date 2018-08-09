package ru.forumcalendar.forumcalendar.controller.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.forumcalendar.forumcalendar.model.SpeakerModel;
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

    @GetMapping("")
    public String index(
            @PathVariable int activityId,
            Model model
    ) {

        model.addAttribute("shiftModels", shiftService.getShiftModelsByActivityId(activityId));

        return HTML_FOLDER + "index";
    }

    @GetMapping("add")
    public String add(
            @PathVariable int activityId,
            Model model
    ) {

        model.addAttribute(new ShiftForm());

        return HTML_FOLDER + "add";
    }

    @PostMapping("add")
    public String add(
            @PathVariable int activityId,
            @Valid ShiftForm shiftForm,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return HTML_FOLDER + "add";
        }

        shiftForm.setActivityId(activityId);
        shiftService.save(shiftForm);

        return "redirect:";
    }

    @GetMapping("{shiftId}/edit")
    public String edit(
            @PathVariable int shiftId,
            Model model
    ) {

        ShiftForm shiftForm = new ShiftForm(shiftService.get(shiftId));
        model.addAttribute(shiftForm);

        return HTML_FOLDER + "edit";
    }

    @PostMapping("{shiftId}/edit")
    public String edit(
            @PathVariable int shiftId,
            @Valid ShiftForm shiftForm,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return HTML_FOLDER + "edit";
        }

        shiftForm.setId(shiftId);
        shiftService.save(shiftForm);

        return "redirect:..";
    }

    @GetMapping("{shiftId}/delete")
    public String delete(
            @PathVariable int shiftId
    ) {

        shiftService.delete(shiftId);

        return "redirect:..";
    }
}
