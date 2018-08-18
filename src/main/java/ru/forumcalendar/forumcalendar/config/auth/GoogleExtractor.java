package ru.forumcalendar.forumcalendar.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import ru.forumcalendar.forumcalendar.domain.User;
import ru.forumcalendar.forumcalendar.repository.UserRepository;
import ru.forumcalendar.forumcalendar.service.UserService;

import java.util.List;
import java.util.Map;

@Component
public class GoogleExtractor implements PrincipalExtractor, AuthoritiesExtractor {

    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public GoogleExtractor(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public Object extractPrincipal(Map<String, Object> map) {
        return userRepository.findById(map.get("sub").toString())
                .orElseGet(() -> userService.signUp(map));
    }

    @Override
    public List<GrantedAuthority> extractAuthorities(Map<String, Object> map) {
        User user = userService.get(map.get("sub").toString());
        return AuthorityUtils.createAuthorityList(user.getRole().getAuthority());
    }
}
