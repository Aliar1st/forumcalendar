package ru.forumcalendar.forumcalendar.converter;

import org.springframework.core.convert.converter.Converter;
import ru.forumcalendar.forumcalendar.domain.Contact;
import ru.forumcalendar.forumcalendar.model.ContactModel;

public class ContactModelConverter implements Converter<Contact, ContactModel> {

    @Override
    public ContactModel convert(Contact contact) {

        ContactModel contactModel = new ContactModel();
        contactModel.setId(contact.getId());
        contactModel.setLink(contact.getLink());
        contactModel.setContactTypeName(contact.getContactType().getName());

        return contactModel;
    }
}
