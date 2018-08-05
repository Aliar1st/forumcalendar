package ru.forumcalendar.forumcalendar.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "teams")
@Getter
@Setter
@EqualsAndHashCode
public class Team extends AuditModel {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Forum forum;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String gang;

    @Column(nullable = false)
    private String direction;

    private String description;

    @OneToMany(mappedBy = "team")
    private Set<Activity> activities = new HashSet<>();
}
