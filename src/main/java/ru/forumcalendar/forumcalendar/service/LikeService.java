package ru.forumcalendar.forumcalendar.service;

public interface LikeService {

    void setLike(int eventId, boolean like);

    void like(int eventId);

    void dislike(int eventId);

    void clear(int eventId);

    int getLikes(int eventId);

    int getDislikes(int eventId);

    int getLikeRaiting(int eventId);
}
