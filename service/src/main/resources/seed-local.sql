-- Seed data for the "local" profile (in-memory H2 database), loaded at startup.
-- Provides one charge point (serial ACE0272306 / vendor Alfen) ready for the emulator,
-- and one REST user admin@local / admin (BCrypt, role ADMINISTRATOR).

INSERT INTO type_allowed (constructor, type) VALUES ('Alfen', 'EVE Single S-Line');

INSERT INTO firmware (url, version, constructor) VALUES ('ftp://fs.example/6.2.0.fwi', '6.2.0', 'Alfen');

INSERT INTO compatibility (id_firmware, id_type_allowed)
SELECT f.id_firmware, t.id_type_allowed
FROM firmware f, type_allowed t
WHERE f.version = '6.2.0' AND t.constructor = 'Alfen';

INSERT INTO configuration (name, description, configuration, id_firmware)
SELECT 'default', '', '{"1":"80"}', f.id_firmware FROM firmware f WHERE f.version = '6.2.0';

INSERT INTO chargepoint (serial_number_chargepoint, type, constructor, client_id, id_configuration)
SELECT 'ACE0272306', 'EVE Single S-Line', 'Alfen', 'client-1', c.id_configuration
FROM configuration c WHERE c.name = 'default';

-- Password for all three users is "admin" (BCrypt). One user per role, since the REST
-- endpoints check a single role (GET -> VISUALIZER, POST/PATCH -> EDITOR).
INSERT INTO app_user (email, lastname, firstname, password, role, is_deleted)
VALUES ('admin@local', 'Admin', 'Admin', '$2b$12$dWkGVgFsvQbtxKrVGpqO8.szRqITU8cU/63KbRFkFa2p0Q4Kco94G', 'ADMINISTRATOR', false);
INSERT INTO app_user (email, lastname, firstname, password, role, is_deleted)
VALUES ('editor@local', 'Editor', 'Editor', '$2b$12$dWkGVgFsvQbtxKrVGpqO8.szRqITU8cU/63KbRFkFa2p0Q4Kco94G', 'EDITOR', false);
INSERT INTO app_user (email, lastname, firstname, password, role, is_deleted)
VALUES ('viewer@local', 'Viewer', 'Viewer', '$2b$12$dWkGVgFsvQbtxKrVGpqO8.szRqITU8cU/63KbRFkFa2p0Q4Kco94G', 'VISUALIZER', false);
