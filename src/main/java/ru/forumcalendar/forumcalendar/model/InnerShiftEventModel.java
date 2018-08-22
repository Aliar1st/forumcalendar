package ru.forumcalendar.forumcalendar.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
public class InnerShiftEventModel extends EventModel {

    private boolean favorite;

    private int likes;

    private int dislikes;

    {
        super.setShiftEvent(true);
    }
}
