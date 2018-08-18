package ru.forumcalendar.forumcalendar.model.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import ru.forumcalendar.forumcalendar.domain.Contact;
import ru.forumcalendar.forumcalendar.validation.annotation.ContactTypeExist;

import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class ContactForm {

    private int id;

    private String userId;

    @ContactTypeExist
    private int contactTypeId;

    @Size(max = 2000, message = "Description is too long")
    @Pattern(regexp = "https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_+.~#?&//=]*)", message = "Invalid link (example: https://regexr.com/)")
    private String link;

    public ContactForm(Contact contact) {
        this.id = contact.getId();
        this.userId = contact.getUser().getId();
        this.contactTypeId = contact.getContactType().getId();
        this.link = contact.getLink();
    }
}
