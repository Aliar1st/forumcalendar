package ru.forumcalendar.forumcalendar.domain;

import lombok.EqualsAndHashCode;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode(callSuper = false)
public class EventSpeakerIdentity implements Serializable {

    @ManyToOne
    @JoinColumn(nullable = false)
    private Speaker speaker;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Event event;
}
