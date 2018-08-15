package ru.forumcalendar.forumcalendar.service;

import ru.forumcalendar.forumcalendar.domain.Speaker;
import ru.forumcalendar.forumcalendar.model.SpeakerModel;
import ru.forumcalendar.forumcalendar.model.form.SpeakerForm;

import java.util.List;

public interface SpeakerService extends SecuredService {

    boolean exist(int id);

    Speaker get(int id);

    Speaker save(SpeakerForm speakerForm);

    void delete(int id);

    boolean hasPermissionToWrite(int id);

    List<SpeakerForm> getSpeakerFormsBySpeakersId(Integer... speakersId);

    List<SpeakerModel> getSpeakerModelsByActivityId(int id);
}
