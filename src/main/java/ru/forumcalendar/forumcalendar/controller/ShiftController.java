package ru.forumcalendar.forumcalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.forumcalendar.forumcalendar.domain.Shift;
import ru.forumcalendar.forumcalendar.model.ShiftModel;
import ru.forumcalendar.forumcalendar.model.form.ShiftForm;
import ru.forumcalendar.forumcalendar.service.ShiftService;

import javax.validation.Valid;

@Controller
@RequestMapping("shifts")
public class ShiftController {
    private static final String HTML_FOLDER = "shift/";
    private static final String REDIRECT_ROOT_MAPPING = "redirect:/shift?activityId=";

    private final ShiftService shiftService;
    private final ConversionService conversionService;

    @Autowired
    public ShiftController(
            ShiftService shiftService,
            @Qualifier("mvcConversionService") ConversionService conversionService
    ) {
        this.shiftService = shiftService;
        this.conversionService = conversionService;
    }

    @GetMapping("")
    public String index(
            @RequestParam int activityId,
            Model model
    ) {
        model.addAttribute("shifts", shiftService.getShiftModelsByActivityId(activityId));
        model.addAttribute("activityId", activityId);

        return HTML_FOLDER + "index";
    }

    @GetMapping("{id}")
    public String show(
            @PathVariable int id,
            Model model
    ) {
        model.addAttribute("shift", conversionService.convert(shiftService.get(id), ShiftModel.class));
        return HTML_FOLDER + "show";
    }

    @GetMapping("add")
    public String add(
            @RequestParam int activityId,
            Model model
    ) {
        ShiftForm shiftForm = new ShiftForm();
        shiftForm.setActivityId(activityId);

        model.addAttribute(shiftForm);

        return HTML_FOLDER + "add";
    }

    @PostMapping("add")
    public String add(
            @Valid ShiftForm shiftForm,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return HTML_FOLDER + "add";
        }

        shiftService.save(shiftForm);

        return REDIRECT_ROOT_MAPPING + shiftForm.getActivityId();
    }

    @GetMapping("{id}/edit")
    public String edit(
            @PathVariable int id,
            Model model
    ) {
        ShiftForm shiftForm = new ShiftForm(shiftService.get(id));
        model.addAttribute(shiftForm);

        return HTML_FOLDER + "edit";
    }

    @PostMapping("{id}/edit")
    public String edit(
            @PathVariable int id,
            @Valid ShiftForm shiftForm,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return HTML_FOLDER + "edit";
        }

        shiftForm.setId(id);
        shiftService.save(shiftForm);

        return REDIRECT_ROOT_MAPPING + shiftForm.getActivityId();
    }

    @GetMapping("{id}/delete")
    public String delete(
            @PathVariable int id
    ) {
        Shift shift = shiftService.delete(id);

        return REDIRECT_ROOT_MAPPING + shift.getActivity().getId();
    }
}
