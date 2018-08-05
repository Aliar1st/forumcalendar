package ru.forumcalendar.forumcalendar.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class User {
    private int id;
    private String forumName;
    private String shiftName;
    private String directionName;
    private String teamName;
    private String photoPath;
    private String firstName;
    private String secondName;
    private List<String> contacts;
}
