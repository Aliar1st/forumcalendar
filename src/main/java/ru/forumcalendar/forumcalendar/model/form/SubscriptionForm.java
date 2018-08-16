package ru.forumcalendar.forumcalendar.model.form;

import lombok.Getter;
import lombok.Setter;
import ru.forumcalendar.forumcalendar.validation.annotation.EventExist;

@Getter
@Setter
public class SubscriptionForm {

    @EventExist
    private int eventId;

    // TODO: 8/15/2018 Проверять в текущей ли смене событие
}
