package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.time.DurationMin;
import ru.yandex.practicum.filmorate.serializers.DurationDeserializer;
import ru.yandex.practicum.filmorate.serializers.DurationSerializer;

import java.time.Duration;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Film {

	private Integer id;
	@NotNull(message = "Введите название фильма")
	@NotBlank(message = "Название фильма не должно быть пустым")
	private String name;
	@Size(max = 200)
	private String description;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate releaseDate;
	@DurationMin(minutes = 0)
	@JsonDeserialize(using = DurationDeserializer.class)
	@JsonSerialize(using = DurationSerializer.class)
	private Duration duration;

}
