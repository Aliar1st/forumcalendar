package ru.forumcalendar.forumcalendar.model.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;
import ru.forumcalendar.forumcalendar.domain.Contact;
import ru.forumcalendar.forumcalendar.domain.User;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UserForm {

    private String id;

    private String photoUrl;

    private MultipartFile photo;

    @Size(max = 50, message = "Name is too long")
    @Pattern(regexp = "[A-ZА-Я][A-Za-zА-Яа-я_\\-\\s]+", message = "First name contains invalid characters")
    private String firstName;

    @Size(max = 50, message = "Name is too long")
    @Pattern(regexp = "[A-ZА-Я][A-Za-zА-Яа-я_\\-\\s]+", message = "Last name contains invalid characters")
    private String lastName;

    @Valid
    private List<ContactForm> contactForms;

    public UserForm(User user) {
        this.id = user.getId();
        this.photoUrl = user.getPhoto();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();

        Set<Contact> contacts = user.getContacts();
        this.contactForms = new ArrayList<>(contacts.size());

        for (Contact c : contacts) {
            this.contactForms.add(new ContactForm(c));
        }

        this.contactForms.sort(Comparator.comparing(ContactForm::getContactTypeId));
    }
}
