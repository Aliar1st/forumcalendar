package ru.forumcalendar.forumcalendar.model;

import lombok.Getter;
import lombok.Setter;
import ru.forumcalendar.forumcalendar.domain.Shift;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ShiftEventModel extends EventModel {

    private boolean favorite;

    private int likes;

    private int dislikes;

    private List<SpeakerModel> speakers;

    {
        super.setShiftEvent(true);
    }
}
