package ru.forumcalendar.forumcalendar.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.forumcalendar.forumcalendar.domain.ContactType;
import ru.forumcalendar.forumcalendar.repository.ContactTypeRepository;
import ru.forumcalendar.forumcalendar.service.ContactTypeService;

import java.util.List;

@Service
public class BaseContactTypeService implements ContactTypeService {

    private final ContactTypeRepository contactTypeRepository;

    @Autowired
    public BaseContactTypeService(ContactTypeRepository contactTypeRepository) {
        this.contactTypeRepository = contactTypeRepository;
    }

    @Override
    public boolean exist(int id) {
        return contactTypeRepository.findById(id).isPresent();
    }

    @Override
    public List<ContactType> getAll() {
        return contactTypeRepository.findAll();
    }
}
