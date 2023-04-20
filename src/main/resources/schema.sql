create table IF NOT EXISTS GENRE
(
    GENRE_ID   INTEGER           not null
        constraint GENRE_PK2
            unique,
    GENRE_NAME CHARACTER VARYING not null,
    constraint GENRE_PK
        primary key (GENRE_ID)
);

create table IF NOT EXISTS  FILM_GENRE
(
    FILM_ID  INTEGER not null,
    GENRE_ID INTEGER,
    constraint FILM_GENRE_PK
        primary key (FILM_ID),
    constraint "FILM_GENRE_GENRE_GENRE_ID_fk"
        foreign key (GENRE_ID) references GENRE (GENRE_ID)
);

create table IF NOT EXISTS USER_FRIENDS
(
    USER_ID           INTEGER not null
        constraint USER_FRIENDS_PK2
            unique,
    FRIEND_ID         INTEGER,
    FRIENDSHIP_STATUS BOOLEAN,
    constraint USER_FRIENDS_PK
        primary key (USER_ID)
);

create table IF NOT EXISTS FILMORATE_USER
(
    ID       INTEGER IDENTITY NOT NULL PRIMARY KEY,
    EMAIL    CHARACTER VARYING,
    LOGIN    CHARACTER VARYING,
    BIRTHDAY DATE NOT NULL,
    NAME     CHARACTER VARYING,

    constraint "USERS_USER_FRIENDS_USER_ID_fk"
        foreign key (ID) references USER_FRIENDS
);

create table IF NOT EXISTS FILM_LIKES
(
    FILM_ID      INTEGER not null,
    USER_ID_LIKE INTEGER,
    constraint FILM_LIKES_PK
        primary key (FILM_ID),
    constraint "FILM_LIKES_FILMORATE_USER_USER_ID_fk"
        foreign key (USER_ID_LIKE) references FILMORATE_USER
);

create table IF NOT EXISTS FILM
(
    FILM_ID           INTEGER not null
        constraint FILMS_PK2
            unique,
    FILM_NAME         CHARACTER VARYING,
    FILM_DESCRIPTION  CHARACTER VARYING,
    FILM_RELEASE_DATE DATE,
    FILM_DURATION     INTEGER,
    FILM_RATING       CHARACTER VARYING,
    constraint FILMS_PK
        primary key (FILM_ID),
    constraint "FILMS_FILM_GENRE_FILM_ID_fk"
        foreign key (FILM_ID) references FILM_GENRE,
    constraint "FILMS_FILM_LIKES_FILM_ID_fk"
        foreign key (FILM_ID) references FILM_LIKES
);

