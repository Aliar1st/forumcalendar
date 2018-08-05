package ru.forumcalendar.forumcalendar.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
public class ForumEvent extends Event {
    private String forumName;
    private List<Speaker> speakers;
}
