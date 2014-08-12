--liquibase formatted sql

--changeset jose:1
CREATE TABLE users
(
  username character varying(50) NOT NULL PRIMARY KEY,
  password text NOT NULL,
  enabled boolean NOT NULL DEFAULT true
);

--changeset jose:2
CREATE TABLE user_roles
(
  username character varying(50) NOT NULL REFERENCES users(username),
  role_name character varying(250) NOT NULL,
  CONSTRAINT user_roles_pkey PRIMARY KEY (username, role_name)
)

--changeset jose:3
insert into users values ('joseluis','$shiro1$SHA-256$500000$2EV56Ke+WaW5tOq+XYuQqQ==$9ZqWozl7vreW1gC33ew0Yx5HrUIEkFU5ahaj8qZV09k=',true)

--changeset jose:4
CREATE TABLE cuentas
(
    id serial not null primary key,
	cuenta varchar(2) not null unique,
	descripcion varchar(250) not null
  
);