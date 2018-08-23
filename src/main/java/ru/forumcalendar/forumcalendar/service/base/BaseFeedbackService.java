package ru.forumcalendar.forumcalendar.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.forumcalendar.forumcalendar.domain.Feedback;
import ru.forumcalendar.forumcalendar.exception.EntityNotFoundException;
import ru.forumcalendar.forumcalendar.model.form.FeedbackForm;
import ru.forumcalendar.forumcalendar.repository.FeedbackRepository;
import ru.forumcalendar.forumcalendar.service.FeedbackService;
import ru.forumcalendar.forumcalendar.service.UserService;

@Service
@Transactional
public class BaseFeedbackService implements FeedbackService {

    private final UserService userService;
    private final FeedbackRepository feedbackRepository;

    @Autowired
    public BaseFeedbackService(
            UserService userService,
            FeedbackRepository feedbackRepository
    ) {
        this.userService = userService;
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public Feedback save(FeedbackForm feedbackForm) {

        Feedback feedback = feedbackRepository.findById(feedbackForm.getId()).orElse(new Feedback());
        feedback.setTitle(feedbackForm.getTitle());
        feedback.setTheme(feedbackForm.getTheme());
        feedback.setFeedback(feedbackForm.getFeedback());
        feedback.setUser(userService.getCurrentUser());

        return feedbackRepository.save(feedback);
    }
}
