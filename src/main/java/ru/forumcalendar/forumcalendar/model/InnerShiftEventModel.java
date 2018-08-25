package ru.forumcalendar.forumcalendar.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class InnerShiftEventModel extends EventModel {

    private boolean favorite;

    private boolean like;

    private boolean dislike;

    private int likes;

    private int dislikes;

    {
        super.setShiftEvent(true);
    }
}
