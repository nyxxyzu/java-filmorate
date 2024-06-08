package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

	private final Map<Integer, Film> films = new HashMap<>();

	private static final LocalDate EARLIEST_DATE = LocalDate.parse("1895-12-25");

	private int getNextId() {
		int currentMaxId = films.keySet()
				.stream()
				.max(Integer::compareTo)
				.orElse(0);
		return ++currentMaxId;
	}

	@Override
	public Film create(Film film) {
		film.setId(getNextId());
		if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(EARLIEST_DATE)) {
			log.warn("Указана дата релиза фильма до 28.12.1895");
			throw new ValidationException("Дата релиза не должна быть раньше 28 декабря 1895 года");
		}
		films.put(film.getId(), film);
		log.info("Фильм добавлен");
		return film;
	}

	@Override
	public Collection<Film> getAllFilms() {
		return films.values();
	}

	@Override
	public Film update(Film newFilm) {
		if (newFilm.getId() == null) {
			log.warn("Не указан id");
			throw new ValidationException("Id должен быть указан");
		}
		if (films.containsKey(newFilm.getId())) {
			Film oldFilm = films.get(newFilm.getId());
			if (newFilm.getName() != null && !newFilm.getName().isEmpty()) {
				oldFilm.setName(newFilm.getName());
			}
			if (newFilm.getDescription() != null) {
				oldFilm.setDescription(newFilm.getDescription());
				log.info("Описание обновлено");
			}
			if (newFilm.getReleaseDate() != null) {
				oldFilm.setReleaseDate(newFilm.getReleaseDate());
				log.info("Дата релиза обновлена");
			}
			if (newFilm.getDuration() != null) {
				oldFilm.setDuration(newFilm.getDuration());
				log.info("Длительность обновлена");
			}
			return oldFilm;
		}
		log.warn("Указан несуществующий id");
		throw new NotFoundException("Пользователь с id = " + newFilm.getId() + " не найден");
	}

	@Override
	public Film getFilmById(int filmId) {
		if (films.get(filmId) == null) {
			throw new NotFoundException("Такого фильма не существует.");
		}
		return films.get(filmId);
	}
}
