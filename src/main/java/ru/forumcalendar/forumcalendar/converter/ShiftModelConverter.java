package ru.forumcalendar.forumcalendar.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.forumcalendar.forumcalendar.domain.Shift;
import ru.forumcalendar.forumcalendar.model.ShiftModel;
import ru.forumcalendar.forumcalendar.repository.TeamRepository;

public class ShiftModelConverter implements Converter<Shift, ShiftModel> {

    private TeamRepository teamRepository;

    @Override
    public ShiftModel convert(Shift shift) {

        ShiftModel shiftModel = new ShiftModel();
        shiftModel.setId(shift.getId());
        shiftModel.setActivityId(shift.getActivity().getId());
        shiftModel.setName(shift.getName());
        shiftModel.setStartDate(shift.getStartDate());
        shiftModel.setEndDate(shift.getEndDate());
        shiftModel.setTeamCount(teamRepository.countAllByShift_id(shift.getId()));

        return shiftModel;
    }

    @Autowired
    public void setTeamRepository(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }
}
