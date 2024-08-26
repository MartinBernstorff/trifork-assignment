package com.trifork.assignment.OrgRepository;

import java.util.Optional;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class OptionalLongConverter implements AttributeConverter<Optional<Long>, Long> {
    @Override
    public Long convertToDatabaseColumn(Optional<Long> attribute) {
        return attribute.orElse(null);
    }

    @Override
    public Optional<Long> convertToEntityAttribute(Long dbData) {
        return Optional.ofNullable(dbData);
    }
}