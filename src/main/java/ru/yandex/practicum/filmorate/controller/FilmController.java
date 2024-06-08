package ru.yandex.practicum.filmorate.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.validationgroups.AdvanceInfo;
import ru.yandex.practicum.filmorate.validationgroups.BasicInfo;


import java.util.Collection;

@RestController
public class FilmController {

	private final FilmService filmService;
	private final FilmStorage filmStorage;

	public FilmController(FilmService filmService, FilmStorage filmStorage) {
		this.filmService = filmService;
		this.filmStorage = filmStorage;
	}

	@GetMapping("/films")
	public Collection<Film> getAllFilms() {
		return filmStorage.getAllFilms();

	}

	@PostMapping("/films")
	public Film create(@Validated({BasicInfo.class, AdvanceInfo.class}) @RequestBody Film film) {
		return filmStorage.create(film);

	}

	@PutMapping("/films")
	public Film update(@Validated(AdvanceInfo.class) @RequestBody Film newFilm) {
		return filmStorage.update(newFilm);
	}

	@GetMapping("/films/{id}")
	public Film getFilmById(@PathVariable("id") int id) {
		return filmStorage.getFilmById(id);
	}

	@PutMapping("/films/{id}/like/{userId}")
	public Film likeFilm(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
		return filmService.likeFilm(filmId, userId);
	}

	@DeleteMapping("/films/{id}/like/{userId}")
	public Film removeLike(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
		return filmService.removeLike(filmId, userId);
	}

	@GetMapping("/films/popular")
	public Collection<Film> getMostLiked(@RequestParam(required = false, defaultValue = "10") int count) {
		return filmService.getMostLiked(count);
	}
}
