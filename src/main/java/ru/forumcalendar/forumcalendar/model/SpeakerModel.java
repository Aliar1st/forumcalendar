package ru.forumcalendar.forumcalendar.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpeakerModel {

    private int id = -1;

    private int activityId;

    private String firstName;

    private String lastName;

    private String link;

    private String description;
}
