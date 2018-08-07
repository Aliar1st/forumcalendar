package ru.forumcalendar.forumcalendar.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "shifts")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class Shift extends AuditModel {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Activity activity;

    @Column
    private String name;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "shift", cascade = CascadeType.REMOVE)
    private Set<Team> teams = new HashSet<>();
}
