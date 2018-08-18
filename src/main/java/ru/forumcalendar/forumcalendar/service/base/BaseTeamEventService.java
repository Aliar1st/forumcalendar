package ru.forumcalendar.forumcalendar.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.forumcalendar.forumcalendar.domain.TeamEvent;
import ru.forumcalendar.forumcalendar.exception.EntityNotFoundException;
import ru.forumcalendar.forumcalendar.model.TeamEventModel;
import ru.forumcalendar.forumcalendar.model.form.TeamEventForm;
import ru.forumcalendar.forumcalendar.repository.TeamEventRepository;
import ru.forumcalendar.forumcalendar.service.TeamEventService;
import ru.forumcalendar.forumcalendar.service.TeamService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BaseTeamEventService implements TeamEventService {

    private final TeamEventRepository teamEventRepository;

    private final TeamService teamService;
    private final ConversionService conversionService;

    @Autowired
    public BaseTeamEventService(
            TeamEventRepository teamEventRepository,
            TeamService teamService,
            @Qualifier("mvcConversionService") ConversionService conversionService
    ) {
        this.teamEventRepository = teamEventRepository;
        this.teamService = teamService;
        this.conversionService = conversionService;
    }

    @Override
    public boolean exist(int id) {
        return teamEventRepository.findById(id).isPresent();
    }

    @Override
    public TeamEvent get(int id) {
        return teamEventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team event with id " + id + " not found"));
    }

    @Override
    public List<TeamEventModel> getAll() {
        return teamEventRepository.findAll()
                .stream()
                .map((t) -> conversionService.convert(t, TeamEventModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public TeamEvent save(TeamEventForm form) {

        TeamEvent teamEvent = teamEventRepository.findById(form.getId()).orElse(new TeamEvent());
        teamEvent.setName(form.getName());
        teamEvent.setStartDatetime(form.getStartDatetime());
        teamEvent.setEndDatetime(form.getEndDatetime());
        teamEvent.setPlace(form.getPlace());
        teamEvent.setDescription(form.getDescription());
        teamEvent.setTeam(teamService.get(form.getTeamId()));

        return teamEventRepository.save(teamEvent);
    }

    @Override
    public TeamEvent delete(int id) {
        TeamEvent teamEvent = get(id);
        teamEventRepository.deleteById(id);
        return teamEvent;
    }

    @Override
    public boolean hasPermissionToWrite(int id) {
        return teamService.hasPermissionToWrite(get(id).getTeam().getId());
    }

    @Override
    public boolean hasPermissionToRead(int id) {
        return true;
    }

    @Override
    public List<TeamEventModel> getTeamEventModelsByTeamIdAndDate(int teamId, LocalDate date) {

        LocalDateTime startDate = date.atStartOfDay();
        LocalDateTime endDate = startDate.plusDays(1);

        return teamEventRepository.getAllByTeamIdAndStartDatetimeBetween(teamId, startDate, endDate)
                .map((t) -> conversionService.convert(t, TeamEventModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<TeamEventModel> getTeamEventModelsByTeamId(int team_id) {
        return teamEventRepository.getByTeamIdOrderByStartDatetime(team_id)
                .map((t) -> conversionService.convert(t, TeamEventModel.class))
                .collect(Collectors.toList());
    }
}
