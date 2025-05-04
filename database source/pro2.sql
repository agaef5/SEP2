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
    id int references sep2.race(id),
    admin_id int references sep2.admin(id),
    name varchar,
    status varchar,
    startTime date --for full date or time for just timestamp
);

create table sep2.raceTrack(
    race_id int references sep2.race(id),
    name varchar,
    raceLength int,
   location varchar
);
