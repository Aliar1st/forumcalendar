package ru.forumcalendar.forumcalendar.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class Subscription extends AuditModel {

    @EmbeddedId
    private SubscriptionIdentity subscriptionIdentity;
}
