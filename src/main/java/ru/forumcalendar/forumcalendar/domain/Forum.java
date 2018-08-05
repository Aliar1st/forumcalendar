package ru.forumcalendar.forumcalendar.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "forums")
@Getter
@Setter
@EqualsAndHashCode
public class Forum extends AuditModel {

    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String name;
}
