package ru.forumcalendar.forumcalendar.dal.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "personal_info")
@Getter
@Setter
public class PersonalInfo extends AuditModel {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id")
    private Team team;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_role_id")
    private TeamRole teamRole;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private String photo;
}
