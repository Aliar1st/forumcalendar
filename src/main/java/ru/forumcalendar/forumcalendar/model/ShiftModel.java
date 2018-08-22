package ru.forumcalendar.forumcalendar.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShiftModel extends ShiftListModel {

    private int userCount;

    private int teamCount;

    private int eventCount;
}
