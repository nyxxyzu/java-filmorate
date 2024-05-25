package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class User {

	private Integer id;
	@NotNull(message = "Введите e-mail")
	@NotBlank(message = "E-mail не должен быть пуст")
	@Email(message = "Неправильный формат e-mail.")
	private String email;
	@NotBlank(message = "Логин не должен быть пуст")
	@NotNull(message = "Введите логин")
	@Pattern(regexp = "^([A-z])*\\S*$", message = "Логин не должен содержать пробелов.")
	private String login;
	private String name;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthday;

}
