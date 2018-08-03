package ru.forumcalendar.forumcalendar.dal.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "links")
@Getter
@Setter
public class Link {

    @Id
    private String link;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id")
    private Team team;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_role_id")
    private TeamRole teamRole;
}
