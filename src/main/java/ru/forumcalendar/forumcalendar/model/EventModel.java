package ru.forumcalendar.forumcalendar.model;

import lombok.Getter;
import lombok.Setter;
import ru.forumcalendar.forumcalendar.domain.Shift;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class EventModel {

    private int id;

    private int day;

    private String name;

    private LocalDateTime startDatetime;

    private LocalDateTime endDatetime;

    private String place;

    private String description;

//    private Shift shift;

    private boolean favorite;

    private int likes;

    private int dislikes;

    private List<SpeakerModel> speakers;
}
