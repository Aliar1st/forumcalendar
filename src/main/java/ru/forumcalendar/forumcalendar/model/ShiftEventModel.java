package ru.forumcalendar.forumcalendar.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ShiftEventModel extends InnerShiftEventModel {

    private boolean subscribed; //для отображения подписен или нет в events

    private List<InnerSpeakerModel> speakers;

}