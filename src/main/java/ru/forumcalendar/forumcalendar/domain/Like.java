package ru.forumcalendar.forumcalendar.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "likes")
@Getter
@Setter
@EqualsAndHashCode
public class Like extends AuditModel {

    @EmbeddedId
    private LikeIdentity likeIdentity;
}
