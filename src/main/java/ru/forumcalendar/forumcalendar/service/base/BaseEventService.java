package ru.forumcalendar.forumcalendar.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.forumcalendar.forumcalendar.domain.Activity;
import ru.forumcalendar.forumcalendar.domain.Event;
import ru.forumcalendar.forumcalendar.domain.Speaker;
import ru.forumcalendar.forumcalendar.exception.EntityNotFoundException;
import ru.forumcalendar.forumcalendar.model.EventModel;
import ru.forumcalendar.forumcalendar.model.ShiftEventModel;
import ru.forumcalendar.forumcalendar.model.form.EventForm;
import ru.forumcalendar.forumcalendar.model.form.SpeakerForm;
import ru.forumcalendar.forumcalendar.repository.EventRepository;
import ru.forumcalendar.forumcalendar.repository.SpeakerRepository;
import ru.forumcalendar.forumcalendar.service.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BaseEventService implements EventService {

    private final SpeakerRepository speakerRepository;
    private final EventRepository eventRepository;

    private final SubscriptionService subscriptionService;
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
            @Lazy SubscriptionService subscriptionService,
            ActivityService activityService,
            ShiftService shiftService,
            UserService userService) {
        this.speakerService = speakerService;
        this.conversionService = conversionService;
        this.speakerRepository = speakerRepository;
        this.eventRepository = eventRepository;
        this.subscriptionService = subscriptionService;
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
    public List<ShiftEventModel> getAll() {
        return eventRepository.findAll()
                .stream()
                .map((e) -> conversionService.convert(e, ShiftEventModel.class))
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
        if (eventForm.getShiftId() != null)
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
        } else {
            event.getSpeakers().clear();
        }

        if (eventForm.getShiftsId() != null && !eventForm.getShiftsId().isEmpty()) {
            List<Event> events = new ArrayList<>(eventForm.getShiftsId().size());
            for (int shiftId : eventForm.getShiftsId()) {
                Event e = event.clone();
                e.setShift(shiftService.get(shiftId));
                events.add(e);
            }

            eventRepository.saveAll(events);
            event = events.get(0);
        } else if (eventForm.getShiftId() != null) {
            event = eventRepository.save(event);
        }

        return event;
    }

    @Override
    public Event delete(int id) {
        Event event = get(id);
        eventRepository.deleteById(id);
        return event;
    }

    @Override
    public List<ShiftEventModel> getEventModelsByShiftIdAndDate(int shiftId, LocalDate date) {

        LocalDateTime startDate = date.atStartOfDay();
        LocalDateTime endDate = startDate.plusDays(1);

        return eventRepository.getAllByShiftIdAndStartDatetimeBetween(shiftId, startDate, endDate)
                .map((a) -> conversionService.convert(a, ShiftEventModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftEventModel> getEventModelsByShiftId(int shiftId) {
        return eventRepository.getAllByShiftIdOrderByStartDatetime(shiftId)
                .map((a) -> conversionService.convert(a, ShiftEventModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftEventModel> getEventModelsByActivityId(int activityId) {
        return eventRepository.getAllByShiftActivityIdOrderByStartDatetime(activityId)
                .map((a) -> conversionService.convert(a, ShiftEventModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftEventModel> getEventsBySpeakerId(int id) {

        return null;
    }

    @Override
    public boolean hasPermissionToWrite(int id) {
        return shiftService.hasPermissionToWrite(get(id).getShift().getId());
    }

    @Override
    public boolean hasPermissionToRead(int id) {
        return true;
    }
}
