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
    id int
);

create table sep2.horse(
    id  serial primary key,
    name varchar,
    speedMin int,
    speedMax int
);

create table sep2.race(
    admin_id int references sep2.admin(id),
    id int,
    name varchar,
    status varchar,
    startTime time --creates time stamp for clock only, not full date

);

create table sep2.raceObserver(
    player_username varchar(10) references sep2.player(username),
    race_id int references sep2.race(race_id)

);

create table sep2.raceTrack(
    race_id int references sep2.race(id),
     name varchar,
     raceLength varchar,
     location varchar
);
