package ru.forumcalendar.forumcalendar.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.sql.Date;
import java.sql.Time;

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
    private Date date;

    @Column(nullable = false)
    private Time time;

    @Column(nullable = false)
    private String place;

    @Column(nullable = false)
    private String description;
}
