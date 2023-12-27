DROP TABLE IF EXISTS film_genre;
DROP TABLE IF EXISTS film_likes;
DROP TABLE IF EXISTS film;
DROP TABLE IF EXISTS genre;
DROP TABLE IF EXISTS mpa;
DROP TABLE IF EXISTS REVIEW;
DROP TABLE IF EXISTS REVIEW_LIKES;
DROP TABLE IF EXISTS feed_event;
CREATE TABLE IF NOT EXISTS genre(id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, genre varchar);
CREATE TABLE IF NOT EXISTS mpa(id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, name_rate varchar);
CREATE TABLE IF NOT EXISTS film(film_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, name varchar, description varchar, release_data timestamp, duration integer, mpa integer REFERENCES mpa(id), rate integer);
CREATE TABLE IF NOT EXISTS film_genre(id_film INTEGER REFERENCES film (film_id), genre_id INTEGER REFERENCES genre(id));
CREATE TABLE IF NOT EXISTS user_filmorate(user_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, email varchar, login varchar, name varchar, birthday timestamp(8));
CREATE TABLE IF NOT EXISTS film_likes (id_film INTEGER REFERENCES film(film_id), id_user INTEGER REFERENCES user_filmorate(user_id));
CREATE TABLE IF NOT EXISTS relationship(id INTEGER REFERENCES user_filmorate(user_id), id_friend INTEGER REFERENCES user_filmorate(user_id));
CREATE TABLE IF NOT EXISTS feed_event (
    event_time TIMESTAMP, event_type varchar, event_operation varchar,
    user_id INTEGER NOT NULL,
    event_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    entity_id INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS REVIEW (
	REVIEW_ID INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	CONTENT CHARACTER VARYING(100),
	IS_POSITIVE BIT,
	USER_ID INTEGER NOT NULL,
	FILM_ID INTEGER NOT NULL,
	USEFUL INTEGER
);

CREATE TABLE IF NOT EXISTS REVIEW_LIKES (
	REVIEW_ID INTEGER,
	USER_ID INTEGER,
	IS_POSITIVE BIT
);