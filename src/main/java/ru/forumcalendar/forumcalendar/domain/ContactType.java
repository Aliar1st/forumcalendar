package ru.forumcalendar.forumcalendar.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "contact_types")
@Getter
@Setter
@EqualsAndHashCode
public class ContactType extends AuditModel {

    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String name;
}
