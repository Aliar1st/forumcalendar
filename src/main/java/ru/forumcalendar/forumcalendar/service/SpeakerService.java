package ru.forumcalendar.forumcalendar.service;

import ru.forumcalendar.forumcalendar.domain.Speaker;
import ru.forumcalendar.forumcalendar.model.SpeakerModel;
import ru.forumcalendar.forumcalendar.model.form.SpeakerForm;

import java.util.List;

public interface SpeakerService extends SecuredService, ResourceService<Speaker, SpeakerModel, SpeakerForm> {

    List<SpeakerModel> searchByName(String q, int activityId) throws InterruptedException;

    List<SpeakerForm> getSpeakerFormsBySpeakersId(List<Integer> speakersId);

    List<SpeakerModel> getSpeakerModelsByActivityId(int id);

    List<SpeakerModel> getSpeakerModelsByShiftId(int id);
}
