package ru.forumcalendar.forumcalendar.model.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.forumcalendar.forumcalendar.domain.Shift;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ShiftForm {

    private int id = -1;

    private String name;

    private int activityId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    public ShiftForm(Shift shift) {
        this.id = shift.getId();
        this.name = shift.getName();
        this.activityId = shift.getActivity().getId();
        this.startDate = shift.getStartDate();
        this.endDate = shift.getEndDate();
    }
}
