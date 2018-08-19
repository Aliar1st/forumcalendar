package ru.forumcalendar.forumcalendar.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class InnerSpeakerModel {

    private int id = -1;

    private int activityId;

    private String photo;

    private String firstName;

    private String lastName;

    private String link;

    private String description;
}
