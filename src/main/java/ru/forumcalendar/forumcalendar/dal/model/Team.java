package ru.forumcalendar.forumcalendar.dal.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "teams")
@Getter
@Setter
public class Team extends AuditModel {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Forum forum;

    @NotNull
    private String name;

    @NotNull
    private String gang;

    @NotNull
    private String direction;

    private String description;

    @OneToMany
    private Set<Activity> activities = new HashSet<>();
}
