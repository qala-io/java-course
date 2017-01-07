package io.qala.javatraining.utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.OffsetDateTime;

public class PastValidator implements ConstraintValidator<Past, OffsetDateTime> {
    @Override public void initialize(Past constraintAnnotation) {}

    @Override public boolean isValid(OffsetDateTime value, ConstraintValidatorContext context) {
        return value == null || value.isBefore(OffsetDateTime.now());
    }
}
