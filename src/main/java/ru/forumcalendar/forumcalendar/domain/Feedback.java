package ru.forumcalendar.forumcalendar.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "feedbacks")
@Getter
@Setter
public class Feedback extends AuditModel {

    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String theme;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String feedback;
}
