package ru.forumcalendar.forumcalendar.service;

import ru.forumcalendar.forumcalendar.domain.Shift;
import ru.forumcalendar.forumcalendar.model.ShiftModel;
import ru.forumcalendar.forumcalendar.model.form.ShiftForm;

import java.util.List;

public interface ShiftService {

    boolean exist(int id);

    Shift get(int id);

    Shift save(ShiftForm shiftForm);

    void delete(int id);

    List<ShiftModel> getShiftModelsByActivityId(int id);
}
