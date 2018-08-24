
/*Добавить чела в команду*/
INSERT INTO forumcalendar.user_teams (user_id, team_id, team_role_id, created_at, updated_at) values
  (1, 1, 2, NOW(), NOW());


SELECT * FROM forumcalendar.user_teams;

/*Куратор*/
UPDATE forumcalendar.user_teams
SET user_teams.team_role_id = 1
WHERE user_teams.user_id = '108510356195224573040';

/*Капитан*/
UPDATE forumcalendar.user_teams
SET user_teams.team_role_id = 2
WHERE user_teams.user_id = '108510356195224573040';

/*Участник*/
UPDATE forumcalendar.user_teams
SET user_teams.team_role_id = 3
WHERE user_teams.user_id = '108510356195224573040';


SELECT * FROM forumcalendar.users;

/*Простой юзер*/
UPDATE forumcalendar.users 
SET users.role_id = 1
WHERE users.id='108510356195224573040';

/*Одмен*/
UPDATE forumcalendar.users 
SET users.role_id = 2
WHERE users.id='108510356195224573040';


SELECT * FROM forumcalendar.activities;

/*Стать создателем активити*/
UPDATE forumcalendar.activities
SET activities.user_id = '108510356195224573040'
WHERE activities.id = 1;


SELECT * FROM forumcalendar.activity_moderators;

truncate forumcalendar.activity_moderators;

/*Стать модером активити*/
insert into forumcalendar.activity_moderators (created_at,updated_at,user_id, activity_id)
value (now(),now(),'108510356195224573040',1);


SELECT * FROM forumcalendar.events_speakers;

/*Добавить событие спикеру*/
insert into forumcalendar.events_speakers (event_id ,speaker_id)
value (1,1);
