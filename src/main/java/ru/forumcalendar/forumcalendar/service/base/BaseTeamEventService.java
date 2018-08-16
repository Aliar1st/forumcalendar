package ru.forumcalendar.forumcalendar.service.base;

import org.springframework.stereotype.Service;
import ru.forumcalendar.forumcalendar.domain.TeamEvent;
import ru.forumcalendar.forumcalendar.model.TeamEventModel;
import ru.forumcalendar.forumcalendar.model.form.TeamEventForm;
import ru.forumcalendar.forumcalendar.service.TeamEventService;

import java.util.List;

@Service
public class BaseTeamEventService implements TeamEventService {

    @Override
    public boolean exist(int id) {
        return false;
    }

    @Override
    public TeamEvent get(int id) {
        return null;
    }

    @Override
    public List<TeamEventModel> getAll() {
        return null;
    }

    @Override
    public TeamEvent save(TeamEventForm form) {
        return null;
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public boolean hasPermissionToWrite(int id) {
        return false;
    }

    @Override
    public boolean hasPermissionToRead(int id) {
        return false;
    }
}
