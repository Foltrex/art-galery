-- cities --
INSERT INTO city (name, latitude, longitude)
VALUES ('Homel', 52.4313, 30.9937);
INSERT INTO city (name, latitude, longitude)
VALUES ('Brest', 52.097622, 23.734051);
INSERT INTO city (name, latitude, longitude)
VALUES ('Grodno', 53.893009, 27.567444);
INSERT INTO city (name, latitude, longitude)
VALUES ('Vitsyebsk', 55.187222, 30.205116);
INSERT INTO city (name, latitude, longitude)
VALUES ('Mogilev', 53.8981, 30.3325);
INSERT INTO city (name, latitude, longitude)
VALUES ('Minsk', 53.9006, 27.5590);

-- addresses --
INSERT INTO address (city_id, full_name)
VALUES ((SELECT id FROM city LIMIT 1), 'Bogdanovicha');
INSERT INTO address (city_id, full_name)
VALUES ((SELECT id FROM city LIMIT 1 OFFSET 1), 'Kulman');
INSERT INTO address (city_id, full_name)
VALUES ((SELECT id FROM city LIMIT 1 OFFSET 2), 'Hataevicha');

-- organization --
INSERT INTO organization (name, status, address_id)
VALUES ('Roga and Kopita', 1, (SELECT id FROM address LIMIT 1 OFFSET 1));
INSERT INTO organization (name, status, address_id)
VALUES ('Miaso i salo', 0, (SELECT id FROM address LIMIT 1 OFFSET 2));

-- facility --
INSERT INTO facility (name, is_active, address_id, organization_id)
VALUES ('Lidbeer', true, (SELECT id FROM address LIMIT 1),
       (SELECT id FROM organization LIMIT 1) );
INSERT INTO facility (name, is_active, address_id, organization_id)
VALUES ('Pinta', false, (SELECT id FROM address LIMIT 1),
       (SELECT id FROM organization LIMIT 1) );
INSERT INTO facility (name, is_active, address_id, organization_id)
VALUES ('Spichki', true, (SELECT id FROM address LIMIT 1),
       (SELECT id FROM organization LIMIT 1) );


-- organization role --
INSERT INTO organization_role (name) VALUES (0);
INSERT INTO organization_role (name) VALUES (1);
INSERT INTO organization_role (name) VALUES (2);


-- representative --
INSERT INTO representative (account_id, firstname, lastname, facility_id, organization_id, organization_role_id)
VALUES ('6b223764-491e-484b-ad89-93280fdaafaa', 'Donald', 'Duck', (SELECT id FROM facility LIMIT 1), (SELECT id FROM organization LIMIT 1), (SELECT id FROM organization_role LIMIT 1));
INSERT INTO representative (account_id, firstname, lastname, facility_id, organization_id, organization_role_id)
VALUES ('a98b17a0-ff58-4473-b514-55b0afeb62e8', 'Donald', 'Trump', (SELECT id FROM facility LIMIT 1 OFFSET 1), (SELECT id FROM organization LIMIT 1), (SELECT id FROM organization_role LIMIT 1 OFFSET 1));
INSERT INTO representative (account_id, firstname, lastname, facility_id, organization_id, organization_role_id)
VALUES ('d6a4df13-14ed-47b4-a331-af9f37ef713d', 'Donald', 'Tiktak', (SELECT id FROM facility LIMIT 1 OFFSET 2), (SELECT id FROM organization LIMIT 1), (SELECT id FROM organization_role LIMIT 1 OFFSET 2));
