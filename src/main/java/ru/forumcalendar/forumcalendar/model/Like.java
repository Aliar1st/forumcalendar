package ru.forumcalendar.forumcalendar.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "likes")
@Getter
@Setter
public class Like extends AuditModel {

    @EmbeddedId
    private LikeIdentity likeIdentity;
}
