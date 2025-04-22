create schema sep2;
set schema 'sep2';
create table sep2.user(
    username varchar(10) primary key,
    password_hash varchar,
    email varchar
);

create table sep2.player(
    username varchar(10) references sep2.user(username),
    balance int
);

create table sep2.admin
(
    username varchar(10) references sep2.user(username)
);

create table sep2.horse(
    id  serial primary key,
    name varchar,
    speedMin int,
    speedMax int
)