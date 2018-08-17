package ru.forumcalendar.forumcalendar.service.base;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.forumcalendar.forumcalendar.domain.Like;
import ru.forumcalendar.forumcalendar.domain.LikeIdentity;
import ru.forumcalendar.forumcalendar.repository.LikeRepository;
import ru.forumcalendar.forumcalendar.service.EventService;
import ru.forumcalendar.forumcalendar.service.LikeService;
import ru.forumcalendar.forumcalendar.service.UserService;

@Service
@Transactional
public class BaseLikeService implements LikeService {

    private final LikeRepository likeRepository;

    private final EventService eventService;
    private final UserService userService;

    public BaseLikeService(
            LikeRepository likeRepository,
            EventService eventService,
            UserService userService
    ) {
        this.likeRepository = likeRepository;
        this.eventService = eventService;
        this.userService = userService;
    }

    @Override
    public void setLike(int eventId, boolean isLike) {

        String userId = userService.getCurrentId();

        Like like = likeRepository.getByLikeIdentityEventIdAndLikeIdentityUserId(eventId, userId);

        if (like == null) {
            LikeIdentity likeIdentity = new LikeIdentity();
            likeIdentity.setUser(userService.getCurrentUser());
            likeIdentity.setEvent(eventService.get(eventId));

            like = new Like();
            like.setLikeIdentity(likeIdentity);
            like.setLike(isLike);

            likeRepository.save(like);
        } else {
            if (like.isLike() != isLike) {
                like.setLike(isLike);
                likeRepository.save(like);
            } else {
                likeRepository.delete(like);
            }
        }
    }

    @Override
    public void like(int eventId) {
        setLike(eventId, true);
    }

    @Override
    public void dislike(int eventId) {
        setLike(eventId, false);
    }

    @Override
    public void clear(int eventId) {

        String userId = userService.getCurrentId();

        Like like = likeRepository.getByLikeIdentityEventIdAndLikeIdentityUserId(eventId, userId);

        if (like != null) {
            likeRepository.delete(like);
        }
    }

    @Override
    public int getLikes(int eventId) {
        return likeRepository.countAllByLikeIdentityEventIdAndIsLikeTrue(eventId);
    }

    @Override
    public int getDislikes(int eventId) {
        return likeRepository.countAllByLikeIdentityEventIdAndIsLikeFalse(eventId);
    }

    @Override
    public int getLikeRaiting(int eventId) {
        return getLikes(eventId) - getDislikes(eventId);
    }
}
