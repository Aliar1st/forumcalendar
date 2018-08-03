package ru.forumcalendar.forumcalendar.dal.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "contact_types")
@Getter
@Setter
public class ContactType {

    @Id
    @GeneratedValue
    private int id;

    private String name;
}
