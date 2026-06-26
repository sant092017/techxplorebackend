package com.techxplore.techxplorebackend.converter;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.fasterxml.jackson.databind.ObjectMapper;

@Converter
public class JsonNodeConverter implements AttributeConverter<JsonNode, String> {
    @Override
    public String convertToDatabaseColumn(JsonNode attribute) {
        return attribute == null ? null : attribute.toString();
    }

    @Override
    public JsonNode convertToEntityAttribute(String dbData) {
        try {
            return dbData == null ? null : new ObjectMapper().readTree(dbData);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting JSON", e);
        }
    }
}
