create table person (
    id serial primary key,
    username varchar(260),
    password varchar(260)
);
create table sensor (
    id serial primary key,
    name varchar(20),
    secret UUID,
    minTemp numeric(3,2),
    maxTemp numeric(3,2)
);
create table sensor_person_relation (
    id serial primary key,
    sensor_id integer references sensor(id),
    person_id integer references person(id)
);