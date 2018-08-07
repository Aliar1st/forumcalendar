package ru.forumcalendar.forumcalendar.model;

import lombok.Getter;
import lombok.Setter;
import ru.forumcalendar.forumcalendar.domain.Activity;
import ru.forumcalendar.forumcalendar.domain.Shift;
import ru.forumcalendar.forumcalendar.model.form.ShiftForm;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ActivityModel {

    private Integer id;

    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer shiftCount;
}
