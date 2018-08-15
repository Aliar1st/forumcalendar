package ru.forumcalendar.forumcalendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.forumcalendar.forumcalendar.domain.Contact;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

}
