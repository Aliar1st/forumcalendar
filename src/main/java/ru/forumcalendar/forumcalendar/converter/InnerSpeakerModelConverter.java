package ru.forumcalendar.forumcalendar.converter;

import org.springframework.core.convert.converter.Converter;
import ru.forumcalendar.forumcalendar.domain.Speaker;
import ru.forumcalendar.forumcalendar.model.InnerSpeakerModel;

public class InnerSpeakerModelConverter implements Converter<Speaker, InnerSpeakerModel> {

    @Override
    public InnerSpeakerModel convert(Speaker speaker) {
        InnerSpeakerModel innerSpeakerModel = new InnerSpeakerModel();

        innerSpeakerModel.setId(speaker.getId());
        innerSpeakerModel.setActivityId(speaker.getActivity().getId());
        innerSpeakerModel.setFirstName(speaker.getFirstName());
        innerSpeakerModel.setLastName(speaker.getLastName());
        innerSpeakerModel.setPhoto(""); //FIXME добавить фото спикеру
        innerSpeakerModel.setLink(speaker.getLink());
        innerSpeakerModel.setDescription(speaker.getDescription());

        return innerSpeakerModel;
    }
}
