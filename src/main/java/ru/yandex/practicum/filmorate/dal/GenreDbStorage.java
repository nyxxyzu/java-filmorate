package ru.yandex.practicum.filmorate.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;

@Repository
public class GenreDbStorage extends BaseDbStorage<Genre> {

	@Autowired
	public GenreDbStorage(JdbcTemplate jdbc, ResultSetExtractor<List<Genre>> extractor) {
		super(jdbc, extractor, Genre.class);
	}

	private static final String GET_ALL_GENRES_QUERY = "SELECT * FROM genres";
	private static final String GET_GENRE_BY_ID_QUERY = "SELECT * FROM genres WHERE id = ?";

	public Collection<Genre> getAllGenres() {
		return findMany(GET_ALL_GENRES_QUERY);
	}

	public Genre getGenreById(long genreId) {
		try {
			return findOne(GET_GENRE_BY_ID_QUERY, genreId);
		} catch (RuntimeException e) {
			throw new NotFoundException("ID жанра не найден");
		}
	}

}
