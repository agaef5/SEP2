CREATE SCHEMA sep2;
SET search_path TO sep2;


CREATE TABLE sep2.game_user(
    username VARCHAR(10) PRIMARY KEY,
    password_hash VARCHAR,
    email VARCHAR,
    isAdmin BOOLEAN,
    balance INT
);

CREATE TABLE sep2.raceTrack(
    id SERIAL PRIMARY KEY,
    name VARCHAR,
    raceLength INT,
    location VARCHAR
);

CREATE TABLE sep2.horse(
    id SERIAL PRIMARY KEY,
    name VARCHAR,
    speedMin INT,
    speedMax INT
);

CREATE TABLE sep2.race(
    id INT PRIMARY KEY,
    racetrack_id INT REFERENCES sep2.raceTrack(id),
    name VARCHAR,
    startTime TIMESTAMP
);

CREATE TABLE sep2.raceObserver(
    player_username VARCHAR REFERENCES sep2.game_user(username),
    race_id INT REFERENCES sep2.race(id),
    PRIMARY KEY (player_username, race_id)
);

CREATE TABLE sep2.participant(
    race_id INT REFERENCES sep2.race(id),
    horse_id INT REFERENCES sep2.horse(id),
    rank INT,
    PRIMARY KEY (race_id, horse_id)
);

CREATE TABLE sep2.bet(
    raceObserver_username VARCHAR,
    race_id INT,
    FOREIGN KEY (raceObserver_username,race_id) REFERENCES sep2.raceObserver(player_username, race_id),
    participant_id INT,
    FOREIGN KEY (race_id, participant_id) REFERENCES sep2.participant(race_id, horse_id),
    PRIMARY KEY (raceObserver_username, race_id, participant_id),
    betAmount INT,
    status BOOLEAN
);


INSERT INTO sep2.raceTrack (name, raceLength, location)
VALUES
    ('Midtown Madness', 100, 'Aarhus'),
    ('Steelball Run', 1000, 'U.S.A.'),
    ('Dakar Rally', 250, 'Sahara'),
    ('Dirt track', 330, 'Sweden'),
    ('Ice track', 400, 'Syberia'),
    ('Hot', 666, 'Hell');

INSERT INTO sep2.game_user (username, password_hash, email, isAdmin, balance)
VALUES ('admin', 'admin', 'admin@admin.com', TRUE, 0);
INSERT INTO sep2.game_user (username, password_hash, email, isAdmin, balance)
VALUES ('testuser', 'testuser', 'test@user.com', FALSE, 1000);

INSERT INTO sep2.horse (name, speedmin, speedmax)
VALUES ('Aga', 10, 20);
INSERT INTO sep2.horse (name, speedmin, speedmax)
VALUES ('Ash', 9, 16);
INSERT INTO sep2.horse (name, speedmin, speedmax)
VALUES ('Wilber', 15, 20);
INSERT INTO sep2.horse (name, speedmin, speedmax)
VALUES ('Samo', 15, 25);
INSERT INTO sep2.horse (name, speedmin, speedmax)
VALUES ('Allan', 9, 16);
INSERT INTO sep2.horse (name, speedmin, speedmax)
VALUES ('Joseph', 12, 17);
INSERT INTO sep2.horse (name, speedmin, speedmax)
VALUES ('Max', 18, 24);
INSERT INTO sep2.horse (name, speedmin, speedmax)
VALUES ('Farquaad', 15, 24);
INSERT INTO sep2.horse (name, speedmin, speedmax)
VALUES ('Lucky', 6, 29);
INSERT INTO sep2.horse (name, speedmin, speedmax)
VALUES ('Zeus', 19, 22);
INSERT INTO sep2.horse (name, speedmin, speedmax)
VALUES ('Unicorn', 21, 28);
INSERT INTO sep2.horse (name, speedmin, speedmax)
VALUES ('Black Cat', 15, 27);
INSERT INTO sep2.horse (name, speedmin, speedmax)
VALUES ('Spirit', 24, 27);
INSERT INTO sep2.horse (name, speedmin, speedmax)
VALUES ('Noob', 15, 25);
INSERT INTO sep2.horse (name, speedmin, speedmax)
VALUES ('Voldemort', 12, 23);
INSERT INTO sep2.horse (name, speedmin, speedmax)
VALUES ('Horse', 14, 19);
