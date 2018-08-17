package ru.forumcalendar.forumcalendar.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.forumcalendar.forumcalendar.domain.Speaker;
import ru.forumcalendar.forumcalendar.exception.EntityNotFoundException;
import ru.forumcalendar.forumcalendar.model.SpeakerModel;
import ru.forumcalendar.forumcalendar.model.form.SpeakerForm;
import ru.forumcalendar.forumcalendar.repository.SpeakerRepository;
import ru.forumcalendar.forumcalendar.service.ActivityService;
import ru.forumcalendar.forumcalendar.service.SpeakerService;
import ru.forumcalendar.forumcalendar.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BaseSpeakerService implements SpeakerService {

    private final SpeakerRepository speakerRepository;

    private final ActivityService activityService;
    private final UserService userService;
    private final ConversionService conversionService;

    @Autowired
    public BaseSpeakerService(
            SpeakerRepository speakerRepository,
            ActivityService activityService,
            UserService userService,
            @Qualifier("mvcConversionService") ConversionService conversionService
    ) {
        this.speakerRepository = speakerRepository;
        this.activityService = activityService;
        this.userService = userService;
        this.conversionService = conversionService;
    }

    @Override
    public boolean exist(int id) {
        return speakerRepository.findById(id).isPresent();
    }

    @Override
    public Speaker get(int id) {
        return speakerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Speaker with id " + id + " not found"));
    }

    @Override
    public List<SpeakerModel> getAll() {
        return speakerRepository.findAll()
                .stream()
                .map((s) -> conversionService.convert(s, SpeakerModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public Speaker save(SpeakerForm speakerForm) {

        Speaker speaker = speakerRepository.findById(speakerForm.getId()).orElse(new Speaker());
        speaker.setFirstName(speakerForm.getFirstName());
        speaker.setLastName(speakerForm.getLastName());
        speaker.setLink(speakerForm.getLink());
        speaker.setDescription(speakerForm.getDescription());
        speaker.setActivity(activityService.get(speakerForm.getActivityId()));

        return speakerRepository.save(speaker);
    }

    @Override
    public Speaker delete(int id) {
        Speaker speaker = get(id);
        speakerRepository.deleteById(id);
        return speaker;
    }

    @Override
    public List<SpeakerModel> getSpeakerModelsByActivityId(int id) {
        return speakerRepository.getAllByActivityId(id)
                .map((s) -> conversionService.convert(s, SpeakerModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<SpeakerForm> getSpeakerFormsBySpeakersId(Integer... speakersId) {
        if (speakersId == null) {
            return null;
        }

        List<SpeakerForm> speakerForms = new ArrayList<>();
        for (int speakerId : speakersId) {
            speakerForms.add(new SpeakerForm(get(speakerId)));
        }

        return speakerForms;
    }

    @Override
    public boolean hasPermissionToWrite(int id) {
        return activityService.hasPermissionToWrite(get(id).getActivity().getId());
    }

    @Override
    public boolean hasPermissionToRead(int id) {
        return true;
    }
}
