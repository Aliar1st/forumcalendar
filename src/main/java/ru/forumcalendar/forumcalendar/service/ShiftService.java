package ru.forumcalendar.forumcalendar.service;

import ru.forumcalendar.forumcalendar.domain.Shift;
import ru.forumcalendar.forumcalendar.model.ShiftModel;
import ru.forumcalendar.forumcalendar.model.form.ShiftForm;

import java.util.List;
import java.util.Map;

public interface ShiftService extends SecuredService, ResourceService<Shift, ShiftModel, ShiftForm> {

    Integer getCurrentUserTeamByShift(int id);

    List<ShiftModel> getShiftModelsByActivityId(int id);

    Map<Integer, String> getShiftIdNameMapByActivityId(int id);
}
