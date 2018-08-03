package ru.forumcalendar.forumcalendar.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
public class Subscription extends AuditModel {

    @EmbeddedId
    private SubscriptionIdentity subscriptionIdentity;
}
