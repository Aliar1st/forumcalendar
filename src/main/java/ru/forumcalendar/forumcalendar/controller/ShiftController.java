package ru.forumcalendar.forumcalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("activity/{activityId}/shift")
public class ShiftController {

    private ShiftService shiftService;

    @Autowired
    public ShiftController(
            ShiftService shiftService
    ) {
        this.shiftService = shiftService;
    }

    @GetMapping("")
    public String index(
            @PathVariable Integer activityId,
            Model model
    ) {

        model.addAttribute("shiftModels", shiftService.getShiftModelsByActivityId(activityId));
        model.addAttribute(activityId);

        return "shift/index";
    }

    @GetMapping("/add")
    public String add(
            @PathVariable int activityId,
            Model model
    ) {

        model.addAttribute(new ShiftForm());
        model.addAttribute(activityId);

        return "shift/add";
    }

    @PostMapping("/add")
    public String add(
            @PathVariable int activityId,
            @Valid ShiftForm shiftForm,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return "shift/add";
        }

        shiftForm.setActivityId(activityId);
        shiftService.save(shiftForm);

        return "redirect:/activity/" + activityId + "/shift";
    }

    @GetMapping("/{shiftId}/edit")
    public String edit(
            @PathVariable int shiftId,
            Model model
    ) {

        ShiftForm shiftForm = new ShiftForm(shiftService.get(shiftId));
        model.addAttribute(shiftForm);

        return "shift/edit";
    }

    @PostMapping("/{shiftId}/edit")
    public String edit(
            @PathVariable int activityId,
            @PathVariable int shiftId,
            @Valid ShiftForm shiftForm,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return "shift/edit";
        }

        shiftForm.setId(shiftId);
        shiftService.save(shiftForm);

        return "redirect:/activity/" + activityId + "/shift";
    }

    @GetMapping("/{shiftId}/delete")
    public String delete(
            @PathVariable int activityId,
            @PathVariable int shiftId
    ) {

        shiftService.delete(shiftId);

        return "redirect:/activity/" + activityId + "/shift";
    }
}
