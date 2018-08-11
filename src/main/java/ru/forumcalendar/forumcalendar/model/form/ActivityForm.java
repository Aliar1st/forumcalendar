package ru.forumcalendar.forumcalendar.model.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import ru.forumcalendar.forumcalendar.domain.Activity;
import ru.forumcalendar.forumcalendar.domain.Shift;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ActivityForm {

    private int id = -1;

    @Length(max = 50, message = "Activity name is too long")
    @Pattern(regexp = "[A-ZА-Я][A-Za-zА-Яа-я]+", message = "Activity name is too short or contains invalid characters")
    private String name;

    @Valid
    private List<ShiftForm> shiftForms;

    public ActivityForm(Activity activity) {
        this.id = activity.getId();
        this.name = activity.getName();

        Set<Shift> shifts = activity.getShifts();
        this.shiftForms = new ArrayList<>(shifts.size());

        for (Shift shift : shifts) {
            this.shiftForms.add(new ShiftForm(shift));
        }

        this.shiftForms.sort(Comparator.comparing(ShiftForm::getStartDate));
    }
}
