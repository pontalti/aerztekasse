package com.demo.aerztekasse.entity.Deserializer;

import java.io.IOException;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class DayOfWeekDeserializer extends JsonDeserializer<DayOfWeek> {

    private static final Map<String, DayOfWeek> ALIASES = new HashMap<>();

    static {
        for (DayOfWeek day : DayOfWeek.values()) {
            ALIASES.put(day.name(), day);
            ALIASES.put(day.name().substring(0, 3), day);
        }

        ALIASES.put("Monday", DayOfWeek.MONDAY);
        ALIASES.put("Tuesday", DayOfWeek.TUESDAY);
        ALIASES.put("Wednesday", DayOfWeek.WEDNESDAY);
        ALIASES.put("Thursday", DayOfWeek.THURSDAY);
        ALIASES.put("Friday", DayOfWeek.FRIDAY);
        ALIASES.put("Saturday", DayOfWeek.SATURDAY);
        ALIASES.put("Sunday", DayOfWeek.SUNDAY);
    }

    @Override
    public DayOfWeek deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String input = p.getText().trim();
        String key = normalize(input);
        DayOfWeek result = ALIASES.get(key);

        if (result == null) {
            throw new IOException("Invalid day of week: " + input);
        }

        return result;
    }

    private String normalize(String input) {
        if (input == null || input.isEmpty()) return "";
        input = input.trim();
        if (input.length() <= 3) {
            return input.substring(0, 1).toUpperCase() + input.substring(1, 3).toLowerCase();
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
}