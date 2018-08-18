package ru.forumcalendar.forumcalendar.model.form;

import lombok.Data;
import ru.forumcalendar.forumcalendar.validation.annotation.EventExist;

@Data
public class ToggleSubscribeForm {

    @EventExist
    private int eventId;

    private String uniqueId;
}
