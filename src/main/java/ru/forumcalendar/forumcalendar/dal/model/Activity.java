package ru.forumcalendar.forumcalendar.dal.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "activities")
@Getter
@Setter
public class Activity extends AuditModel {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Team team;

    @NotNull
    private Date date;

    @NotNull
    private Time time;

    @NotNull
    private String place;

    @NotNull
    private String description;
}
