package ru.forumcalendar.forumcalendar.model.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import ru.forumcalendar.forumcalendar.domain.Shift;
import ru.forumcalendar.forumcalendar.validation.annotation.DateTimeOrder;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@DateTimeOrder(
        dateTimeBefore = "startDate",
        dateTimeAfter = "endDate"
)
public class ShiftForm {

    private int id;

    //@ActivityExist
    private int activityId;

    @Size(max = 50, message = "Name is too long")
    @Pattern(regexp = "([A-Za-zА-Яа-я0-9]\\s?)+", message = "Name contains invalid characters or too short")
    private String name;

    @NotNull(message = "Enter start date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "Enter end date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Size(min = 2, message = "Description is too short")
    @Size(max = 5000, message = "Description is too long")
    private String description;

    public ShiftForm(Shift shift) {
        this.id = shift.getId();
        this.activityId = shift.getActivity().getId();
        this.name = shift.getName();
        this.startDate = shift.getStartDate();
        this.endDate = shift.getEndDate();
        this.description = shift.getDescription();
    }
}
