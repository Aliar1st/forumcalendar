package ru.forumcalendar.forumcalendar.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.forumcalendar.forumcalendar.domain.ContactType;
import ru.forumcalendar.forumcalendar.repository.ContactTypeRepository;
import ru.forumcalendar.forumcalendar.service.ContactService;

import java.util.List;

@Service
public class BaseContactService implements ContactService {

    private final ContactTypeRepository contactTypeRepository;

    @Autowired
    public BaseContactService(ContactTypeRepository contactTypeRepository) {
        this.contactTypeRepository = contactTypeRepository;
    }

    @Override
    public List<ContactType> getAllContactTypes() {
        return contactTypeRepository.findAll();
    }
}
