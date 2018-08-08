package ru.forumcalendar.forumcalendar.service;

import ru.forumcalendar.forumcalendar.domain.Speaker;
import ru.forumcalendar.forumcalendar.model.SpeakerModel;
import ru.forumcalendar.forumcalendar.model.form.SpeakerForm;

import java.util.List;

public interface SpeakerService {

    Speaker get(int id);

    Speaker save(SpeakerForm speakerForm);

    void delete(int id);

    List<SpeakerModel> getSpeakerModelsByActivityId(int id);
}
