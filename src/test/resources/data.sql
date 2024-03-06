INSERT INTO type_allowed  (constructor, type) VALUES ('Alfen', 'Eve Single S-line');
INSERT INTO type_allowed  (constructor, type) VALUES ('Alfen', 'Eve Double S-line');


INSERT INTO app_user (email, lastname, firstname, password, role, is_deleted) VALUES
('admin@email', 'adminLastName', 'adminFirstName', '$2y$10$EZYrNsMhqp.7diEC0ym5EOFuIFlBp05336PBwhPw5IHEUegDmSzJS', 'ADMINISTRATOR', 0),
('editor@email', 'editorLastName', 'editorFirstName', '$2y$10$EZYrNsMhqp.7diEC0ym5EOFuIFlBp05336PBwhPw5IHEUegDmSzJS', 'EDITOR', 0),
('visualizer@email', 'visualizerLastName', 'visualizerFirstName', '$2y$10$EZYrNsMhqp.7diEC0ym5EOFuIFlBp05336PBwhPw5IHEUegDmSzJS', 'VISUALIZER', 0),
('random@notEmail', 'randomLastName', 'randomFirstName', '$2y$10$EZYrNsMhqp.7diEC0ym5EOFuIFlBp05336PBwhPw5IHEUegDmSzJS', 'VISUALIZER', 0);
