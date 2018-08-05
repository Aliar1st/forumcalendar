package ru.forumcalendar.forumcalendar.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Team {
    private String name;
    private User curator;
    private User captain;
    private List<User> members;
    private List<TeamEvent> events;
}
