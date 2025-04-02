create schema pro2;
create table user(
    username varchar(10) primary key,
    password_hash varchar,
    email varchar
);

create table player(
    username varchar(10) references user(username),
    balance int,
)

create table admin(
    username varchar(10 references user(username))
)