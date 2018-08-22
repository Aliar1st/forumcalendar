package ru.forumcalendar.forumcalendar.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ShiftEventModel extends InnerShiftEventModel {

    private List<InnerSpeakerModel> speakers;

}