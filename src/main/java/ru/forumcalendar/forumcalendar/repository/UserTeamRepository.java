package ru.forumcalendar.forumcalendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.forumcalendar.forumcalendar.domain.UserTeam;
import ru.forumcalendar.forumcalendar.domain.UserTeamIdentity;

public interface UserTeamRepository extends JpaRepository<UserTeam, UserTeamIdentity> {

    int countAllByUserTeamIdentityTeamId(int userTeamIdentity_team_id);
}
