package ru.forumcalendar.forumcalendar.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "team_roles")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class TeamRole extends AuditModel {

    public static final int ROLE_CURATOR_ID = 1;
    public static final int ROLE_CAPTAIN_ID = 2;
    public static final int ROLE_MEMBER_ID = 3;

    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String slug;
}
