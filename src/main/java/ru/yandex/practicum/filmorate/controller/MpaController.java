package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@Slf4j
@RestController
public class MpaController {

	private final MpaService mpaService;


	@Autowired
	public MpaController(MpaService mpaService) {
		this.mpaService = mpaService;

	}

	@GetMapping("/mpa")
	public Collection<Mpa> getAllMpas() {
		log.info(mpaService.getAllMpas().toString());
		return mpaService.getAllMpas();
	}

	@GetMapping("/mpa/{id}")
	public Mpa getMpaById(@PathVariable("id") int id) {
		log.info(mpaService.getMpaById(id).toString());
		return mpaService.getMpaById(id);
	}
}
