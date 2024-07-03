package ru.yandex.practicum.filmorate.dal.extractors;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MpaExtractor implements ResultSetExtractor<List<Mpa>> {
	public List<Mpa> extractData(ResultSet rs) throws SQLException {
		List<Mpa> mpaList = new ArrayList<>();
		while (rs.next()) {
			long mpaId = rs.getLong("id");
			Mpa mpa = new Mpa();
			mpa.setId(mpaId);
			mpa.setName(rs.getString("mpa_name"));
			mpaList.add(mpa);
		}
		return mpaList;
	}
}
