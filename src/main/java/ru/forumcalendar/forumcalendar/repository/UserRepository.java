package ru.forumcalendar.forumcalendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.forumcalendar.forumcalendar.domain.Activity;
import ru.forumcalendar.forumcalendar.domain.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByLogin(String login);
}
