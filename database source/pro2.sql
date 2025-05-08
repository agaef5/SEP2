create schema sep2;
set schema 'sep2';

create table sep2.user(
    username varchar(10) primary key,
    password_hash varchar,
    email varchar,
    role_id int
);

create table sep2.player(
    username varchar(10) references sep2.user(username),
    balance int
);

create table sep2.admin
(
    username varchar(10) references sep2.user(username),
    id int primary key
);

create table sep2.horse(
    id  serial primary key,
    name varchar,
    speedMin int,
    speedMax int
);


CREATE TABLE sep2.race (
    id INT PRIMARY KEY,
    admin_id INT REFERENCES sep2.admin(id),
    name VARCHAR,
    status VARCHAR,
    startTime DATE
);

CREATE TABLE sep2.raceTrack (
    race_id INT,
    name VARCHAR,
    raceLength INT,
    location VARCHAR
);

INSERT INTO sep2.raceTrack (race_id, name, raceLength, location)
VALUES (1, 'Midtown Madness', 1000, 'Aarhus'),
       (2, 'Steelball Run', 10000, 'U.S.A.'),
       (3, 'Dakar Rally', 2500, 'Sahara'),
       (4, 'Dirt track', 3300, 'Sweden'),
       (5, 'Ice track', 4000, 'Syberia'),
       (6, 'Hot', 666, 'Hell')

DROP TABLE IF EXISTS sep2.raceTrack;
DROP TABLE IF EXISTS sep2.race;
