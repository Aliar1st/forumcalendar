package ru.forumcalendar.forumcalendar.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserModel {

    private String id;

    private String photo;

    private String firstName;

    private String lastName;

    private List<ContactModel> contacts;
}
