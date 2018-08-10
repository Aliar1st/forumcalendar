package ru.forumcalendar.forumcalendar.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.forumcalendar.forumcalendar.domain.Speaker;
import ru.forumcalendar.forumcalendar.exception.EntityNotFoundException;
import ru.forumcalendar.forumcalendar.model.SpeakerModel;
import ru.forumcalendar.forumcalendar.model.form.SpeakerForm;
import ru.forumcalendar.forumcalendar.repository.ActivityRepository;
import ru.forumcalendar.forumcalendar.repository.SpeakerRepository;
import ru.forumcalendar.forumcalendar.service.SpeakerService;
import ru.forumcalendar.forumcalendar.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BaseSpeakerService implements SpeakerService {

    private final ActivityRepository activityRepository;
    private final SpeakerRepository speakerRepository;

    private final UserService userService;
    private final ConversionService conversionService;

    @Autowired
    public BaseSpeakerService(
            ActivityRepository activityRepository,
            SpeakerRepository speakerRepository,
            UserService userService,
            @Qualifier("mvcConversionService") ConversionService conversionService
    ) {
        this.activityRepository = activityRepository;
        this.speakerRepository = speakerRepository;
        this.userService = userService;
        this.conversionService = conversionService;
    }

    @Override
    public Speaker get(int id) {
        return speakerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Speaker with id " + id + " not found"));
    }

    @Override
    public Speaker save(SpeakerForm speakerForm) {

        Speaker speaker = speakerRepository.findById(speakerForm.getId()).orElse(new Speaker());
        speaker.setFirstName(speakerForm.getFirstName());
        speaker.setLastName(speakerForm.getLastName());
        speaker.setLink(speakerForm.getLink());
        speaker.setDescription(speakerForm.getDescription());
        speaker.setActivity(activityRepository.findById(speakerForm.getActivityId())
                .orElseThrow(() -> new EntityNotFoundException("Activity with id " + speakerForm.getActivityId() + " not found")));

        return speakerRepository.save(speaker);
    }

    @Override
    public void delete(int id) {
        speakerRepository.deleteById(id);
    }

    @Override
    public List<SpeakerModel> getSpeakerModelsByActivityId(int id) {
        return speakerRepository.getAllByActivityIdOrderByCreatedAt(id)
                .stream()
                .map((s) -> conversionService.convert(s, SpeakerModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<SpeakerForm> getSpeakerFormsBySpeakersId(int... speakersId) {
        List<SpeakerForm> speakerForms = new ArrayList<>();

        for (int speakerId : speakersId) {
            speakerForms.add(new SpeakerForm(speakerRepository.findById(speakerId).
                    orElseThrow(() -> new IllegalArgumentException(
                            "Can't find team with id '" + speakerId + "'")
                    )));
        }

        return speakerForms;
    }

    @Override
    public boolean isUserSpeaker(int id) {
        return get(id).getActivity().getUser().getId().equals(userService.getCurrentId());
    }
}
