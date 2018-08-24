package ru.forumcalendar.forumcalendar.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import ru.forumcalendar.forumcalendar.domain.Speaker;
import ru.forumcalendar.forumcalendar.model.InnerShiftEventModel;
import ru.forumcalendar.forumcalendar.model.ShiftEventModel;
import ru.forumcalendar.forumcalendar.model.SpeakerModel;

import java.util.List;
import java.util.stream.Collectors;

public class SpeakerModelConverter implements Converter<Speaker, SpeakerModel> {

    private ShiftEventModelConverter shiftEventModelConverter;

    @Override
    public SpeakerModel convert(Speaker speaker) {

        SpeakerModel speakerModel = new SpeakerModel();

        speakerModel.setPhoto(speaker.getPhoto());
        speakerModel.setId(speaker.getId());
        speakerModel.setActivityId(speaker.getActivity().getId());
        speakerModel.setFirstName(speaker.getFirstName());
        speakerModel.setLastName(speaker.getLastName());
        speakerModel.setLink(speaker.getLink());
        speakerModel.setDescription(speaker.getDescription());

        List<ShiftEventModel> eventModels = speaker.getEvents()
                .stream()
                .map((s) -> shiftEventModelConverter.convert(s))
                .collect(Collectors.toList());

        speakerModel.setEvents(eventModels);

        return speakerModel;
    }

    @Autowired
    public void setInnerEventModelConverter(@Lazy ShiftEventModelConverter shiftEventModelConverter) {
        this.shiftEventModelConverter = shiftEventModelConverter;
    }
}
