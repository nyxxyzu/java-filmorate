package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.MpaDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Optional;

@Service
public class MpaService {

	private final MpaDbStorage mpaStorage;

	@Autowired
	public MpaService(MpaDbStorage mpaStorage) {
		this.mpaStorage = mpaStorage;

	}

	public Collection<Mpa> getAllMpas() {
		return mpaStorage.getAllMpas();
	}

	public Optional<Mpa> getMpaById(long mpaId) {
		try {
			return mpaStorage.getMpaById(mpaId);
		} catch (RuntimeException e) {
			throw new NotFoundException("ID рейтинга не найден");
		}
	}
}
