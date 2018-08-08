package ru.forumcalendar.forumcalendar.converter;

import org.springframework.core.convert.converter.Converter;
import ru.forumcalendar.forumcalendar.domain.Speaker;
import ru.forumcalendar.forumcalendar.model.SpeakerModel;

public class SpeakerModelConverter implements Converter<Speaker, SpeakerModel> {

    @Override
    public SpeakerModel convert(Speaker speaker) {

        SpeakerModel speakerModel = new SpeakerModel();

        speakerModel.setId(speaker.getId());
        speakerModel.setActivityId(speaker.getActivity().getId());
        speakerModel.setFirstName(speaker.getFirstName());
        speakerModel.setLastName(speaker.getLastName());
        speakerModel.setLink(speaker.getLink());
        speakerModel.setDescription(speaker.getDescription());

        return speakerModel;
    }
}
