package ru.forumcalendar.forumcalendar.dal.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "contacts")
@Getter
@Setter
public class Contact {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contact_type_id")
    private ContactType contactType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "personal_info_id")
    private PersonalInfo personalInfo;

    private String link;
}
