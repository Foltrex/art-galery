-- cities --
WITH first_city AS (
    SELECT * FROM city LIMIT 1;
)

INSERT INTO city (name, latitude, longitude) VALUES ('8fb22838-ba10-4e09-8d1f-fc7e45aae02d', 'Homel', 52.4313, 30.9937);
INSERT INTO city (name, latitude, longitude) VALUES ('cfffcd75-4773-46f2-b396-18353db760d7', 'Brest', 52.097622, 23.734051);
INSERT INTO city (name, latitude, longitude) VALUES ('f5873a34-f156-4fc9-9825-8f0810a21e39', 'Grodno', 53.893009, 27.567444);
INSERT INTO city (name, latitude, longitude) VALUES ('892a679c-7e72-4f06-9dad-35af40bfef84', 'Vitsyebsk', 55.187222, 30.205116);
INSERT INTO city (name, latitude, longitude) VALUES ('71aceea6-7442-4ea0-9800-ccee3662c509', 'Mogilev', 53.8981, 30.3325);
INSERT INTO city (name, latitude, longitude) VALUES ('ec152ee2-3c23-4962-b147-8deefc57329e', 'Minsk', 53.9006, 27.5590);

-- addresses --
WITH first_address AS (
    SELECT * FROM address LIMIT 1;
)

INSERT INTO address (city_id, street_name, street_number) VALUES ('ac11fd6c-52b0-40c1-ac33-52ab1008b9bc', (SELECT * FROM first_city), 'Bogdanovicha', 120);
INSERT INTO address (city_id, street_name, street_number) VALUES ('2a5bbbd4-4fed-4c23-82c8-df97a7bb7650', (SELECT * FROM first_city), 'Kulman', 111);
INSERT INTO address (city_id, street_name, street_number) VALUES ('bed31673-423b-4269-9e69-1be0b25c9130', (SELECT * FROM first_city), 'Hataevicha', 51);

-- facility --
INSERT INTO facility (name, is_active, address_id) VALUES ('d18962c7-1c2e-4819-a123-cc4923b68a73', 'Lidbeer', true, (SELECT * FROM first_address));
INSERT INTO facility (name, is_active, address_id) VALUES ('1ba8ce8c-8218-4836-8593-833cc3579e14', 'Pinta', false, (SELECT * FROM first_address));
INSERT INTO facility (name, is_active, address_id) VALUES ('a4c0e9e4-7a40-47a5-a5dc-7ba9f22bb1c4', 'Spichki', true, (SELECT * FROM first_address));

