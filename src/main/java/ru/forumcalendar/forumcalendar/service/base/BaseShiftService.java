package ru.forumcalendar.forumcalendar.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.forumcalendar.forumcalendar.domain.Shift;
import ru.forumcalendar.forumcalendar.domain.UserTeam;
import ru.forumcalendar.forumcalendar.exception.EntityNotFoundException;
import ru.forumcalendar.forumcalendar.model.ShiftModel;
import ru.forumcalendar.forumcalendar.model.form.ShiftForm;
import ru.forumcalendar.forumcalendar.repository.ActivityRepository;
import ru.forumcalendar.forumcalendar.repository.ShiftRepository;
import ru.forumcalendar.forumcalendar.repository.UserTeamRepository;
import ru.forumcalendar.forumcalendar.service.ActivityService;
import ru.forumcalendar.forumcalendar.service.ShiftService;
import ru.forumcalendar.forumcalendar.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BaseShiftService implements ShiftService {

    private final ActivityRepository activityRepository;
    private final ShiftRepository shiftRepository;
    private final UserTeamRepository userTeamRepository;

    private final ActivityService activityService;
    private final UserService userService;
    private final ConversionService conversionService;

    @Autowired
    public BaseShiftService(
            ActivityRepository activityRepository,
            ShiftRepository shiftRepository,
            UserTeamRepository userTeamRepository,
            ActivityService activityService,
            UserService userService,
            @Qualifier("mvcConversionService") ConversionService conversionService
    ) {
        this.activityRepository = activityRepository;
        this.shiftRepository = shiftRepository;
        this.userTeamRepository = userTeamRepository;
        this.activityService = activityService;
        this.userService = userService;
        this.conversionService = conversionService;
    }

    @Override
    public boolean exist(int id) {
        return shiftRepository.findById(id).isPresent();
    }

    @Override
    public Shift get(int id) {
        return shiftRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shift with id " + id + " not found"));
    }

    @Override
    public List<ShiftModel> getAll() {
        return shiftRepository.findAll()
                .stream()
                .map((s) -> conversionService.convert(s, ShiftModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public Shift save(ShiftForm shiftForm) {

        Shift shift = shiftRepository.findById(shiftForm.getId()).orElse(new Shift());
        shift.setName(shiftForm.getName());
        shift.setStartDate(shiftForm.getStartDate());
        shift.setEndDate(shiftForm.getEndDate());
        shift.setDescription(shiftForm.getDescription());
        shift.setActivity(activityRepository.findById(shiftForm.getActivityId())
                .orElseThrow(() -> new EntityNotFoundException("Activity with id " + shiftForm.getActivityId() + " not found")));

        return shiftRepository.save(shift);
    }

    @Override
    public Shift delete(int id) {
        Shift shift = get(id);
        shiftRepository.deleteById(id);
        return shift;
    }

    @Override
    public Integer getCurrentUserTeamByShift(int id) {

        UserTeam userTeam = userTeamRepository
                .getByUserIdAndTeamShiftId(userService.getCurrentId(), id);

        return userTeam == null ? null : userTeam.getUserTeamIdentity().getTeam().getId();
    }

    @Override
    public List<ShiftModel> getShiftModelsByActivityId(int id) {
        return shiftRepository.getAllByActivityIdOrderByCreatedAt(id)
                .stream()
                .map((s) -> conversionService.convert(s, ShiftModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public Map<Integer, String> getShiftIdNameMapByActivityId(int id) {
        return shiftRepository.getAllByActivityIdOrderByCreatedAt(id)
                .stream()
                .collect(Collectors.toMap(Shift::getId, Shift::getName));
    }

    @Override
    public boolean hasPermissionToWrite(int id) {
        return activityService.hasPermissionToWrite(get(id).getActivity().getId());
    }

    @Override
    public boolean hasPermissionToRead(int id) {
        return true;
    }
}
