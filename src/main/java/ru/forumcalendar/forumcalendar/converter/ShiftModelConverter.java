package ru.forumcalendar.forumcalendar.converter;

import org.springframework.core.convert.converter.Converter;
import ru.forumcalendar.forumcalendar.domain.Shift;
import ru.forumcalendar.forumcalendar.domain.Team;
import ru.forumcalendar.forumcalendar.model.ShiftListModel;
import ru.forumcalendar.forumcalendar.model.ShiftModel;

import java.util.List;
import java.util.Set;

public class ShiftModelConverter implements Converter<Shift, ShiftModel> {

    @Override
    public ShiftModel convert(Shift shift) {

        ShiftModel shiftModel = new ShiftModel();
        shiftModel.setId(shift.getId());
        shiftModel.setActivityId(shift.getActivity().getId());
        shiftModel.setName(shift.getName());
        shiftModel.setStartDate(shift.getStartDate());
        shiftModel.setEndDate(shift.getEndDate());
        shiftModel.setDescription(shift.getDescription());

        Set<Team> teams = shift.getTeams();

        int userCount = teams
                .parallelStream()
                .mapToInt(team -> team.getUserTeams().size())
                .reduce(0, (left, right) -> left + right);

        shiftModel.setTeamCount(teams.size());
        shiftModel.setUserCount(userCount);
        shiftModel.setEventCount(shift.getEvents().size());

        return shiftModel;
    }
}
