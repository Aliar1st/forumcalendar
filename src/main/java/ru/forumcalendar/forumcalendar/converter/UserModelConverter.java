package ru.forumcalendar.forumcalendar.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import ru.forumcalendar.forumcalendar.domain.Contact;
import ru.forumcalendar.forumcalendar.domain.User;
import ru.forumcalendar.forumcalendar.model.ContactModel;
import ru.forumcalendar.forumcalendar.model.UserModel;
import ru.forumcalendar.forumcalendar.repository.ContactRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserModelConverter implements Converter<User, UserModel> {

    private ContactModelConverter contactModelConverter;

    @Override
    public UserModel convert(User user) {

        UserModel userModel = new UserModel();
        userModel.setId(user.getId());
        userModel.setFirstName(user.getFirstName());
        userModel.setLastName(user.getLastName());
        userModel.setPhoto(user.getPhoto());

        List<ContactModel> contacts = user.getContacts()
                .stream()
                .map((c) -> contactModelConverter.convert(c))
                .collect(Collectors.toList());

        userModel.setContacts(contacts);

        return userModel;
    }

    @Autowired
    public void setConversionService(@Lazy ContactModelConverter contactModelConverter) {
        this.contactModelConverter = contactModelConverter;
    }
}
