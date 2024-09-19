package com.example.client.api;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.lang.reflect.Type;

public class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            String dateTimeString = json.getAsString();
            return LocalDateTime.parse(dateTimeString, DATE_TIME_FORMATTER);
        } catch (Exception e) {
            throw new JsonParseException("Failed to parse LocalDateTime", e);
        }
    }
}