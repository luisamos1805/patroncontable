--liquibase formatted sql

--changeset juan:1
create table departamentos(
	id serial not null primary key,
	codigo varchar(50) not null,
	descripcion varchar(150) not null
);

--changeset juan:2
create table provincias(
	id serial not null primary key,
	codigo varchar(50) not null,
	descripcion varchar(150) not null,
	departamento_id integer not null references departamentos(id)
);

--changeset juan:3
create table distritos(
	id serial not null primary key,
	codigo varchar(50) not null,
	descripcion varchar(150) not null,
	provincia_id integer not null references provincias(id)
);
