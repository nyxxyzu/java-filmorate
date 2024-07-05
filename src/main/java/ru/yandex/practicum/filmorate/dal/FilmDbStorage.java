package ru.yandex.practicum.filmorate.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {

	private static final String ADD_FILM_QUERY = "INSERT INTO films(name, description, releaseDate, duration, mpa) VALUES (?, ?, ?, ?, ?)";
	private static final String ADD_GENRE_QUERY = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
	private static final String GET_ALL_FILMS_QUERY = "SELECT * FROM films AS f LEFT JOIN film_genre AS fg ON f.id = fg.film_id " +
			"LEFT JOIN genres AS g on fg.genre_id = g.id LEFT JOIN mpa AS m ON f.mpa = m.id LEFT JOIN likes AS l ON f.id = l.film_id";
	private static final String GET_FILM_BY_ID_QUERY = "SELECT * FROM films AS f LEFT JOIN film_genre AS fg ON f.id = fg.film_id " +
			"LEFT JOIN genres AS g on fg.genre_id = g.id LEFT JOIN mpa AS m ON f.mpa = m.id LEFT JOIN likes AS l ON f.id = l.film_id " +
			"WHERE f.id = ?";
	private static final String UPDATE_FILM_QUERY = "UPDATE films SET name = ?, description = ?, releaseDate = ?, duration = ?, " +
			"mpa = ? WHERE id = ?";
	private static final String DELETE_GENRE_QUERY = "DELETE FROM film_genre WHERE film_id = ?";
	private static final String LIKE_FILM_QUERY = "INSERT INTO likes (user_id, film_id) SELECT ?, ? " +
			"WHERE NOT EXISTS (SELECT * FROM likes WHERE user_id = ? AND film_id = ?)";
	private static final String REMOVE_LIKE_QUERY = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";

	@Autowired
	public FilmDbStorage(JdbcTemplate jdbc, ResultSetExtractor<List<Film>> extractor) {
		super(jdbc, extractor);
	}

	public void likeFilm(long filmId, long userId) {
		noPkInsert(
				LIKE_FILM_QUERY,
				userId,
				filmId,
				userId,
				filmId
		);
	}

	public void removeLike(long filmId, long userId) {
		delete(
				REMOVE_LIKE_QUERY,
				filmId,
				userId
		);
	}

	@Override
	public Film create(Film film) {
		long id = insert(
				ADD_FILM_QUERY,
				film.getName(),
				film.getDescription(),
				Date.valueOf(film.getReleaseDate()),
				film.getDuration().toMinutes(),
				film.getMpa().getId()
		);

		film.setId(id);
		jdbc.batchUpdate(ADD_GENRE_QUERY, film.getGenres(), film.getGenres().size(),
				(PreparedStatement ps, Genre genre) -> {
			ps.setLong(1, film.getId());
			ps.setLong(2, genre.getId());
				}
				);
		return film;
	}

	private void deleteGenre(long filmId) {
		delete(
				DELETE_GENRE_QUERY,
				filmId
		);
	}

	public Optional<Film> getFilmById(long id) {
		return findOne(GET_FILM_BY_ID_QUERY, id);
	}

	public Film update(Film newFilm) {
		update(
				UPDATE_FILM_QUERY,
				newFilm.getName(),
				newFilm.getDescription(),
				Date.valueOf(newFilm.getReleaseDate()),
				newFilm.getDuration().toMinutes(),
				newFilm.getMpa().getId(),
				newFilm.getId()
			);
		deleteGenre(newFilm.getId());
		jdbc.batchUpdate(ADD_GENRE_QUERY, newFilm.getGenres(), 10,
				(PreparedStatement ps, Genre genre) -> {
					ps.setLong(1, newFilm.getId());
					ps.setLong(2, genre.getId());
				}
		);
		return newFilm;
	}

	public Collection<Film> getAllFilms() {
		return findMany(GET_ALL_FILMS_QUERY);
	}
}
