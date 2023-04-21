create table IF NOT EXISTS FILM
(
    ID           IDENTITY NOT NULL PRIMARY KEY,
    RATING_ID    INTEGER,
    NAME         VARCHAR NOT NULL,
    DESCRIPTION  TEXT,
    DURATION     INTEGER,
    RELEASE_DATE DATE,
    constraint FILM_ID
        primary key (ID)
);

create table IF NOT EXISTS RATING
(
    ID   INTEGER NOT NULL PRIMARY KEY,
    NAME VARCHAR,
    constraint rating_pk
        primary key (ID)
);

--ALTER TABLE RATING ALTER COLUMN ID RESTART WITH 1;

alter table FILM
    alter column ID BIGINT auto_increment;

alter table FILM
    add constraint if not exists FILM_RATING___FK
        foreign key (RATING_ID) references RATING
            on update cascade on delete set null;



create table IF NOT EXISTS USER_FILMORATE
(
    ID       IDENTITY NOT NULL PRIMARY KEY,
    EMAIL    VARCHAR,
    LOGIN    VARCHAR,
    BIRTHDAY DATE NOT NULL,
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

ALTER TABLE GENRE ALTER COLUMN ID RESTART WITH 1;

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
    USER_ID INTEGER,
    FRIEND_ID  INTEGER,
    constraint "friend_FILMORATE_USER_ID_fk"
        foreign key (USER_ID) references USER_FILMORATE,
    constraint "friend_FILMORATE_USER_ID_fk2"
        foreign key (FRIEND_ID) references USER_FILMORATE
);

create table if not exists FILM_LIKES
(
    FILM_ID INTEGER NOT NULL,
    USER_ID_LIKE LONG NOT NULL,
    constraint "user_film_likes_FILMORATE_USER_ID_fk"
        foreign key (USER_ID_LIKE) references USER_FILMORATE,
    constraint "user_film_likes_FILM_ID_fk"
        foreign key (FILM_ID) references FILM
);

