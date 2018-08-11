package ru.forumcalendar.forumcalendar.model.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import ru.forumcalendar.forumcalendar.domain.Shift;
import ru.forumcalendar.forumcalendar.validation.annotation.ActivityExist;
import ru.forumcalendar.forumcalendar.validation.annotation.DateTimeOrder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@DateTimeOrder(
        dateTimeBefore = "startDate",
        dateTimeAfter = "endDate"
)
public class ShiftForm {

    private int id = -1;

    //@ActivityExist
    private int activityId;

    @Length(max = 50, message = "Shift name is too long")
    @Pattern(regexp = "[A-ZА-Я][A-Za-zА-Яа-я]+", message = "Shift name is too short or contains invalid characters")
    private String name;

    //TODO добавить аннотацию для даты
    @NotNull(message = "Enter start date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    //TODO добавить аннотацию для даты
    @NotNull(message = "Enter end date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    public ShiftForm(Shift shift) {
        this.id = shift.getId();
        this.activityId = shift.getActivity().getId();
        this.name = shift.getName();
        this.startDate = shift.getStartDate();
        this.endDate = shift.getEndDate();
    }
}
