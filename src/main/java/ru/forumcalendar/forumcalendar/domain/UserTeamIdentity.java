package ru.forumcalendar.forumcalendar.domain;

import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;


@Embeddable
@EqualsAndHashCode(callSuper = false)
public class UserTeamIdentity implements Serializable {

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Team team;
}
