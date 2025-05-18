package com.demo.aerztekasse.entity.Serializer;

import java.io.IOException;
import java.time.DayOfWeek;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class DayOfWeekSerializer extends JsonSerializer<DayOfWeek> {

    @Override
    public void serialize(DayOfWeek value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }

        String formatted = value.name().charAt(0) + value.name().substring(1).toLowerCase();
        gen.writeString(formatted);
    }
}