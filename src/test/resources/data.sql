INSERT INTO type_allowed  (id_type_allowed, constructor, type) VALUES (1, 'Alfen', 'Eve Single S-line');
INSERT INTO type_allowed  (id_type_allowed, constructor, type) VALUES (2, 'Alfen', 'Eve Double S-line');

INSERT INTO firmware (id_firmware, url, version, constructor) VALUES (1, 'www.alfen5-4-3.com', '5.4.3-4073)', 'Alfen');
INSERT INTO firmware (id_firmware, url, version, constructor) VALUES (2, 'www.alfen-5-3-1.com', '5.3.1-8374)', 'Alfen');

INSERT INTO compatibility (id_firmware, id_type_allowed) VALUES (1, 1);
INSERT INTO compatibility (id_firmware, id_type_allowed) VALUES (1, 2);
INSERT INTO compatibility (id_firmware, id_type_allowed) VALUES (2, 1);
INSERT INTO compatibility (id_firmware, id_type_allowed) VALUES (2, 2);

INSERT INTO status (error) VALUES ('');
INSERT INTO status (error) VALUES ('');

INSERT INTO configuration (name, configuration) VALUES ('TestConfig','{}');
INSERT INTO configuration (name, configuration) VALUES ('TestConfig2','{}');

INSERT INTO chargepoint (serial_number_chargepoint, type, constructor, client_id, server_address, id_configuration, id_status, id_firmware) VALUES ('alfen_serial_number', 'mega-type', 'alfen', 'COMMUNIAU_1', 'localhost', 1, 1, 1);
INSERT INTO chargepoint (serial_number_chargepoint, type, constructor, client_id, server_address, id_configuration, id_status, id_firmware) VALUES ('alfen_serial_number2', 'mega-type', 'alfen', 'COSTANDINI_1', 'localhost', 2, 2, 2);