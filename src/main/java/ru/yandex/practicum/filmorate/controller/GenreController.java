package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@RestController
public class GenreController {

	private final GenreService genreService;


	@Autowired
	public GenreController(GenreService genreService) {
		this.genreService = genreService;

	}

	@GetMapping("/genres")
	public Collection<Genre> getAllGenres() {
		log.info(genreService.getAllGenres().toString());
		return genreService.getAllGenres();
	}

	@GetMapping("/genres/{id}")
	public Optional<Genre> getGenreById(@PathVariable("id") int id) {
		log.info(genreService.getGenreById(id).toString());
		return genreService.getGenreById(id);
	}
}
