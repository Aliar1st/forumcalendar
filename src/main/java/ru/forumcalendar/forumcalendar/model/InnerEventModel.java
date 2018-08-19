package ru.forumcalendar.forumcalendar.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
public class InnerEventModel {

    private int id;

    private int day;

    private String name;

    private LocalDateTime startDatetime;

    private LocalDateTime endDatetime;

    private String place;

    private String description;

    private boolean shiftEvent;

    private boolean favorite;

    private int likes;

    private int dislikes;

}
