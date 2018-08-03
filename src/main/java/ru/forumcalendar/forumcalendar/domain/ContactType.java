package ru.forumcalendar.forumcalendar.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "contact_types")
@Getter
@Setter
public class ContactType extends AuditModel {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    private String name;
}
