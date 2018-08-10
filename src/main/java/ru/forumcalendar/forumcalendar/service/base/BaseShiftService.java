package ru.forumcalendar.forumcalendar.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.forumcalendar.forumcalendar.domain.Shift;
import ru.forumcalendar.forumcalendar.exception.EntityNotFoundException;
import ru.forumcalendar.forumcalendar.model.ShiftModel;
import ru.forumcalendar.forumcalendar.model.form.ShiftForm;
import ru.forumcalendar.forumcalendar.repository.ActivityRepository;
import ru.forumcalendar.forumcalendar.repository.ShiftRepository;
import ru.forumcalendar.forumcalendar.service.ShiftService;
import ru.forumcalendar.forumcalendar.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BaseShiftService implements ShiftService {

    private final ActivityRepository activityRepository;
    private final ShiftRepository shiftRepository;

    private final UserService userService;
    private final ConversionService conversionService;

    @Autowired
    public BaseShiftService(
            ActivityRepository activityRepository,
            ShiftRepository shiftRepository,
            UserService userService,
            @Qualifier("mvcConversionService") ConversionService conversionService
    ) {
        this.activityRepository = activityRepository;
        this.shiftRepository = shiftRepository;
        this.userService = userService;
        this.conversionService = conversionService;
    }

    @Override
    public Shift get(int id) {
        return shiftRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shift with id " + id + " not found"));
    }

    @Override
    public Shift save(ShiftForm shiftForm) {

        Shift shift = shiftRepository.findById(shiftForm.getId()).orElse(new Shift());
        shift.setName(shiftForm.getName());
        shift.setStartDate(shiftForm.getStartDate());
        shift.setEndDate(shiftForm.getEndDate());
        shift.setActivity(activityRepository.findById(shiftForm.getActivityId())
                .orElseThrow(() -> new EntityNotFoundException("Activity with id " + shiftForm.getActivityId() + " not found")));

        return shiftRepository.save(shift);
    }

    @Override
    public void delete(int id) {
        shiftRepository.deleteById(id);
    }

    @Override
    public List<ShiftModel> getShiftModelsByActivityId(int id) {
        return shiftRepository.getAllByActivityIdOrderByCreatedAt(id)
                .stream()
                .map((s) -> conversionService.convert(s, ShiftModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isUserShift(int id) {
        return get(id).getActivity().getUser().getId().equals(userService.getCurrentId());
    }
}
