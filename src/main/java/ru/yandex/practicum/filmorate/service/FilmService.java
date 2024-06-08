package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

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
		return filmStorage.getAllFilms()
				.stream()
				.sorted((f1, f2) -> f2.getLikedBy().size() - f1.getLikedBy().size())
				.limit(size)
				.collect(Collectors.toList());
	}


}
