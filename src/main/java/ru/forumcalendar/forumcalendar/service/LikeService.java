package ru.forumcalendar.forumcalendar.service;

import ru.forumcalendar.forumcalendar.model.InnerShiftEventModel;

public interface LikeService {

    InnerShiftEventModel setLikeDislike(InnerShiftEventModel model, int eventId);

    RatingType isLike(int eventId);

    void setLike(int eventId, boolean like);

    void like(int eventId);

    void dislike(int eventId);

    void clear(int eventId);

    int getLikes(int eventId);

    int getDislikes(int eventId);

    int getLikeRaiting(int eventId);

    enum RatingType {
        LIKE,
        DISLIKE,
        NONE;
    }
}
