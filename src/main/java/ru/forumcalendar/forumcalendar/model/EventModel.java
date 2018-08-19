package ru.forumcalendar.forumcalendar.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.forumcalendar.forumcalendar.domain.Shift;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class EventModel extends InnerEventModel implements Comparable<EventModel> {

    @Override
    public int compareTo(EventModel o) {
        if (this.getStartDatetime().isAfter(o.getStartDatetime())) {
            return 1;
        } else {
            return -1;
        }
    }
}
