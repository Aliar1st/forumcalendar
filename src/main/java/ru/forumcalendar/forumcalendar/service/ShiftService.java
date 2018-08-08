package ru.forumcalendar.forumcalendar.service;

import ru.forumcalendar.forumcalendar.model.ShiftModel;

import java.util.List;

public interface ShiftService {

    List<ShiftModel> getShiftModelsByActivityId(Integer id);
}
