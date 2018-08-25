package ru.forumcalendar.forumcalendar.converter;

import org.springframework.core.convert.converter.Converter;
import ru.forumcalendar.forumcalendar.domain.Shift;
import ru.forumcalendar.forumcalendar.model.ShiftListModel;

public class ShiftListModelConverter implements Converter<Shift, ShiftListModel> {

    @Override
    public ShiftListModel convert(Shift shift) {

        ShiftListModel shiftListModel = new ShiftListModel();
        shiftListModel.setId(shift.getId());
        shiftListModel.setActivityId(shift.getActivity().getId());
        shiftListModel.setName(shift.getName());
        shiftListModel.setStartDate(shift.getStartDate());
        shiftListModel.setEndDate(shift.getEndDate());
        shiftListModel.setDescription(shift.getDescription());
        shiftListModel.setPhotoUrl(shift.getPhoto());

        return shiftListModel;
    }
}
