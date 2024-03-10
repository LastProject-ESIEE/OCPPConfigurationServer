INSERT INTO app_user (email, lastname, firstname, password, role, is_deleted) VALUES
('admin@email', 'adminLastName', 'adminFirstName', '$2y$10$EZYrNsMhqp.7diEC0ym5EOFuIFlBp05336PBwhPw5IHEUegDmSzJS', 'ADMINISTRATOR', 0),
('editor@email', 'editorLastName', 'editorFirstName', '$2y$10$EZYrNsMhqp.7diEC0ym5EOFuIFlBp05336PBwhPw5IHEUegDmSzJS', 'EDITOR', 0),
('visualizer@email', 'visualizerLastName', 'visualizerFirstName', '$2y$10$EZYrNsMhqp.7diEC0ym5EOFuIFlBp05336PBwhPw5IHEUegDmSzJS', 'VISUALIZER', 0),
('random@notEmail', 'randomLastName', 'randomFirstName', '$2y$10$EZYrNsMhqp.7diEC0ym5EOFuIFlBp05336PBwhPw5IHEUegDmSzJS', 'VISUALIZER', 0),
('deleted@notEmail', 'randomLastName', 'randomFirstName', '$2y$10$EZYrNsMhqp.7diEC0ym5EOFuIFlBp05336PBwhPw5IHEUegDmSzJS', 'VISUALIZER', 1);

INSERT INTO firmware (url, version, constructor) VALUES
('https://lienFirmware1', '6.1.1-4160',  'Alfen BV'),
('https://lienFirmware2', '5.8.1-4123',  'Alfen BV'),
('https://lienFirmware3', '6.5.0-4217',  'Alfen BV');

INSERT INTO type_allowed  (constructor, type) VALUES
('Alfen BV', 'Eve Single S-line'),
('Alfen BV', 'Eve Double S-line');

INSERT INTO compatibility (id_firmware, id_type_allowed) VALUES
(2, 1),
(2, 2),
(3, 1),
(3, 2);

INSERT INTO configuration (name, description, configuration, last_edit, id_firmware) VALUES
('configuration intensité de la LED', 'Description config intensité de la LED', '{"1":"100"}', '2024-02-25 15:12:09', 1),
('configuration bonne année 2024', 'Description config 2024', '{"1":"100","3":"Borne-Test","4":"true","5":"20"}', '2024-01-01 00:00:00', 3),
('configuration vide', '', '{}', '2024-02-20 15:12:09', 2),
('configuration copropriété #2', 'Description config copro #2', '{"1":"0","5":"500"}', '2024-02-21 15:12:09', null),
('configuration voisins', 'Description config voisins', '{"1":"100"}', '2023-12-25 22:31:34', 1);

INSERT INTO chargepoint (serial_number_chargepoint, type, constructor, client_id, server_address, id_configuration, last_update, error, state, step, step_status) VALUES
('ACE0000001', 'Eve Single S-line', 'Alfen BV', 'borne to be alive', 'https://server_address', null, '2024-03-08 10:34:09', '', 1, 'CONFIGURATION', 'PENDING'),
('ACE0000002', 'Eve Double S-line', 'Alfen BV', 'dépasse les bornes', 'https://server_address', 2, '2024-03-08 10:34:09', '', 0, 'CONFIGURATION', 'PROCESSING'),
('ACE0000003', 'Eve Single S-line', 'Alfen BV', 'borne de test', 'https://server_address', 1, '2024-03-08 10:34:09', 'erreur de test', 1, 'CONFIGURATION', 'FINISHED'),
('ACE0000004', 'Eve Double S-line', 'Alfen BV', 'elisabeth borne', 'https://server_address', 3, '2024-03-08 10:34:09', '', 1, 'CONFIGURATION', 'FAILED'),
('ACE0000005', 'Eve Double S-line', 'Alfen BV', 'nom de la borne', 'https://server_address', 5, '2024-03-08 10:34:09', '', 0, 'FIRMWARE', 'PENDING'),
('ACE0000005', 'Eve Single S-line', 'Alfen BV', 'born to kill', 'https://server_address', 5, '2024-03-08 10:34:09', '', 0, 'FIRMWARE', 'PROCESSING'),
('ACE0000006', 'Eve Double S-line', 'Alfen BV', 'stéphane borne (l''historien)', 'https://server_address', 4, '2024-03-08 10:34:09', '', 1, 'FIRMWARE', 'FINISHED'),
('ACE0000007', 'Eve Single S-line', 'Alfen BV', 'les bornés', 'https://server_address', 2, '2024-03-08 10:34:09', '', 1, 'FIRMWARE', 'FAILED');

INSERT INTO business_logs (date, complete_log, chargepoint_id, user_id, category, level) VALUES
('2024-03-08 13:00:00', 'Un utilisateur s''est connecté', null, 1, 'LOGIN', 'INFO'),
('2024-03-08 11:00:00', 'log qui indique un message très important', 2, 1, 'CONFIG', 'ERROR'),
('2024-03-08 12:00:00', 'log qui indique un message fatal', 1, 2, 'FIRM', 'FATAL'),
('2024-03-08 10:00:00', 'log qui indique un message d''info', 4, 2, 'FIRM', 'INFO');

INSERT INTO technical_logs (date, component, level, complete_log) VALUES
('2024-03-08 13:00:00', 'BACKEND', 'INFO', 'log technique de backend'),
('2024-03-08 11:00:00', 'WEBSOCKET', 'ERROR', 'log erreur technique de websocket'),
('2024-03-08 12:00:00', 'FRONTEND', 'INFO', 'log technique de frontend'),
('2024-03-08 10:00:00', 'DATABASE', 'INFO', 'log technique de database');
