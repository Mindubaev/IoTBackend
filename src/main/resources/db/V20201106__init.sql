/**
 * Author:  Artur
 * Created: 6 нояб. 2020 г.
 */
create table person (
    id serial primary key,
    username varchar(260),
    password varchar(260)
); 
create table setting (
    id serial primary key,
    minTemp numeric(3,2),
    maxTemp numeric(3,2),
    name varchar(20),
    person_id integer references person(id)
);
create table kettle (
    id serial primary key,
    name varchar(20),
    secret UUID,
    setting_id integer references setting(id) 
); 
create table kettle_person_relation (
    id serial primary key,
    kettle_id integer references kettle(id),
    person_id integer references person(id)
);
create table statistic (
    id serial primary key,
    temp numeric(3,2),
    date timestamp,
    kettle_id integer references kettle(id)
); 

