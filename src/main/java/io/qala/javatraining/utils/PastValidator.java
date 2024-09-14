package io.qala.javatraining.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.OffsetDateTime;

public class PastValidator implements ConstraintValidator<Past, OffsetDateTime> {
    @Override public void initialize(Past constraintAnnotation) {}

    @Override public boolean isValid(OffsetDateTime value, ConstraintValidatorContext context) {
        return value == null || value.isBefore(OffsetDateTime.now());
    }
}
