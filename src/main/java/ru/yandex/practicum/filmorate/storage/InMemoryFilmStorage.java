package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {

	private final Map<Long, Film> films = new HashMap<>();

	private static final LocalDate EARLIEST_DATE = LocalDate.parse("1895-12-25");

	private long getNextId() {
		long currentMaxId = films.keySet()
				.stream()
				.max(Long::compareTo)
				.orElse(0L);
		return ++currentMaxId;
	}

	@Override
	public Film create(Film film) {
		film.setId(getNextId());
		if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(EARLIEST_DATE)) {
			throw new ValidationException("Дата релиза не должна быть раньше 28 декабря 1895 года");
		}
		films.put(film.getId(), film);
		return film;
	}

	@Override
	public Collection<Film> getAllFilms() {
		return films.values();
	}

	@Override
	public Film update(Film newFilm) {
		if (newFilm.getId() == null) {
			throw new ValidationException("Id должен быть указан");
		}
		if (films.containsKey(newFilm.getId())) {
			Film oldFilm = films.get(newFilm.getId());
			if (newFilm.getName() != null && !newFilm.getName().isEmpty()) {
				oldFilm.setName(newFilm.getName());
			}
			if (newFilm.getDescription() != null) {
				oldFilm.setDescription(newFilm.getDescription());
			}
			if (newFilm.getReleaseDate() != null) {
				oldFilm.setReleaseDate(newFilm.getReleaseDate());
			}
			if (newFilm.getDuration() != null) {
				oldFilm.setDuration(newFilm.getDuration());
			}
			return oldFilm;
		}
		throw new NotFoundException("Пользователь с id = " + newFilm.getId() + " не найден");
	}

	@Override
	public Film getFilmById(long filmId) {
		if (films.get(filmId) == null) {
			throw new NotFoundException("Такого фильма не существует.");
		}
		return films.get(filmId);
	}

	@Override
	public Collection<Film> getMostLiked(int size) {
		return films.values()
				.stream()
				.sorted((f1, f2) -> f2.getLikedBy().size() - f1.getLikedBy().size())
				.limit(size)
				.collect(Collectors.toList());
	}
}
