package ru.forumcalendar.forumcalendar.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@EqualsAndHashCode
public class Subscription extends AuditModel {

    @EmbeddedId
    private SubscriptionIdentity subscriptionIdentity;
}
