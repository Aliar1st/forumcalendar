package ru.forumcalendar.forumcalendar.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class User extends AuditModel implements UserDetails {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    private String gender;

    private String locale;

    private String photo;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "user")
    private Set<Activity> activities = new HashSet<>();

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "userTeamIdentity.user")
    private Set<UserTeam> userTeams = new HashSet<>();

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "likeIdentity.user")
    private Set<Like> likes = new HashSet<>();

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "subscriptionIdentity.user")
    private Set<Subscription> subscriptions = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(role);
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
