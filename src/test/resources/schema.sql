DROP SCHEMA IF EXISTS project CASCADE;

CREATE SCHEMA project;

DROP TABLE IF EXISTS tcap_dnamutant;

CREATE TABLE tcap_dnamutant (
    id_mutant VARCHAR(255) PRIMARY KEY,
    is_mutant BOOLEAN
);