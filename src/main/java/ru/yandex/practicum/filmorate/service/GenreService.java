package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.GenreDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

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

	public Optional<Genre> getGenreById(long genreId) {
		try {
			return genreStorage.getGenreById(genreId);
		} catch (RuntimeException e) {
			throw new NotFoundException("ID жанра не найден");
		}
	}
}
