package ru.forumcalendar.forumcalendar.model.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import ru.forumcalendar.forumcalendar.domain.Shift;
import ru.forumcalendar.forumcalendar.validation.annotation.ActivityExist;

import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ShiftForm {

    int id = -1;

    //@ActivityExist
    private int activityId;

    @Length(max = 50, message = "Shift name is too long")
    @Pattern(regexp = "[A-ZА-Я][A-Za-zА-Яа-я]+", message = "Shift name is too short or contains invalid characters")
    String name;

    //TODO добавить аннотацию для даты
    //@NotBlank(message = "Enter start date")
    //@DateTimeOrder
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate startDate;

    //TODO добавить аннотацию для даты
    //@NotBlank(message = "Enter end date")
    //@DateTimeOrder
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate endDate;

    public ShiftForm(Shift shift) {
        this.id = shift.getId();
        this.name = shift.getName();
        this.startDate = shift.getStartDate();
        this.endDate = shift.getEndDate();
    }
}
