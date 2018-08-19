package ru.forumcalendar.forumcalendar.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.forumcalendar.forumcalendar.domain.Event;

import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class SpeakerModel extends InnerSpeakerModel {

    private List<InnerEventModel> events;
}
