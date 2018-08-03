package ru.forumcalendar.forumcalendar.dal.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "forums")
@Getter
@Setter
public class Forum {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    private String name;
}
