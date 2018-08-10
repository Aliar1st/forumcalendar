package ru.forumcalendar.forumcalendar.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.forumcalendar.forumcalendar.domain.Activity;
import ru.forumcalendar.forumcalendar.domain.Event;
import ru.forumcalendar.forumcalendar.domain.Speaker;
import ru.forumcalendar.forumcalendar.exception.EntityNotFoundException;
import ru.forumcalendar.forumcalendar.model.EventModel;
import ru.forumcalendar.forumcalendar.model.form.EventForm;
import ru.forumcalendar.forumcalendar.model.form.SpeakerForm;
import ru.forumcalendar.forumcalendar.repository.ActivityRepository;
import ru.forumcalendar.forumcalendar.repository.EventRepository;
import ru.forumcalendar.forumcalendar.repository.ShiftRepository;
import ru.forumcalendar.forumcalendar.repository.SpeakerRepository;
import ru.forumcalendar.forumcalendar.service.EventService;
import ru.forumcalendar.forumcalendar.service.SpeakerService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BaseEventService implements EventService {

    private final SpeakerService speakerService;
    private final ShiftRepository shiftRepository;
    private final ConversionService conversionService;
    private final ActivityRepository activityRepository;
    private final SpeakerRepository speakerRepository;
    private final EventRepository eventRepository;

    @Autowired
    public BaseEventService(
            SpeakerService speakerService,
            ShiftRepository shiftRepository,
            @Qualifier("mvcConversionService") ConversionService conversionService,
            ActivityRepository activityRepository,
            SpeakerRepository speakerRepository,
            EventRepository eventRepository) {
        this.speakerService = speakerService;
        this.shiftRepository = shiftRepository;
        this.conversionService = conversionService;
        this.activityRepository = activityRepository;
        this.speakerRepository = speakerRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public Event get(int id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event with id " + id + " not found"));
    }

    @Override
    public Event save(EventForm eventForm) {
        Event event = eventRepository.findById(eventForm.getId()).orElse(new Event());
        event.setDatetime(eventForm.getDatetime());
        event.setDescription(eventForm.getDescription());
        event.setId(eventForm.getId());
        event.setName(eventForm.getName());
        event.setPlace(eventForm.getPlace());
        event.setShift(shiftRepository.findById(eventForm.getShiftId())
                .orElseThrow(() -> new EntityNotFoundException("Shift with id " + eventForm.getShiftId() + " not found")));

        List<SpeakerForm> speakerForms = speakerService.getSpeakerFormsBySpeakersId(eventForm.getSpeakersId());
        if (speakerForms != null) {
            List<Speaker> speakers = new ArrayList<>(speakerForms.size());
            for (SpeakerForm sf : speakerForms) {
                Speaker speaker = speakerRepository.findById(sf.getId()).orElse(new Speaker());
                Activity activity = activityRepository.findById(sf.getActivityId())
                        .orElseThrow(() -> new EntityNotFoundException("Activity with id " + sf.getActivityId() + " not found"));

                speaker.setActivity(activity);
                speaker.setDescription(sf.getDescription());
                speaker.setFirstName(sf.getFirstName());
                speaker.setLastName(sf.getLastName());
                speaker.setLink(sf.getLink());
                speakers.add(speaker);
            }
            event.setSpeakers(new HashSet<>(speakers));
            speakerRepository.saveAll(speakers);
        }

        return eventRepository.save(event);
    }

    @Override
    public void delete(int id) {
        eventRepository.deleteById(id);
    }

    @Override
    public List<EventModel> getEventModelsByShiftId(int shiftId) {
        return eventRepository.getAllByShiftId(shiftId)
                .stream()
                .map((a) -> conversionService.convert(a, EventModel.class))
                .collect(Collectors.toList());
    }
}
