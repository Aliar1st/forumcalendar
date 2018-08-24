package ru.forumcalendar.forumcalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.forumcalendar.forumcalendar.model.form.FeedbackForm;
import ru.forumcalendar.forumcalendar.service.FeedbackService;

import javax.validation.Valid;

@Controller
@RequestMapping("/feedback")
public class FeedbackController {

    private static final String HTML_FOLDER = "feedback/";
    public static final String REDIRECT_TO_MENU = "redirect:/menu";

    private final FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping("")
    public String add(Model model) {
        model.addAttribute(new FeedbackForm());
        return HTML_FOLDER + "add";
    }

    @PostMapping("")
    public String add(
            @Valid FeedbackForm feedbackForm,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return HTML_FOLDER + "add";
        }

        feedbackService.save(feedbackForm);

        return REDIRECT_TO_MENU;
    }
}
