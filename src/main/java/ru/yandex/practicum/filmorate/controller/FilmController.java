package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validationgroups.AdvanceInfo;
import ru.yandex.practicum.filmorate.validationgroups.BasicInfo;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

	private final Map<Integer, Film> films = new HashMap<>();

	private int getNextId() {
		int currentMaxId = films.keySet()
				.stream()
				.max(Integer::compareTo)
				.orElse(0);
		return ++currentMaxId;
	}

	@GetMapping
	public Collection<Film> getAllFilms() {
		return films.values();
	}

	@PostMapping
	public Film create(@Validated({BasicInfo.class, AdvanceInfo.class}) @RequestBody Film film) {
		film.setId(getNextId());
		LocalDate earliestDate = LocalDate.parse("1895-12-25");
		if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(earliestDate)) {
			log.warn("Указана дата релиза фильма до 28.12.1895");
			throw new ValidationException("Дата релиза не должна быть раньше 28 декабря 1895 года");
		}
		films.put(film.getId(), film);
		log.info("Фильм добавлен");
		return film;
	}

	@PutMapping
	public Film update(@Validated(AdvanceInfo.class) @RequestBody Film newFilm) {
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
		throw new ValidationException("Пользователь с id = " + newFilm.getId() + " не найден");
	}
}
