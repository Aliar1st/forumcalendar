package ru.forumcalendar.forumcalendar.converter;

import org.springframework.core.convert.converter.Converter;
import ru.forumcalendar.forumcalendar.domain.Event;
import ru.forumcalendar.forumcalendar.model.EventModel;

public class EventModelConverter implements Converter<Event, EventModel> {

    @Override
    public EventModel convert(Event event) {

        EventModel eventModel = new EventModel();

        eventModel.setDatetime(event.getDatetime());
        eventModel.setDescription(event.getDescription());
        eventModel.setId(event.getId());
        eventModel.setName(event.getName());
        eventModel.setPlace(event.getPlace());
        eventModel.setShift(event.getShift());
        eventModel.setSpeakersCount(event.getSpeakers().size());

        return eventModel;

    }
}
