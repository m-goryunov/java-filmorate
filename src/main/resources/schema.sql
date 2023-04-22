CREATE SCHEMA IF NOT EXISTS "PUBLIC";

CREATE TABLE IF NOT EXISTS FILM
(
    ID           IDENTITY NOT NULL PRIMARY KEY,
    RATING_ID    INTEGER,
    NAME         VARCHAR  NOT NULL,
    DESCRIPTION  VARCHAR,
    DURATION     INTEGER,
    RELEASE_DATE DATE,
    CONSTRAINT FILM_ID
        PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS RATING
(
    RATING_ID   INTEGER NOT NULL PRIMARY KEY,
    RATING_NAME VARCHAR,
    CONSTRAINT RATING_PK
        PRIMARY KEY (RATING_ID)
);

alter table FILM
    ADD CONSTRAINT IF NOT EXISTS FILM_RATING___FK
        FOREIGN KEY (RATING_ID) REFERENCES RATING
            on update cascade on delete set null;



create table IF NOT EXISTS USER_FILMORATE
(
    ID       IDENTITY NOT NULL PRIMARY KEY,
    EMAIL    VARCHAR,
    LOGIN    VARCHAR,
    BIRTHDAY DATE     NOT NULL,
    NAME     CHARACTER VARYING,
    constraint USER_ID
        primary key (ID)
);

create table IF NOT EXISTS GENRE
(
    ID   IDENTITY NOT NULL PRIMARY KEY,
    NAME varchar,
    constraint GENRE_PK
        primary key (ID)
);

ALTER TABLE GENRE
    ALTER COLUMN ID RESTART WITH 1;

create table IF NOT EXISTS FILM_GENRE
(
    GENRE_ID INTEGER,
    FILM_ID  INTEGER,
    constraint film_genre_pk
        unique (FILM_ID, GENRE_ID),
    constraint "film_genre_FILM_ID_fk"
        foreign key (FILM_ID) references FILM,
    constraint "film_genre_GENRE_id_fk"
        foreign key (FILM_ID) references GENRE
);

create table if not exists USER_FRIENDS
(
    USER_ID   INTEGER,
    FRIEND_ID INTEGER,
    constraint "friend_FILMORATE_USER_ID_fk"
        foreign key (USER_ID) references USER_FILMORATE,
    constraint "friend_FILMORATE_USER_ID_fk2"
        foreign key (FRIEND_ID) references USER_FILMORATE
);

create table if not exists FILM_LIKES
(
    FILM_ID      INTEGER NOT NULL,
    USER_ID_LIKE INTEGER NOT NULL,
    constraint "user_film_likes_FILMORATE_USER_ID_fk"
        foreign key (USER_ID_LIKE) references USER_FILMORATE,
    constraint "user_film_likes_FILM_ID_fk"
        foreign key (FILM_ID) references FILM
);