CREATE TABLE IF NOT EXISTS users (
            id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
            email VARCHAR(255) NOT NULL,
            login VARCHAR(100) NOT NULL,
            name VARCHAR(100),
            birthday DATE
          );

CREATE TABLE IF NOT EXISTS mpa (
            id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
            mpa_name VARCHAR(100) NOT NULL
          );

CREATE TABLE IF NOT EXISTS films (
            id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
            name VARCHAR(255) NOT NULL,
            description VARCHAR(200) NOT NULL,
            releaseDate DATE,
            duration INTEGER,
            mpa INTEGER REFERENCES mpa(id)
          );

CREATE TABLE IF NOT EXISTS friends (
            user_id INTEGER NOT NULL REFERENCES users(id),
            friend_id INTEGER NOT NULL REFERENCES users(id),
            accepted BOOLEAN NOT NULL DEFAULT 0
          );

CREATE TABLE IF NOT EXISTS likes (
            user_id INTEGER NOT NULL REFERENCES users(id),
            film_id INTEGER NOT NULL REFERENCES films(id)
          );

CREATE TABLE IF NOT EXISTS genres (
            id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
            genre_name VARCHAR(100) NOT NULL
          );

CREATE TABLE IF NOT EXISTS film_genre (
            film_id INTEGER NOT NULL REFERENCES films(id),
            genre_id INTEGER NOT NULL REFERENCES genres(id)
          );




