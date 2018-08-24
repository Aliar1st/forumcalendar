package ru.forumcalendar.forumcalendar.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.forumcalendar.forumcalendar.domain.Contact;
import ru.forumcalendar.forumcalendar.domain.ContactType;
import ru.forumcalendar.forumcalendar.domain.Role;
import ru.forumcalendar.forumcalendar.domain.User;
import ru.forumcalendar.forumcalendar.exception.EntityNotFoundException;
import ru.forumcalendar.forumcalendar.model.UserModel;
import ru.forumcalendar.forumcalendar.model.form.ContactForm;
import ru.forumcalendar.forumcalendar.model.form.UserForm;
import ru.forumcalendar.forumcalendar.repository.*;
import ru.forumcalendar.forumcalendar.service.UploadsService;
import ru.forumcalendar.forumcalendar.service.UserService;

import java.io.File;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class BaseUserService implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ContactRepository contactRepository;
    private final ContactTypeRepository contactTypeRepository;
    private final UserTeamRepository userTeamRepository;

    private final UploadsService uploadsService;
    private final ConversionService conversionService;

    @Autowired
    public BaseUserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            ContactRepository contactRepository,
            ContactTypeRepository contactTypeRepository,
            UserTeamRepository userTeamRepository,
            UploadsService uploadsService,
            @Qualifier("mvcConversionService") ConversionService conversionService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.contactRepository = contactRepository;
        this.contactTypeRepository = contactTypeRepository;
        this.userTeamRepository = userTeamRepository;
        this.uploadsService = uploadsService;
        this.conversionService = conversionService;
    }

    @Override
    public boolean exist(String id) {
        return userRepository.findById(id).isPresent();
    }

    @Override
    public User signUp(Map<String, Object> userMap) {

        User user = new User();

        Role roleUser = roleRepository.findById(Role.ROLE_USER_ID)
                .orElseThrow(() -> new IllegalArgumentException("Can't find role with id " + Role.ROLE_USER_ID));

        user.setRole(roleUser);
        user.setId((String) userMap.get("sub"));
        user.setFirstName((String) userMap.get("given_name"));
        user.setLastName((String) userMap.get("family_name"));
        user.setEmail(userMap.get("email").toString());
        user.setGender((String) userMap.get("gender"));
        user.setLocale((String) userMap.get("locale"));


        String photo = uploadsService.upload((String) userMap.get("picture"), user.getId())
                .map(File::getName)
                .orElse("photo-ava.jpg");

        user.setPhoto(photo);

        userRepository.save(user);

        Contact c1 = new Contact();
        c1.setUser(user);
        c1.setContactType(contactTypeRepository.findById(1)
                .orElseThrow(() -> new IllegalArgumentException("Can't find contact type with id " + 1)));
        Contact c2 = new Contact();
        c2.setUser(user);
        c2.setContactType(contactTypeRepository.findById(1)
                .orElseThrow(() -> new IllegalArgumentException("Can't find contact type with id " + 2)));

        contactRepository.saveAll(Arrays.asList(c1, c2));

        return user;
    }

    @Override
    public User getCurrentUser(Principal principal) {
        return ((User) ((OAuth2Authentication) principal).getUserAuthentication().getPrincipal());
    }

    @Override
    public User getCurrentUser() {
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        return userRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("User with id " + user.getId() + " not found"));
    }

    @Override
    public String getCurrentId() {
        return getCurrentUser().getId();
    }

    @Override
    public boolean isCurrentSuperUser() {
        return getCurrentUser().getRole().getId() == Role.ROLE_SUPERUSER_ID;
    }

    @Override
    public User get(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + id + " not found"));
    }

    @Override
    @Transactional
    public User save(UserForm userForm) {

        User user = getCurrentUser();

        //noinspection Duplicates
        if (!userForm.getPhoto().isEmpty()) {
            String photo = uploadsService.upload(userForm.getPhoto(), getCurrentId())
                    .map((f) -> {
                        if (!user.getPhoto().equals(f.getName()))
                            uploadsService.delete(user.getPhoto());
                        return f.getName();
                    })
                    .orElse(user.getPhoto());
            user.setPhoto(photo);
        }

        user.setFirstName(userForm.getFirstName());
        user.setLastName(userForm.getLastName());

        if (userForm.getContactForms() != null) {
            List<Contact> contacts = new ArrayList<>(userForm.getContactForms().size());

            for (ContactForm cf : userForm.getContactForms()) {
                Contact contact = contactRepository.getByUserIdAndContactTypeId(user.getId(), cf.getContactTypeId()).orElse(new Contact());
                contact.setUser(user);
                contact.setLink(cf.getLink());

                ContactType contactType = contactTypeRepository.findById(cf.getContactTypeId())
                        .orElseThrow(() -> new EntityNotFoundException("Contact type with id " + cf.getContactTypeId() + " not found"));

                contact.setContactType(contactType);

                contacts.add(contact);
            }

            contactRepository.saveAll(contacts);
        }

        return userRepository.save(user);
    }

    @Override
    public List<UserModel> getAllNotCuratorsByShiftId(int shiftId) {
        return userTeamRepository.getAllNotCuratorsByShiftId(shiftId)
                .map((ut) -> conversionService.convert(ut.getUserTeamIdentity().getUser(), UserModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserModel> getAllNotCuratorsByActivityId(int activityId) {
        return userTeamRepository.getAllNotCuratorsByActivityId(activityId)
                .map((ut) -> conversionService.convert(ut.getUserTeamIdentity().getUser(), UserModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserModel> getAllCuratorsByShiftId(int shiftId) {
        return userTeamRepository.getAllCuratorsByShiftId(shiftId)
                .map((ut) -> conversionService.convert(ut.getUserTeamIdentity().getUser(), UserModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserModel> getAllCuratorsByActivityId(int activityId) {
        return userTeamRepository.getAllCuratorsByActivityId(activityId)
                .map((ut) -> conversionService.convert(ut.getUserTeamIdentity().getUser(), UserModel.class))
                .collect(Collectors.toList());
    }

//
//    @Override
//    public boolean hasRole(String role) {
//
//        //noinspection unchecked
//        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) SecurityContextHolder.getContext()
//                .getAuthentication().getAuthorities();
//
//        for (GrantedAuthority authority : authorities) {
//            if (authority.getAuthority().equals(role)) {
//                return true;
//            }
//        }
//
//        return false;
//    }
}
