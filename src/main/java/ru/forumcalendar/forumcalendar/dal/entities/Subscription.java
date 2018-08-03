package ru.forumcalendar.forumcalendar.dal.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
public class Subscription {

    @EmbeddedId
    private SubscriptionIdentity subscriptionIdentity;
}
