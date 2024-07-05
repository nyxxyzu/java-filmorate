package ru.yandex.practicum.filmorate.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;


import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class MpaDbStorage extends BaseDbStorage<Mpa> {

	@Autowired
	public MpaDbStorage(JdbcTemplate jdbc, ResultSetExtractor<List<Mpa>> extractor) {
		super(jdbc, extractor);
	}

	private static final String GET_ALL_MPAS_QUERY = "SELECT * FROM mpa";
	private static final String GET_MPA_BY_ID_QUERY = "SELECT * FROM mpa WHERE id = ?";

	public Collection<Mpa> getAllMpas() {
		return findMany(GET_ALL_MPAS_QUERY);
	}

	public Optional<Mpa> getMpaById(long mpaId) {
		return findOne(GET_MPA_BY_ID_QUERY, mpaId);

	}

}
