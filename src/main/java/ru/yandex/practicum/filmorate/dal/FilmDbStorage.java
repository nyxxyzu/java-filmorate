package ru.yandex.practicum.filmorate.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Primary
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
	private static final LocalDate EARLIEST_DATE = LocalDate.parse("1895-12-25");

	@Autowired
	public FilmDbStorage(JdbcTemplate jdbc, ResultSetExtractor<List<Film>> extractor) {
		super(jdbc, extractor, Film.class);
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

	public Collection<Film> getMostLiked(int size) {
		return getAllFilms().stream()
				.sorted((f1, f2) -> f2.getLikedBy().size() - f1.getLikedBy().size())
				.limit(size)
				.toList();
	}

	@Override
	public Film create(Film film) {
		if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(EARLIEST_DATE)) {
			throw new ValidationException("Дата релиза не должна быть раньше 28 декабря 1895 года");
		}
		try {
			long id = insert(
					ADD_FILM_QUERY,
					film.getName(),
					film.getDescription(),
					Date.valueOf(film.getReleaseDate()),
					film.getDuration().toMinutes(),
					film.getMpa().getId()
			);

			film.setId(id);
			for (Genre genre : film.getGenres()) {
				addGenre(id, genre.getId());
			}
		} catch (RuntimeException e) {
			throw new ValidationException("Ошибка валидации");
		}
		return film;
	}

	private void addGenre(long filmId, long genreId) {
		noPkInsert(
				ADD_GENRE_QUERY,
				filmId,
				genreId
		);
	}

	private void deleteGenre(long filmId) {
		delete(
				DELETE_GENRE_QUERY,
				filmId
		);
	}

	public Film getFilmById(long id) {
		return findOne(GET_FILM_BY_ID_QUERY, id);
	}

	public Film update(Film newFilm) {
		try {
			update(
					UPDATE_FILM_QUERY,
					newFilm.getName(),
					newFilm.getDescription(),
					Date.valueOf(newFilm.getReleaseDate()),
					newFilm.getDuration().toMinutes(),
					newFilm.getMpa().getId(),
					newFilm.getId()
			);}
		catch (InternalServerException e) {
			throw new NotFoundException("Пользователь не найден");
		}
		deleteGenre(newFilm.getId());
		for (Genre genre : newFilm.getGenres()) {
			addGenre(newFilm.getId(), genre.getId());
		}
		return newFilm;
	}

	public Collection<Film> getAllFilms() {
		return findMany(GET_ALL_FILMS_QUERY);
	}
}
