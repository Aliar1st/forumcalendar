package ru.forumcalendar.forumcalendar.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "likes")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class Like extends AuditModel {

    @EmbeddedId
    private LikeIdentity likeIdentity;

    @Column(nullable = false)
    private boolean isLike;
}
