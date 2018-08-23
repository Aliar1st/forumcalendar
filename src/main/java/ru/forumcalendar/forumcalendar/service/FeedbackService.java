package ru.forumcalendar.forumcalendar.service;

import ru.forumcalendar.forumcalendar.domain.Feedback;
import ru.forumcalendar.forumcalendar.model.form.FeedbackForm;

public interface FeedbackService {

    Feedback save(FeedbackForm feedbackForm);
}
