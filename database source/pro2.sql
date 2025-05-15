CREATE SCHEMA sep2;
SET search_path TO sep2;


CREATE TABLE sep2.game_user(
    username VARCHAR(10) PRIMARY KEY,
    password_hash VARCHAR,
    email VARCHAR,
    isAdmin BOOLEAN,
    balance INT
);

CREATE TABLE sep2.raceObserver(
    player_username REFERENCES sep2.game_user(username),
    race_id REFERENCES sep2.race(id),
    id INT  PRIMARY KEY
)

CREATE TABLE sep2.bet(
    id INT  PRIMARY KEY
    raceObserver_id REFERENCES sep2.raceObserver(id),
    race_id REFERENCES sep2.race(id),
    participant_id REFERENCES sep2.participant(id),
    betAmount INT,
    status BOOLEAN
)


CREATE TABLE sep2.horse(
    id SERIAL PRIMARY KEY,
    name VARCHAR,
    speedMin INT,
    speedMax INT
);

CREATE TABLE sep2.raceTrack(
    id INT PRIMARY KEY, --just added, was missing
    race_id SERIAL PRIMARY KEY,
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

CREATE TABLE sep2.participant(
    id INT PRIMARY KEY,
    race_id INT REFERENCES sep2.race(id),
    horse_id INT REFERENCES sep2.horse(id),
    rank INT
);


INSERT INTO sep2.raceTrack (name, raceLength, location)
VALUES
    ('Midtown Madness', 1000, 'Aarhus'),
    ('Steelball Run', 10000, 'U.S.A.'),
    ('Dakar Rally', 2500, 'Sahara'),
    ('Dirt track', 3300, 'Sweden'),
    ('Ice track', 4000, 'Syberia'),
    ('Hot', 666, 'Hell');

INSERT INTO sep2.game_user (username, password_hash, email, isAdmin, balance)
VALUES ('admin', 'admin', 'admin@admin.com', TRUE, 0)
INSERT INTO sep2.game_user (username, password_hash, email, isAdmin, balance)
VALUES ('testuser', 'testuser', 'test@user.com', FALSE, 1000);