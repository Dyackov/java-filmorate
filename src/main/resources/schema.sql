drop table if exists FILMS_GENRES cascade;

drop table if exists FRIENDS cascade;

drop table if exists GENRES cascade;

drop table if exists LIKES cascade;

drop table if exists REVIEWS_USERS cascade;

drop table if exists REVIEWS cascade;

drop table if exists FILMS cascade;

drop table if exists MPA_RATING cascade;

drop table if exists USERS cascade;

CREATE TABLE IF NOT EXISTS USERS
(
    USER_ID  BIGINT PRIMARY KEY AUTO_INCREMENT,
    EMAIL    VARCHAR NOT NULL,
    LOGIN    VARCHAR NOT NULL,
    NAME     VARCHAR,
    BIRTHDAY DATE
);

CREATE TABLE IF NOT EXISTS FRIENDS
(
    USER_ID  BIGINT REFERENCES USERS (USER_ID) ON DELETE CASCADE,
    FRIEND_ID BIGINT REFERENCES USERS (USER_ID) ON DELETE CASCADE,
    STATUS    ENUM ('CONFIRMED','UNCONFIRMED') DEFAULT 'UNCONFIRMED',
    CONSTRAINT FRIENDS UNIQUE (USER_ID, FRIEND_ID)
);

CREATE TABLE IF NOT EXISTS MPA_RATING
(
    RATING_ID   INT PRIMARY KEY,
    RATING_CODE VARCHAR
);

CREATE TABLE IF NOT EXISTS FILMS
(
    FILM_ID      BIGINT PRIMARY KEY AUTO_INCREMENT,
    NAME         VARCHAR NOT NULL,
    DESCRIPTION  VARCHAR(200),
    RELEASE_DATE DATE,
    DURATION     INT,
    RATING_ID    INT REFERENCES MPA_RATING (RATING_ID)
);

CREATE TABLE IF NOT EXISTS LIKES
(
    USER_ID BIGINT REFERENCES USERS (USER_ID) ON DELETE CASCADE,
    FILM_ID BIGINT REFERENCES FILMS (FILM_ID) ON DELETE CASCADE,
    CONSTRAINT LIKES UNIQUE (USER_ID, FILM_ID)
);

CREATE TABLE IF NOT EXISTS GENRES
(
    GENRE_ID INT PRIMARY KEY,
    NAME     VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS FILMS_GENRES
(
    FILM_ID  BIGINT REFERENCES FILMS (FILM_ID) ON DELETE CASCADE,
    GENRE_ID BIGINT REFERENCES GENRES (GENRE_ID),
    CONSTRAINT FILMS_GENRES UNIQUE (FILM_ID, GENRE_ID)
);

CREATE TABLE IF NOT EXISTS REVIEWS
(
    REVIEW_ID BIGINT PRIMARY KEY AUTO_INCREMENT,
    CONTENT VARCHAR,
    IS_POSITIVE BOOLEAN,
    USER_ID BIGINT REFERENCES USERS (USER_ID) ON DELETE CASCADE,
    FILM_ID BIGINT REFERENCES FILMS (FILM_ID) ON DELETE CASCADE,
    USEFUL INT

);

CREATE TABLE IF NOT EXISTS REVIEWS_USERS
(
    REVIEW_ID BIGINT REFERENCES REVIEWS (REVIEW_ID) ON DELETE CASCADE,
    USER_ID BIGINT REFERENCES USERS (USER_ID) ON DELETE CASCADE,
    IS_POSITIVE BOOLEAN,
    CONSTRAINT UNIQUE_REVIEWS_USERS UNIQUE (REVIEW_ID, USER_ID)
);

