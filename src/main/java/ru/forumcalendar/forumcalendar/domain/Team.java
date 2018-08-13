package ru.forumcalendar.forumcalendar.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.jws.soap.SOAPBinding;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "teams")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class Team extends AuditModel {

    @Id
    @GeneratedValue
    private int id;

//    @ManyToOne
//    @JoinColumn(nullable = false)
//    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Shift shift;

    @Column(nullable = false)
    private int number;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String direction;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "team")
    @EqualsAndHashCode.Exclude
    private Set<TeamEvent> teamEvents = new HashSet<>();

    @OneToMany(mappedBy = "userTeamIdentity.team")
    @EqualsAndHashCode.Exclude
    private Set<UserTeam> userTeams = new HashSet<>();
}
