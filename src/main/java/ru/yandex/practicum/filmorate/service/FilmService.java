package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmDbStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

@Service
public class FilmService {

	private final FilmDbStorage filmStorage;

	@Autowired
	public FilmService(FilmDbStorage filmStorage) {
		this.filmStorage = filmStorage;
	}

	public void likeFilm(long filmId, long userId) {
		filmStorage.likeFilm(filmId, userId);
	}

	public void removeLike(long filmId, long userId) {
		filmStorage.removeLike(filmId, userId);
	}

	public Collection<Film> getMostLiked(int size) {
		return filmStorage.getMostLiked(size);
	}

	public Film create(Film film) {
		return filmStorage.create(film);
	}

	public Film getFilmById(long id) {
		return filmStorage.getFilmById(id);
	}

	public Film update(Film film) {
		return filmStorage.update(film);
	}

	public Collection<Film> getAllFilms() {
		return filmStorage.getAllFilms();
	}


}
