package ru.forumcalendar.forumcalendar.converter;

import org.springframework.core.convert.converter.Converter;
import ru.forumcalendar.forumcalendar.domain.Activity;
import ru.forumcalendar.forumcalendar.domain.Shift;
import ru.forumcalendar.forumcalendar.model.ActivityModel;

import java.time.LocalDate;
import java.util.Set;

public class ActivityModelConverter implements Converter<Activity, ActivityModel> {

    @Override
    public ActivityModel convert(Activity activity) {

        ActivityModel activityModel = new ActivityModel();
        activityModel.setId(activity.getId());
        activityModel.setName(activity.getName());
        activityModel.setDescription(activity.getDescription());

        Set<Shift> shifts = activity.getShifts();
        activityModel.setShiftCount(shifts.size());

        if (!shifts.isEmpty()) {
            Shift shiftTmp = shifts.iterator().next();
            LocalDate start_date = shiftTmp.getStartDate();
            LocalDate end_date = shiftTmp.getEndDate();

            for (Shift shift : shifts) {
                if (start_date.isAfter(shift.getStartDate()))
                    start_date = shift.getStartDate();

                if (end_date.isBefore(shift.getEndDate()))
                    end_date = shift.getEndDate();
            }

            activityModel.setStartDate(start_date);
            activityModel.setEndDate(end_date);
        }

        return activityModel;
    }
}
