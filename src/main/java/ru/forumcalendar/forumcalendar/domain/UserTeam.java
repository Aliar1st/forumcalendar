package ru.forumcalendar.forumcalendar.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "user_teams")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class UserTeam extends AuditModel {

    @EmbeddedId
    private UserTeamIdentity userTeamIdentity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private TeamRole teamRole;
}
