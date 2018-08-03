package ru.forumcalendar.forumcalendar.dal.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "teams")
@Getter
@Setter
public class Team extends AuditModel {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "forum_id")
    private Forum forum;

    @NotNull
    private String name;

    @NotNull
    private String gang;

    @NotNull
    private String direction;

    private String description;
}
