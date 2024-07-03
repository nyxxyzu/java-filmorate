package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.MpaDbStorage;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

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

	public Mpa getMpaById(long mpaId) {
		return mpaStorage.getMpaById(mpaId);
	}
}
