INSERT INTO roles (id, name, authority, created_at, updated_at) VALUES
  (1, 'User',      'ROLE_USER',      NOW(), NOW()),
  (2, 'Superuser', 'ROLE_SUPERUSER', NOW(), NOW());

INSERT INTO team_roles (id, name, slug, created_at, updated_at) VALUES
  (1, 'Curator', 'CURATOR', NOW(), NOW()),
  (2, 'Captain', 'CAPTAIN', NOW(), NOW()),
  (3, 'Member',  'MEMBER',  NOW(), NOW());

INSERT INTO contact_types (id, name, created_at, updated_at) VALUES
  (1, 'ВКонтакте',     NOW(), NOW()),
  (2, 'Facebook',      NOW(), NOW()),
  (3, 'Instagram',     NOW(), NOW()),
  (4, 'Одноклассники', NOW(), NOW()),
  (5, 'Twitter',       NOW(), NOW()),
  (6, 'Google+',       NOW(), NOW());

INSERT INTO `users` (`id`, `created_at`, `updated_at`, `email`, `first_name`, `gender`, `last_name`, `locale`, `photo`, `role_id`) VALUES
('106725778006320436881', '2018-08-13 22:27:11', '2018-08-13 22:27:11', 'aliar@yandex.ru', 'Илья', NULL, 'Агеев', 'ru', '5b765ec5b596e916fe119f38afebdb2d.jpg', 2);

INSERT INTO `activities` (`id`, `created_at`, `updated_at`, `description`, `name`, `user_id`) VALUES
(1, '2018-08-14 15:55:19', '2018-08-14 15:55:19', NULL, 'ActivityMy', '106725778006320436881'),
(15, '2018-08-17 11:46:22', '2018-08-17 11:50:59', 'ActivityTestDesc', 'ActivityTestD', '106725778006320436881');

INSERT INTO `contacts` (`id`, `created_at`, `updated_at`, `link`, `contact_type_id`, `user_id`) VALUES
(1, '2018-08-14 22:04:45', '2018-08-15 01:00:11', 'https://vk.com/aliarfirst', 1, '106725778006320436881'),
(2, '2018-08-14 22:05:52', '2018-08-14 22:05:52', 'https://plus.google.com/u/0/106725778006320436881', 6, '106725778006320436881');


INSERT INTO `shifts` (`id`, `created_at`, `updated_at`, `description`, `end_date`, `name`, `start_date`, `activity_id`) VALUES
(2, '2018-08-14 15:55:19', '2018-08-14 15:55:19', NULL, '2018-08-18', 'ShiftFirst', '2018-08-15', 1),
(3, '2018-08-14 15:55:19', '2018-08-14 15:55:19', NULL, '2018-08-24', 'ShiftSecond', '2018-08-19', 1),
(16, '2018-08-17 11:46:22', '2018-08-17 12:28:52', 'ShiftFirstTestDesc', '2018-08-21', 'ShiftFirstTestD', '2018-08-20', 15),
(20, '2018-08-17 12:10:46', '2018-08-17 12:10:46', NULL, '2018-08-25', 'ShiftTest', '2018-08-24', 15);

INSERT INTO `speakers` (`id`, `created_at`, `updated_at`, `description`, `first_name`, `last_name`, `link`, `activity_id`) VALUES
(4, '2018-08-14 15:56:48', '2018-08-14 15:56:48', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc placerat ligula turpis, id consectetur justo tincidunt eget. Integer quis eleifend mi. Cras nec euismod tortor, id rutrum enim. Suspendisse ac condimentum lectus, sit amet interdum magna. Nunc quis consequat neque. Donec id tellus in sem scelerisque aliquam.', 'Pete', 'Huston', 'http://petehuston.com', 1),
(5, '2018-08-14 15:56:48', '2018-08-14 15:56:48', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc placerat ligula turpis, id consectetur justo tincidunt eget. Integer quis eleifend mi. Cras nec euismod tortor, id rutrum enim. Suspendisse ac condimentum lectus, sit amet interdum magna. Nunc quis consequat neque. Donec id tellus in sem scelerisque aliquam.', 'Pete II', 'Huston II', 'http://petehustonii.com', 1),
(21, '2018-08-17 12:24:08', '2018-08-17 12:24:08', 'SpeakerFTestDesc', 'SpeakerFTest', 'SpeakerLTest', 'SpeakerFTest.com', 15);

INSERT INTO `events` (`id`, `created_at`, `updated_at`, `start_datetime`, `end_datetime`, `description`, `name`, `place`, `shift_id`) VALUES
(7, '2018-08-14 15:59:16', '2018-08-14 15:59:16', '2018-08-15 09:00:00', '2018-08-15 10:00:00', 'EventDesc1', 'EventOneTwoOne', 'PlaceOne', 2),
(8, '2018-08-14 15:59:42', '2018-08-14 15:59:42', '2018-08-15 10:00:00', '2018-08-15 11:00:00', 'EventDesc2', 'EventOneTwoTwo', 'PlaceOne', 2),
(9, '2018-08-14 15:59:42', '2018-08-14 15:59:42', '2018-08-15 10:00:00', '2018-08-15 11:00:00', 'EventDesc3', 'Event3', 'PlaceOne', 2),
(10, '2018-08-14 15:59:42', '2018-08-14 15:59:42', '2018-08-16 10:00:00', '2018-08-15 11:00:00', 'EventDesc4', 'Event4', 'PlaceOne', 2),
(11, '2018-08-14 15:59:42', '2018-08-14 15:59:42', '2018-08-17 10:00:00', '2018-08-17 11:00:00', 'EventDesc2', 'EventOneTwoTwo', 'PlaceOne', 2),
(12, '2018-08-14 15:59:42', '2018-08-14 15:59:42', '2018-08-17 10:00:00', '2018-08-18 11:00:00', 'EventDesc2', 'Event10', 'PlaceOne', 2),
(13, '2018-08-14 15:59:42', '2018-08-14 15:59:42', '2018-08-16 10:00:00', '2018-08-17 11:00:00', 'EventDesc2', 'Event10', 'PlaceOne', 2),
(26, '2018-08-17 12:47:50', '2018-08-17 12:48:39', '2018-08-17 09:00:00', '2018-08-17 10:00:00', 'EventTestD', 'EventTestF', 'EventTestP', 16),
(28, '2018-08-17 22:29:57', '2018-08-17 22:29:57', '2018-08-18 14:00:00', '2018-08-18 15:00:00', 'Event1D', 'Event1', 'Event1P', 2);

INSERT INTO `events_speakers` (`event_id`, `speaker_id`) VALUES
(13, 4),
(28, 4),
(8, 5),
(13, 5),
(28, 5),
(13, 21);

INSERT INTO `subscriptions` (`created_at`, `updated_at`, `user_id`, `event_id`) VALUES
('2018-08-15 19:29:53', '2018-08-15 19:29:53', '106725778006320436881', 7),
('2018-08-15 19:29:59', '2018-08-15 19:29:59', '106725778006320436881', 8),
('2018-08-15 19:30:06', '2018-08-15 19:30:06', '106725778006320436881', 9),
('2018-08-15 19:30:11', '2018-08-15 19:30:11', '106725778006320436881', 10);

INSERT INTO `teams` (`id`, `created_at`, `updated_at`, `description`, `direction`, `name`, `number`, `shift_id`) VALUES
(5, '2018-08-14 15:57:14', '2018-08-14 15:57:14', 'TeamDescFirst', 'TeamDirFirst', 'TeamFirst', 0, 2),
(6, '2018-08-14 15:57:22', '2018-08-14 15:57:22', 'TeamDescFirstSecond', 'TeamDirFirstSecond', 'TeamSecond', 0, 2),
(22, '2018-08-17 12:37:00', '2018-08-17 12:37:04', 'TeamTestDescD', 'TeamTestDir', 'TeamTestD', 0, 16);

INSERT INTO `team_events` (`id`, `created_at`, `updated_at`, `start_datetime`, `end_datetime`, `description`, `place`, `name`, `team_id`) VALUES
(1, '2018-08-17 12:41:13', '2018-08-17 12:41:13', '2018-08-17 10:00:00', '2018-08-17 11:00:00', 'TeamEvTestD', 'TeamEvTestP', 'TeamEvTest', 5),
(2, '2018-08-17 12:41:13', '2018-08-17 12:41:13', '2018-08-17 11:00:00', '2018-08-17 12:00:00', 'TeamEvTestD', 'TeamEvTestP', 'TeamEvTest3', 5),
(3, '2018-08-17 12:41:13', '2018-08-17 12:41:13', '2018-08-17 12:00:00', '2018-08-17 13:00:00', 'TeamEvTestD', 'TeamEvTestP', 'TeamEvTest4', 5),
(4, '2018-08-17 12:41:13', '2018-08-17 12:41:13', '2018-08-17 12:00:00', '2018-08-17 13:00:00', 'TeamEvTestD', 'TeamEvTestP', 'TeamEvTest5', 5),
(5, '2018-08-17 12:41:13', '2018-08-17 12:41:13', '2018-08-17 12:00:00', '2018-08-17 14:00:00', 'TeamEvTestD', 'TeamEvTestP', 'TeamEvTest7', 5),
(6, '2018-08-17 12:41:13', '2018-08-17 12:41:13', '2018-08-17 13:00:00', '2018-08-17 14:00:00', 'TeamEvTestD', 'TeamEvTestP', 'TeamEvTest8', 5),
(7, '2018-08-17 12:41:13', '2018-08-17 12:41:13', '2018-08-18 13:00:00', '2018-08-18 14:00:00', 'TeamEvTestD', 'TeamEvTestP', 'TeamEvTest9', 5),
(8, '2018-08-17 12:41:13', '2018-08-17 12:41:13', '2018-08-16 13:00:00', '2018-08-16 14:00:00', 'TeamEvTestD', 'TeamEvTestP', 'TeamEvTest10', 5),
(9, '2018-08-17 12:41:13', '2018-08-17 12:41:13', '2018-08-16 22:00:00', '2018-08-17 01:00:00', 'TeamEvTestD', 'TeamEvTestP', 'TeamEvTest11', 5),
(10, '2018-08-17 12:41:13', '2018-08-17 12:41:13', '2018-08-17 23:00:00', '2018-08-18 00:00:00', 'TeamEvTestD', 'TeamEvTestP', 'TeamEvTest12', 5),
(23, '2018-08-17 12:41:13', '2018-08-17 12:41:13', '2018-08-17 09:00:00', '2018-08-17 10:00:00', 'TeamEvTestD', 'TeamEvTestP', 'TeamEvTest', 5),
(27, '2018-08-17 14:43:27', '2018-08-17 14:44:59', '2018-08-24 09:00:00', '2018-08-25 09:00:00', 'TeamEvTestD', 'TeamEvTestP', 'TeamEvTestD', 5);

INSERT INTO `user_teams` (`created_at`, `updated_at`, `user_id`, `team_id`, `team_role_id`) VALUES
('2018-08-19 18:04:51', '2018-08-19 18:04:51', '106725778006320436881', 5, 1);