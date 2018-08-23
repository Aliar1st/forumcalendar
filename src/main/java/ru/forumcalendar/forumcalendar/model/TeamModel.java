package ru.forumcalendar.forumcalendar.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class TeamModel {

    private int id = -1;

    private int shiftId;

    private String name;

    private int number;

//    private String direction;

    private String description;

    private int userCount;
}
