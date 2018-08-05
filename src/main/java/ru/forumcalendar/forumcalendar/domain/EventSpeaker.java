package ru.forumcalendar.forumcalendar.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "event_speakers")
@Getter
@Setter
public class EventSpeaker extends AuditModel {

    @EmbeddedId
    private EventSpeakerIdentity eventSpeakerIdentity;
}
