package ru.forumcalendar.forumcalendar.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;


@Entity
@Table(name = "roles")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class Role extends AuditModel implements GrantedAuthority {

    public static final int ROLE_USER_ID = 1;
    public static final int ROLE_SUPERUSER_ID = 2;

    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @NaturalId
    private String authority;
}
