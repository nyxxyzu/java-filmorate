package ru.yandex.practicum.filmorate.dal.extractors;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FilmExtractor implements ResultSetExtractor<List<Film>> {

	@Override
	public List<Film> extractData(ResultSet rs) throws SQLException {
		Map<Long, Film> filmIdToFilm = new HashMap<>();
		while (rs.next()) {
			long filmId = rs.getLong("id");
			if (filmIdToFilm.containsKey(filmId)) {
				long genreId = rs.getLong("genre_id");
				if (genreId != 0) {
					Genre genre = new Genre();
					genre.setId(genreId);
					genre.setName(rs.getString("genre_name"));
					filmIdToFilm.get(filmId).getGenres().add(genre);
				}
				long userId = rs.getLong("user_id");
				if (userId != 0) {
					filmIdToFilm.get(filmId).getLikedBy().add(userId);
				}
			} else {
				Film film = new Film();
				film.setId(filmId);
				film.setName(rs.getString("name"));
				film.setDescription(rs.getString("description"));
				film.setReleaseDate(rs.getDate("releaseDate").toLocalDate());
				film.setDuration(Duration.ofMinutes(rs.getInt("duration")));
				Mpa mpa = new Mpa();
				mpa.setId(rs.getLong("mpa"));
				mpa.setName(rs.getString("mpa_name"));
				film.setMpa(mpa);
				long genreId = rs.getLong("genre_id");
				if (genreId != 0) {
					Genre genre = new Genre();
					genre.setId(genreId);
					genre.setName(rs.getString("genre_name"));
					film.getGenres().add(genre);
				}
				long userId = rs.getLong("user_id");
				if (userId != 0) {
					film.getLikedBy().add(userId);
				}
				filmIdToFilm.put(filmId, film);
			}
		}

		return new ArrayList<>(filmIdToFilm.values());
	}
}
