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
import ru.forumcalendar.forumcalendar.repository.SpeakerRepository;
import ru.forumcalendar.forumcalendar.service.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BaseEventService implements EventService {

    private final SpeakerRepository speakerRepository;
    private final EventRepository eventRepository;

    private final ActivityService activityService;
    private final SpeakerService speakerService;
    private final ShiftService shiftService;
    private final UserService userService;
    private final ConversionService conversionService;

    @Autowired
    public BaseEventService(
            SpeakerService speakerService,
            @Qualifier("mvcConversionService") ConversionService conversionService,
            SpeakerRepository speakerRepository,
            EventRepository eventRepository,
            ActivityService activityService,
            ShiftService shiftService,
            UserService userService) {
        this.speakerService = speakerService;
        this.conversionService = conversionService;
        this.speakerRepository = speakerRepository;
        this.eventRepository = eventRepository;
        this.activityService = activityService;
        this.shiftService = shiftService;
        this.userService = userService;
    }

    @Override
    public boolean exist(int id) {
        return eventRepository.findById(id).isPresent();
    }

    @Override
    public Event get(int id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event with id " + id + " not found"));
    }

    @Override
    public List<EventModel> getAll() {
        return eventRepository.findAll()
                .stream()
                .map((e) -> conversionService.convert(e, EventModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public Event save(EventForm eventForm) {
        Event event = eventRepository.findById(eventForm.getId()).orElse(new Event());
        event.setStartDatetime(eventForm.getStartDatetime());
        event.setEndDatetime(eventForm.getEndDatetime());
        event.setDescription(eventForm.getDescription());
        event.setId(eventForm.getId());
        event.setName(eventForm.getName());
        event.setPlace(eventForm.getPlace());
        event.setShift(shiftService.get(eventForm.getShiftId()));

        List<SpeakerForm> speakerForms = speakerService.getSpeakerFormsBySpeakersId(eventForm.getSpeakersId());
        if (speakerForms != null) {
            List<Speaker> speakers = new ArrayList<>(speakerForms.size());
            for (SpeakerForm sf : speakerForms) {
                Speaker speaker = speakerRepository.findById(sf.getId()).orElse(new Speaker());
                Activity activity = activityService.get(sf.getActivityId());

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
    public Event delete(int id) {
        Event event = get(id);
        eventRepository.deleteById(id);
        return event;
    }

    @Override
    public List<EventModel> getEventModelsByShiftId(int shiftId) {
        return eventRepository.getAllByShiftId(shiftId)
                .stream()
                .map((a) -> conversionService.convert(a, EventModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasPermissionToWrite(int id) {
        return get(id).getShift().getActivity().getUser().getId().equals(userService.getCurrentId());
    }

    @Override
    public boolean hasPermissionToRead(int id) {
        return true;
    }
}
