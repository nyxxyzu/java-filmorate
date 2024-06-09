package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Service
public class FilmService {

	private final FilmStorage filmStorage;
	private final UserStorage userStorage;

	@Autowired
	public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
		this.filmStorage = filmStorage;
		this.userStorage = userStorage;
	}

	public Film likeFilm(int filmId, int userId) {
		Film film = filmStorage.getFilmById(filmId);
		if (film == null) {
			throw new NotFoundException("Фильм не найден");
		}
		User user = userStorage.getUserById(userId);
		if (user == null) {
			throw new NotFoundException("Пользователь не найден");
		}
		film.getLikedBy().add(userId);
		user.getLikedFilms().add(filmId);
		return film;
	}

	public Film removeLike(int filmId, int userId) {
		Film film = filmStorage.getFilmById(filmId);
		if (film == null) {
			throw new NotFoundException("Фильм не найден");
		}
		User user = userStorage.getUserById(userId);
		if (user == null) {
			throw new NotFoundException("Пользователь не найден");
		}
		film.getLikedBy().remove(userId);
		user.getLikedFilms().remove(filmId);
		return film;
	}

	public Collection<Film> getMostLiked(int size) {
		return filmStorage.getMostLiked(size);
	}

	public Film create(Film film) {
		return filmStorage.create(film);
	}

	public Film getFilmById(int id) {
		return filmStorage.getFilmById(id);
	}

	public Film update(Film film) {
		return filmStorage.update(film);
	}

	public Collection<Film> getAllFilms() {
		return filmStorage.getAllFilms();
	}


}
