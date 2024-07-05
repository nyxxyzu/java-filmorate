package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {

	Film create(Film film);

	Film update(Film newFilm);

	Collection<Film> getAllFilms();

	Optional<Film> getFilmById(long filmId);

}
