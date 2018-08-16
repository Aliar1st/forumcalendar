package ru.forumcalendar.forumcalendar.validation;

import org.springframework.beans.factory.annotation.Autowired;
import ru.forumcalendar.forumcalendar.repository.ContactTypeRepository;
import ru.forumcalendar.forumcalendar.service.ContactTypeService;
import ru.forumcalendar.forumcalendar.validation.annotation.ContactTypeExist;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ContactTypeExistValidator implements ConstraintValidator<ContactTypeExist, Integer> {

    private final ContactTypeService contactTypeService;

    @Autowired
    public ContactTypeExistValidator(ContactTypeService contactTypeService) {
        this.contactTypeService = contactTypeService;
    }

    public boolean isValid(Integer contactTypeId, ConstraintValidatorContext context) {
        return contactTypeService.exist(contactTypeId);
    }
}
