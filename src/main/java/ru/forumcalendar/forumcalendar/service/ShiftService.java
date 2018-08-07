package ru.forumcalendar.forumcalendar.service;

import ru.forumcalendar.forumcalendar.domain.Activity;
import ru.forumcalendar.forumcalendar.domain.Shift;
import ru.forumcalendar.forumcalendar.model.ShiftModel;
import ru.forumcalendar.forumcalendar.model.form.ShiftForm;

import java.util.List;

public interface ShiftService {

    List<ShiftModel> getShiftModelsByActivityId(Integer id);
}
