package ru.forumcalendar.forumcalendar.model;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public abstract class Event {
    String name;
    Date date;
    Time time;
    String description;
    String place;
}
