package ru.yandex.practicum.filmorate.dal.extractors;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class GenreExtractor implements ResultSetExtractor<List<Genre>> {
	public List<Genre> extractData(ResultSet rs) throws SQLException {
		List<Genre> genreList = new ArrayList<>();
		while (rs.next()) {
			long genreId = rs.getLong("id");
			Genre genre = new Genre();
			genre.setId(genreId);
			genre.setName(rs.getString("genre_name"));
			genreList.add(genre);
		}
		return genreList;
	}
}
