package ru.forumcalendar.forumcalendar.converter;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import ru.forumcalendar.forumcalendar.domain.Event;
import ru.forumcalendar.forumcalendar.domain.Speaker;
import ru.forumcalendar.forumcalendar.model.EventModel;
import ru.forumcalendar.forumcalendar.model.InnerEventModel;
import ru.forumcalendar.forumcalendar.model.SpeakerModel;

import java.util.List;
import java.util.stream.Collectors;

public class SpeakerModelConverter implements Converter<Speaker, SpeakerModel> {

    private InnerEventModelConverter innerEventModelConverter;

    @Override
    public SpeakerModel convert(Speaker speaker) {

        SpeakerModel speakerModel = new SpeakerModel();

        speakerModel.setPhoto(""); //FIXME добавить фото спикеру
        speakerModel.setId(speaker.getId());
        speakerModel.setActivityId(speaker.getActivity().getId());
        speakerModel.setFirstName(speaker.getFirstName());
        speakerModel.setLastName(speaker.getLastName());
        speakerModel.setLink(speaker.getLink());
        speakerModel.setDescription(speaker.getDescription());

        List<InnerEventModel> eventModels = speaker.getEvents()
                .stream()
                .map((s) -> innerEventModelConverter.convert(s))
                .collect(Collectors.toList());

        speakerModel.setEvents(eventModels);

        return speakerModel;
    }

    @Autowired
    public void setInnerEventModelConverter(@Lazy InnerEventModelConverter innerEventModelConverter) {
        this.innerEventModelConverter = innerEventModelConverter;
    }
}
