DROP TABLE actor_film_role;
DROP TABLE director_film_assignment;
DROP TABLE film_genre;
DROP TABLE actor;
DROP TABLE director;
DROP TABLE genre;
DROP TABLE film;

CREATE TABLE actor (
actorId VARCHAR(25) PRIMARY KEY NOT NULL,
actorName VARCHAR(75) NOT NULL
);

CREATE TABLE film (
idIMDB VARCHAR(25) PRIMARY KEY NOT NULL,
title VARCHAR(75) NOT NULL,
plot TEXT,
year CHAR(4)
); 

CREATE TABLE actor_film_role(
actorId VARCHAR(25) REFERENCES actor(actorId),
idIMDB VARCHAR(25) REFERENCES film(idIMDB),
role VARCHAR(75)
);

CREATE TABLE director(
nameId VARCHAR(25) PRIMARY KEY NOT NULL,
name VARCHAR(75) NOT NULL
);

CREATE TABLE director_film_assignment(
directorId VARCHAR(25) REFERENCES director(nameId),
filmId VARCHAR(25) REFERENCES film(idIMDB)
);

CREATE TABLE genre(
name VARCHAR(25) PRIMARY KEY NOT NULL
);

CREATE TABLE film_genre(
genre VARCHAR(25) REFERENCES genre(name),
filmId VARCHAR(25) REFERENCES film(idIMDB)
);
