package ru.forumcalendar.forumcalendar.model.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.forumcalendar.forumcalendar.domain.Activity;
import ru.forumcalendar.forumcalendar.domain.Shift;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ActivityForm {

    private int id = -1;

    private String userId;

    private String name;

    private List<ShiftForm> shiftForms;

    public ActivityForm(Activity activity) {
        this.id = activity.getId();
        this.userId = activity.getUser().getId();
        this.name = activity.getName();

        Set<Shift> shifts = activity.getShifts();
        this.shiftForms = new ArrayList<>(shifts.size());

        for (Shift shift : shifts) {
            this.shiftForms.add(new ShiftForm(shift));
        }
    }
}
