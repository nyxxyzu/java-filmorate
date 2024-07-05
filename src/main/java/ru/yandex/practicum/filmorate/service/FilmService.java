package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmDbStorage;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

@Service
public class FilmService {

	private final FilmDbStorage filmStorage;
	private static final LocalDate EARLIEST_DATE = LocalDate.parse("1895-12-25");

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
		return getAllFilms().stream()
				.sorted((f1, f2) -> f2.getLikedBy().size() - f1.getLikedBy().size())
				.limit(size)
				.toList();
	}

	public Film create(Film film) {
		if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(EARLIEST_DATE)) {
			throw new ValidationException("Дата релиза не должна быть раньше 28 декабря 1895 года");
		}
		try {
			return filmStorage.create(film);
		} catch (RuntimeException e) {
			throw new ValidationException("Ошибка валидации");
		}

	}

	public Optional<Film> getFilmById(long id) {
		return filmStorage.getFilmById(id);
	}

	public Film update(Film film) {
		try {
			return filmStorage.update(film);
		} catch (InternalServerException e) {
			throw new NotFoundException("Ошибка валидации");
		}
	}

	public Collection<Film> getAllFilms() {
		return filmStorage.getAllFilms();
	}


}
