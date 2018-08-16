package ru.forumcalendar.forumcalendar.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.forumcalendar.forumcalendar.domain.TeamEvent;
import ru.forumcalendar.forumcalendar.exception.EntityNotFoundException;
import ru.forumcalendar.forumcalendar.model.TeamEventModel;
import ru.forumcalendar.forumcalendar.model.TeamModel;
import ru.forumcalendar.forumcalendar.model.form.TeamEventForm;
import ru.forumcalendar.forumcalendar.repository.TeamEventRepository;
import ru.forumcalendar.forumcalendar.service.TeamEventService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BaseTeamEventService implements TeamEventService {

    private final TeamEventRepository teamEventRepository;

    private final ConversionService conversionService;

    @Autowired
    public BaseTeamEventService(
            TeamEventRepository teamEventRepository,
            @Qualifier("mvcConversionService") ConversionService conversionService
    ) {
        this.teamEventRepository = teamEventRepository;
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
        return null;
    }

    @Override
    public void delete(int id) {
        teamEventRepository.deleteById(id);
    }

    @Override
    public boolean hasPermissionToWrite(int id) {
        return false;
    }

    @Override
    public boolean hasPermissionToRead(int id) {
        return true;
    }
}
