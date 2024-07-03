package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

	Film create(Film film);

	Film update(Film newFilm);

	Collection<Film> getAllFilms();

	Film getFilmById(long filmId);

	Collection<Film> getMostLiked(int size);
}
