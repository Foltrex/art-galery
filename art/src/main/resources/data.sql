-- cities --
INSERT INTO city (name, latitude, longitude) VALUES ('Homel', 52.4313, 30.9937);
INSERT INTO city (name, latitude, longitude) VALUES ('Brest', 52.097622, 23.734051);
INSERT INTO city (name, latitude, longitude) VALUES ('Grodno', 53.893009, 27.567444);
INSERT INTO city (name, latitude, longitude) VALUES ('Vitsyebsk', 55.187222, 30.205116);
INSERT INTO city (name, latitude, longitude) VALUES ('Mogilev', 53.8981, 30.3325);
INSERT INTO city (name, latitude, longitude) VALUES ('Minsk', 53.9006, 27.5590);

-- addresses --
INSERT INTO address (city_id, street_name, street_number) VALUES ((SELECT * FROM city LIMIT 1), 'Bogdanovicha', 120);
INSERT INTO address (city_id, street_name, street_number) VALUES ((SELECT * FROM city LIMIT 1), 'Kulman', 111);
INSERT INTO address (city_id, street_name, street_number) VALUES ((SELECT * FROM city LIMIT 1), 'Hataevicha', 51);

-- facility --
INSERT INTO facility (name, is_active, address_id) VALUES ('Lidbeer', true, (SELECT * FROM address LIMIT 1));
INSERT INTO facility (name, is_active, address_id) VALUES ('Pinta', false, (SELECT * FROM address LIMIT 1));
INSERT INTO facility (name, is_active, address_id) VALUES ('Spichki', true, (SELECT * FROM address LIMIT 1));

