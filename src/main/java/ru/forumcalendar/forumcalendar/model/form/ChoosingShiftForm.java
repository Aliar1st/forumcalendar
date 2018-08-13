package ru.forumcalendar.forumcalendar.model.form;

import lombok.Getter;
import lombok.Setter;
import ru.forumcalendar.forumcalendar.validation.annotation.ShiftExist;

@Getter
@Setter
public class ChoosingShiftForm {

    @ShiftExist
    private int shiftId;
}
