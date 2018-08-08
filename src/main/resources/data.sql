INSERT INTO roles (id, name, authority, created_at, updated_at) values
  (1, 'User',      'USER',      NOW(), NOW()),
  (2, 'Superuser', 'SUPERUSER', NOW(), NOW());

INSERT INTO team_roles (id, name, slug, created_at, updated_at) values
  (1, 'Curator', 'CURATOR', NOW(), NOW()),
  (2, 'Captain', 'CAPTAIN', NOW(), NOW()),
  (3, 'Member',  'MEMBER', NOW(), NOW());