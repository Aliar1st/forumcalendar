package ru.forumcalendar.forumcalendar.converter;

import org.springframework.core.convert.converter.Converter;
import ru.forumcalendar.forumcalendar.domain.Shift;
import ru.forumcalendar.forumcalendar.model.ShiftModel;

public class ShiftModelConverter implements Converter<Shift, ShiftModel> {

    @Override
    public ShiftModel convert(Shift shift) {

        ShiftModel shiftModel = new ShiftModel();
        shiftModel.setId(shift.getId());
        shiftModel.setActivityId(shift.getActivity().getId());
        shiftModel.setName(shift.getName());
        shiftModel.setStartDate(shift.getStartDate());
        shiftModel.setEndDate(shift.getEndDate());
        shiftModel.setTeamCount(shift.getTeams().size());

        return shiftModel;
    }
}
