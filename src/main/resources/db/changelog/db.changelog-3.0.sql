--liquibase formatted sql

--changeset ikornienko:1
ALTER TABLE users
    ADD COLUMN image varchar(64);

--changeset ikornienko:2
ALTER TABLE users_aud
    ADD COLUMN image varchar(64);