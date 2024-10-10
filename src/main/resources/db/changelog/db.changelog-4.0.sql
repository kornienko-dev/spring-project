--liquibase formatted sql

--changeset ikornienko:1
ALTER TABLE users
    ADD COLUMN password varchar(128) DEFAULT '{noop}123';

--changeset ikornienko:2
ALTER TABLE users_aud
    ADD COLUMN password varchar(128);