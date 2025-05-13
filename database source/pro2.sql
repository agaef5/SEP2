CREATE SCHEMA sep2;
SET search_path TO sep2;


CREATE TABLE sep2.user(
    username VARCHAR(10) PRIMARY KEY,
    password_hash VARCHAR,
    email VARCHAR,
    isAdmin BIT
);

CREATE TABLE sep2.player(
    username VARCHAR(10) REFERENCES sep2.user(username),
    balance INT
);


CREATE TABLE sep2.horse(
    id SERIAL PRIMARY KEY,
    name VARCHAR,
    speedMin INT,
    speedMax INT
);

CREATE TABLE sep2.raceTrack(
    id SERIAL PRIMARY KEY,
    name VARCHAR,
    raceLength INT,
    location VARCHAR
);


CREATE TABLE sep2.race(
    id INT PRIMARY KEY,
    racetrack_id INT REFERENCES sep2.raceTrack(id),
    horse_id INT REFERENCES sep2.horse(id)
    name VARCHAR,
    startTime TIMESTAMP
);


INSERT INTO sep2.raceTrack (name, raceLength, location)
VALUES
    ('Midtown Madness', 1000, 'Aarhus'),
    ('Steelball Run', 10000, 'U.S.A.'),
    ('Dakar Rally', 2500, 'Sahara'),
    ('Dirt track', 3300, 'Sweden'),
    ('Ice track', 4000, 'Syberia'),
    ('Hot', 666, 'Hell');

