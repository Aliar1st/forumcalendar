package ru.forumcalendar.forumcalendar.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SpeakerModel extends InnerSpeakerModel {

    private List<InnerShiftEventModel> events;
}
