package ru.forumcalendar.forumcalendar.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamModel {

    private int id = -1;

    private String userId;

    private int shiftId;

    private String name;

    private String direction;

    private String description;

    private int userCount;
}
