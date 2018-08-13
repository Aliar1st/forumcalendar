package ru.forumcalendar.forumcalendar.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "activities")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class Activity extends AuditModel {

    @Id
    @GeneratedValue
    private int id;

    @OneToOne
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "activity", cascade = CascadeType.REMOVE)
    private Set<Shift> shifts = new HashSet<>();
}
