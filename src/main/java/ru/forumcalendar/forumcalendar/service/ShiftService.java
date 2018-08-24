package ru.forumcalendar.forumcalendar.service;

import ru.forumcalendar.forumcalendar.domain.Shift;
import ru.forumcalendar.forumcalendar.model.ShiftListModel;
import ru.forumcalendar.forumcalendar.model.form.ShiftForm;

import java.util.List;
import java.util.Map;

public interface ShiftService extends SecuredService, ResourceService<Shift, ShiftListModel, ShiftForm> {

    Integer getCurrentUserTeamByShift(int id);

    List<ShiftListModel> getShiftModelsByActivityId(int id);

    Map<Integer, String> getShiftIdNameMapByActivityId(int id);
}
