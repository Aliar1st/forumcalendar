package ru.forumcalendar.forumcalendar.service;

import ru.forumcalendar.forumcalendar.domain.Speaker;
import ru.forumcalendar.forumcalendar.model.EventModel;
import ru.forumcalendar.forumcalendar.model.SpeakerModel;
import ru.forumcalendar.forumcalendar.model.form.SpeakerForm;

import java.util.List;

public interface SpeakerService extends SecuredService, ResourceService<Speaker, SpeakerModel, SpeakerForm> {

    List<SpeakerForm> getSpeakerFormsBySpeakersId(Integer... speakersId);

    List<SpeakerModel> getSpeakerModelsByActivityId(int id);
}
