package ru.yandex.practicum.filmorate.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.Duration;

public class DurationDeserializer extends StdDeserializer<Duration> {

	public DurationDeserializer() {
		this(null);
	}

	public DurationDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public Duration deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
		long duration = jsonParser.getLongValue();
		return Duration.ofMinutes(duration);
	}
}
