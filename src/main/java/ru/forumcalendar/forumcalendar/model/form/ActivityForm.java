package ru.forumcalendar.forumcalendar.model.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import ru.forumcalendar.forumcalendar.domain.Activity;
import ru.forumcalendar.forumcalendar.domain.Shift;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ActivityForm {// implements ResourceForm<Activity> {

    private int id;

    @Size(min = 2, message = "Name is too short")
    @Size(max = 50, message = "Name is too long")
    @Pattern(regexp = "([A-Za-zА-Яа-я0-9]\\s?)+", message = "Name contains invalid characters")
    private String name;

    @Size(max = 5000, message = "Description is too long")
    private String description;

    @Valid
    private List<ShiftForm> shiftForms;

    public ActivityForm(Activity activity) {
        this.id = activity.getId();
        this.name = activity.getName();
        this.description = activity.getDescription();

        Set<Shift> shifts = activity.getShifts();
        this.shiftForms = new ArrayList<>(shifts.size());

        for (Shift shift : shifts) {
            this.shiftForms.add(new ShiftForm(shift));
        }

        this.shiftForms.sort(Comparator.comparing(ShiftForm::getStartDate));
    }
}
