package ru.forumcalendar.forumcalendar.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.forumcalendar.forumcalendar.model.ShiftModel;
import ru.forumcalendar.forumcalendar.repository.ShiftRepository;
import ru.forumcalendar.forumcalendar.service.ShiftService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BaseShiftService implements ShiftService {

    private ShiftRepository shiftRepository;

    private ConversionService conversionService;

    @Autowired
    public BaseShiftService(
            ShiftRepository shiftRepository,
            @Qualifier("mvcConversionService") ConversionService conversionService
    ) {
        this.shiftRepository = shiftRepository;
        this.conversionService = conversionService;
    }

    @Override
    public List<ShiftModel> getShiftModelsByActivityId(Integer id) {
        return shiftRepository.getAllByActivity_Id(id)
                .stream()
                .map((s) -> conversionService.convert(s, ShiftModel.class))
                .collect(Collectors.toList());
    }
}
