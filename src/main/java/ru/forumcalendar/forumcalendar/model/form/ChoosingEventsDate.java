package ru.forumcalendar.forumcalendar.model.form;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class ChoosingEventsDate {

    @NotNull
//    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
