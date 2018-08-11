INSERT INTO roles (id, name, authority, created_at, updated_at) values
  (1, 'User',      'ROLE_USER',      NOW(), NOW()),
  (2, 'Superuser', 'ROLE_SUPERUSER', NOW(), NOW());

INSERT INTO team_roles (id, name, slug, created_at, updated_at) values
  (1, 'Curator', 'CURATOR', NOW(), NOW()),
  (2, 'Captain', 'CAPTAIN', NOW(), NOW()),
  (3, 'Member',  'MEMBER', NOW(), NOW());

INSERT INTO users (id, email, first_name, last_name, gender, locale, photo, role_id, created_at, updated_at) values
  (1, 'email@mail.ru', 'First name', 'Last name','Male','locale','fsdfa.jpg', 1, NOW(), NOW());
--
-- INSERT INTO activities (id, user_id, name, created_at, updated_at) values
--   (1, 1, 'FirstForum', NOW(), NOW());
-- --
-- INSERT INTO shifts (id, activity_id, name, start_date, end_date, created_at, updated_at) values
--   (1, 1, 'Shift1', NOW(), NOW(), NOW(), NOW());
--
-- INSERT INTO teams (id, shift_id, name, direction, description, created_at, updated_at) values
--   (1, 1, 'Team1', 'direction1','description', NOW(), NOW()),
--   (2, 1, 'Team2', 'direction2','description', NOW(), NOW());
--
-- INSERT INTO user_teams (user_id, team_id, team_role_id, created_at, updated_at) values
--   (1, 1, 1, NOW(), NOW());
-- --
-- INSERT INTO speakers (id, description, first_name, last_name, link, activity_id, created_at, updated_at) values
--   (1, 'description1', 'Jon', 'Potter' , 'link', 1, NOW(), NOW()),
--   (2, 'description2', 'Ron', 'Midfild', 'link', 1, NOW(), NOW());
