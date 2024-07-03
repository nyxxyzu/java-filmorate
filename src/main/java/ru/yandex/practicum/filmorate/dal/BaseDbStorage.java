package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.exception.InternalServerException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@RequiredArgsConstructor
public class BaseDbStorage<T> {
	protected final JdbcTemplate jdbc;
	protected final ResultSetExtractor<List<T>> extractor;
	private final Class<T> entityType;

	protected T findOne(String query, Object... params) {
		return jdbc.query(query, extractor, params).getFirst();

	}

	protected List<T> findMany(String query, Object... params) {
		return jdbc.query(query, extractor, params);
	}

	protected boolean delete(String query, Object... params) {
		int rowsDeleted = jdbc.update(query, params);
		return rowsDeleted > 0;
	}

	protected Long insert(String query, Object... params) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbc.update(connection -> {
			PreparedStatement ps = connection
					.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			for (int idx = 0; idx < params.length; idx++) {
				ps.setObject(idx + 1, params[idx]);
			}
			return ps;
		}, keyHolder);

		Long id = keyHolder.getKeyAs(Long.class);
		if (id != null) {
			return id;
		} else {
			throw new InternalServerException("Не удалось сохранить данные");
		}
	}

	protected void update(String query, Object... params) {
		int rowsUpdated = jdbc.update(query, params);
		if (rowsUpdated == 0) {
			throw new InternalServerException("Не удалось обновить данные");
		}
	}

	protected void noPkInsert(String query, Object... params) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbc.update(connection -> {
			PreparedStatement ps = connection
					.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			for (int idx = 0; idx < params.length; idx++) {
				ps.setObject(idx + 1, params[idx]);
			}
			return ps;
		}, keyHolder);
	}
}
