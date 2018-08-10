package ru.forumcalendar.forumcalendar.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "team_events")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class TeamEvent extends AuditModel {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Team team;

    @Column(nullable = false)
    private LocalDateTime datetime;

    @Column(nullable = false)
    private String place;

    @Column(nullable = false)
    private String description;
}
