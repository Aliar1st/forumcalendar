package ru.forumcalendar.forumcalendar.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "speakers")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class Speaker extends AuditModel {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Activity activity;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String link;

    @Column(columnDefinition = "TEXT")
    private String description;
}
