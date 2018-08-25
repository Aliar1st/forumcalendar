package ru.forumcalendar.forumcalendar.model.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import ru.forumcalendar.forumcalendar.domain.Activity;
import ru.forumcalendar.forumcalendar.domain.Shift;

import javax.persistence.Column;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ActivityForm {// implements ResourceForm<Activity> {

    private int id;

    private String photoUrl;

    private MultipartFile photo;

    @Size(max = 50, message = "Name is too long")
    @Pattern(regexp = "([A-Za-zА-Яа-я0-9]\\s?)+", message = "Name contains invalid characters or too short")
    private String name;

    @NotNull(message = "Enter start date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "Enter end date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Size(max = 50, message = "Name is too long")
    @Pattern(regexp = "([A-Za-zА-Яа-я0-9]\\s?)+", message = "Place contains invalid characters or too short")
    private String place;

    @Size(min = 2, message = "Description is too short")
    @Size(max = 5000, message = "Description is too long")
    private String description;

    @Valid
    private List<ShiftForm> shiftForms;

    public ActivityForm(Activity activity) {
        this.id = activity.getId();
        this.name = activity.getName();
        this.description = activity.getDescription();
        this.place = activity.getPlace();
        this.startDate = activity.getStartDate();
        this.endDate = activity.getEndDate();

        Set<Shift> shifts = activity.getShifts();
        this.shiftForms = new ArrayList<>(shifts.size());

        for (Shift shift : shifts) {
            this.shiftForms.add(new ShiftForm(shift));
        }

        this.shiftForms.sort(Comparator.comparing(ShiftForm::getStartDate));
    }
}
