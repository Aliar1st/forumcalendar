package ru.forumcalendar.forumcalendar.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "activity_moderators")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class ActivityModerator extends AuditModel {

    @EmbeddedId
    private ActivityModeratorIdentity activityModeratorIdentity;
}
