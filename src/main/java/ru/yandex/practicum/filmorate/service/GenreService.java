package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.GenreDbStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

@Service
public class GenreService {

	private final GenreDbStorage genreStorage;

	@Autowired
	public GenreService(GenreDbStorage genreStorage) {
		this.genreStorage = genreStorage;

	}

	public Collection<Genre> getAllGenres() {
		return genreStorage.getAllGenres();
	}

	public Genre getGenreById(long genreId) {
		return genreStorage.getGenreById(genreId);
	}
}
