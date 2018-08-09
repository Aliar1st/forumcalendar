package ru.forumcalendar.forumcalendar.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.forumcalendar.forumcalendar.domain.Activity;
import ru.forumcalendar.forumcalendar.domain.Event;
import ru.forumcalendar.forumcalendar.domain.Shift;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BaseEventService implements EventService {

    private final ShiftRepository shiftRepository;
    private final ConversionService conversionService;
    private final ActivityRepository activityRepository;
    private final SpeakerRepository speakerRepository;
    private final EventRepository eventRepository;

    @Autowired
    public BaseEventService(
            ShiftRepository shiftRepository,
            @Qualifier("mvcConversionService") ConversionService conversionService,
            ActivityRepository activityRepository,
            SpeakerRepository speakerRepository,
            EventRepository eventRepository) {
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
        event.setDate(eventForm.getDate());
        event.setDescription(eventForm.getDescription());
        event.setId(eventForm.getId());
        event.setName(eventForm.getName());
        event.setPlace(eventForm.getPlace());
        event.setShift(shiftRepository.findById(eventForm.getShiftId()).orElse(new Shift()));
        event.setTime(eventForm.getTime());

        List<SpeakerForm> speakerForms = eventForm.getSpeakerForms();
        if (speakerForms != null) {
            List<Speaker> speakers = new ArrayList<>(eventForm.getSpeakerForms().size());
            for (SpeakerForm sf : eventForm.getSpeakerForms()) {
                Speaker speaker = speakerRepository.findById(sf.getId()).orElse(new Speaker());
                Activity activity = activityRepository.findById(sf.getActivityId()).orElse(new Activity());

                speaker.setActivity(activity);
                speaker.setDescription(sf.getDescription());
                speaker.setFirstName(sf.getFirstName());
                speaker.setLastName(sf.getLastName());
                speaker.setLink(sf.getLink());
                speakers.add(speaker);
            }
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
