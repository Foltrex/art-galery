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
INSERT INTO facility (name, is_active, address_id, organization_id)
VALUES ('HookahPlace Yakuba Kolasa', true, (SELECT id FROM address LIMIT 1),
       (SELECT id FROM organization LIMIT 1) );
INSERT INTO facility (name, is_active, address_id, organization_id)
VALUES ('Private House Bar', true, (SELECT id FROM address LIMIT 1),
       (SELECT id FROM organization LIMIT 1) );
INSERT INTO facility (name, is_active, address_id, organization_id)
VALUES ('Nuahule Krasnaya', true, (SELECT id FROM address LIMIT 1),
       (SELECT id FROM organization LIMIT 1) );
INSERT INTO facility (name, is_active, address_id, organization_id)
VALUES ('Dictator Bar', true, (SELECT id FROM address LIMIT 1),
       (SELECT id FROM organization LIMIT 1) );
INSERT INTO facility (name, is_active, address_id, organization_id)
VALUES ('TNT Rock Club', true, (SELECT id FROM address LIMIT 1),
       (SELECT id FROM organization LIMIT 1) );
INSERT INTO facility (name, is_active, address_id, organization_id)
VALUES ('Pub 82 on Dinamo', true, (SELECT id FROM address LIMIT 1),
       (SELECT id FROM organization LIMIT 1) );
INSERT INTO facility (name, is_active, address_id, organization_id)
VALUES ('Banki-Butylki', true, (SELECT id FROM address LIMIT 1),
       (SELECT id FROM organization LIMIT 1) );
INSERT INTO facility (name, is_active, address_id, organization_id)
VALUES ('MADMEN', true, (SELECT id FROM address LIMIT 1),
       (SELECT id FROM organization LIMIT 1) );
INSERT INTO facility (name, is_active, address_id, organization_id)
VALUES ('Bootlegger Bar', true, (SELECT id FROM address LIMIT 1),
       (SELECT id FROM organization LIMIT 1) );


-- organization role --
INSERT INTO organization_role (name) VALUES (0);
INSERT INTO organization_role (name) VALUES (1);
INSERT INTO organization_role (name) VALUES (2);


-- representative --
INSERT INTO representative (account_id, firstname, lastname, facility_id, organization_id, organization_role_id)
VALUES ('25fa19a6-6367-4fe1-a194-983fa28d24fc', 'Donald', 'Duck', (SELECT id FROM facility LIMIT 1), (SELECT id FROM organization LIMIT 1), (SELECT id FROM organization_role LIMIT 1));
INSERT INTO representative (account_id, firstname, lastname, facility_id, organization_id, organization_role_id)
VALUES ('42400536-1fd7-4072-8819-e6fb657fdde1', 'Donald', 'Trump', (SELECT id FROM facility LIMIT 1 OFFSET 1), (SELECT id FROM organization LIMIT 1), (SELECT id FROM organization_role LIMIT 1 OFFSET 1));
INSERT INTO representative (account_id, firstname, lastname, facility_id, organization_id, organization_role_id)
VALUES ('5a1a1ec6-cf8a-470e-b728-5a7dfb1a81e2', 'Donald', 'Tiktak', (SELECT id FROM facility LIMIT 1 OFFSET 2), (SELECT id FROM organization LIMIT 1), (SELECT id FROM organization_role LIMIT 1 OFFSET 2));
