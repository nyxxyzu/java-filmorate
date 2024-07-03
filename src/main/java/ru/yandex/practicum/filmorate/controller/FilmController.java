package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validationgroups.AdvanceInfo;
import ru.yandex.practicum.filmorate.validationgroups.BasicInfo;


import java.util.Collection;

@RestController
@Slf4j
public class FilmController {

	private final FilmService filmService;

	public FilmController(FilmService filmService) {
		this.filmService = filmService;
	}

	@GetMapping("/films")
	public Collection<Film> getAllFilms() {
		log.info(filmService.getAllFilms().toString());
		return filmService.getAllFilms();

	}

	@PostMapping("/films")
	public Film create(@Validated({BasicInfo.class, AdvanceInfo.class}) @RequestBody Film film) {
		log.info(film.toString());
		return filmService.create(film);

	}

	@PutMapping("/films")
	public Film update(@Validated(AdvanceInfo.class) @RequestBody Film newFilm) {
		log.info(newFilm.toString());
		return filmService.update(newFilm);
	}

	@GetMapping("/films/{id}")
	public Film getFilmById(@PathVariable("id") int id) {
		log.info(filmService.getFilmById(id).toString());
		return filmService.getFilmById(id);
	}

	@PutMapping("/films/{id}/like/{userId}")
	public void likeFilm(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
		filmService.likeFilm(filmId, userId);
	}

	@DeleteMapping("/films/{id}/like/{userId}")
	public void removeLike(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
		filmService.removeLike(filmId, userId);
	}

	@GetMapping("/films/popular")
	public Collection<Film> getMostLiked(@RequestParam(required = false, defaultValue = "10") int count) {
		log.info(filmService.getMostLiked(count).toString());
		return filmService.getMostLiked(count);
	}
}
