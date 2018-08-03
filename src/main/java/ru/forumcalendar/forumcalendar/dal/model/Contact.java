package ru.forumcalendar.forumcalendar.dal.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "contacts")
@Getter
@Setter
public class Contact extends AuditModel {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private ContactType contactType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private PersonalInfo personalInfo;

    @NotNull
    private String link;
}
