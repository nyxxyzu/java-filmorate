package ru.yandex.practicum.filmorate.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class GenreDbStorage extends BaseDbStorage<Genre> {

	@Autowired
	public GenreDbStorage(JdbcTemplate jdbc, ResultSetExtractor<List<Genre>> extractor) {
		super(jdbc, extractor);
	}

	private static final String GET_ALL_GENRES_QUERY = "SELECT * FROM genres";
	private static final String GET_GENRE_BY_ID_QUERY = "SELECT * FROM genres WHERE id = ?";

	public Collection<Genre> getAllGenres() {
		return findMany(GET_ALL_GENRES_QUERY);
	}

	public Optional<Genre> getGenreById(long genreId) {
		return findOne(GET_GENRE_BY_ID_QUERY, genreId);

	}

}
