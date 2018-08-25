package ru.forumcalendar.forumcalendar.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "shifts")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Indexed
public class Shift extends AuditModel {

    @Id
    @GeneratedValue
    @Field(name = "shift_id")
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

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private String photo;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "shift", cascade = CascadeType.REMOVE)
    private Set<Team> teams = new HashSet<>();

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "shift", cascade = CascadeType.REMOVE)
    private Set<Event> events = new HashSet<>();
}
