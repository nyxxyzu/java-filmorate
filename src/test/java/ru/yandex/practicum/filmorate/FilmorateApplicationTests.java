package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validationgroups.AdvanceInfo;
import ru.yandex.practicum.filmorate.validationgroups.BasicInfo;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class FilmorateApplicationTests {

	Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	FilmController filmController = new FilmController();
	UserController userController = new UserController();


	@Test
	void cantCreateFilmWithNoName() {
		Film film = new Film();
		film.setName("");
		Set<ConstraintViolation<Film>> violations = validator.validate(film, BasicInfo.class);
		assertThat(violations.size()).isEqualTo(1);

	}

	@Test
	void cantMakeFilmDescriptionOver200Symbols() {
		Film film = new Film();
		film.setName("Film name");
		film.setDescription("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
				"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
				"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
				"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		Set<ConstraintViolation<Film>> violations = validator.validate(film, AdvanceInfo.class);
		assertThat(violations.size()).isEqualTo(1);
	}

	@Test
	void cantMakeReleaseDateBefore1895() {
		Film film = new Film();
		film.setReleaseDate(LocalDate.of(1883,5,5));
		assertThrows(ValidationException.class, () -> {
			filmController.create(film);
		});
	}

	@Test
	void durationCantBeNegative() {
		Film film = new Film();
		film.setName("Film name");
		film.setDuration(Duration.ofMinutes(-30));
		Set<ConstraintViolation<Film>> violations = validator.validate(film, AdvanceInfo.class);
		assertThat(violations.size()).isEqualTo(1);
	}

	@Test
	void emailShouldNotBeEmpty() {
		User user = new User();
		user.setEmail("");
		user.setLogin("login");
		Set<ConstraintViolation<User>> violations = validator.validate(user, BasicInfo.class);
		assertThat(violations.size()).isEqualTo(1);
	}

	@Test
	void emailShouldBeCorrect() {
		User user = new User();
		user.setEmail("asdasdasd");
		user.setLogin("login");
		Set<ConstraintViolation<User>> violations = validator.validate(user, AdvanceInfo.class);
		assertThat(violations.size()).isEqualTo(1);
	}

	@Test
	void loginShouldNotBeEmpty() {
		User user = new User();
		user.setEmail("aaaa@yandex.ru");
		user.setLogin("");
		Set<ConstraintViolation<User>> violations = validator.validate(user, BasicInfo.class);
		assertThat(violations.size()).isEqualTo(1);
	}

	@Test
	void loginShouldNotHaveSpaces() {
		User user = new User();
		user.setEmail("aaaa@yandex.ru");
		user.setLogin("login 1");
		Set<ConstraintViolation<User>> violations = validator.validate(user, AdvanceInfo.class);
		assertThat(violations.size()).isEqualTo(1);
	}

	@Test
	void birthDateCanNotBeInFuture() {
		User user = new User();
		user.setEmail("aaaa@yandex.ru");
		user.setLogin("login");
		user.setBirthday(LocalDate.of(2025,5,5));
		Set<ConstraintViolation<User>> violations = validator.validate(user, AdvanceInfo.class);
		assertThat(violations.size()).isEqualTo(1);
	}
}
