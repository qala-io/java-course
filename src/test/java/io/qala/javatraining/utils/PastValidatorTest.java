package io.qala.javatraining.utils;


import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static io.qala.datagen.RandomDate.after;
import static io.qala.datagen.RandomDate.beforeNow;
import static java.time.Instant.now;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PastValidatorTest {

    @Test void futureDatesFailValidation() {
        assertFalse(isValid(after(now()).offsetDateTime()));
    }
    @Test void pastDatesPassValidation() {
        assertTrue(isValid(beforeNow().offsetDateTime()));
    }
    @Test void nullDatesPassValidation_becauseTheyAreIgnored() {
        assertTrue(isValid(null));
    }

    private boolean isValid(OffsetDateTime dateTime) {
        PastValidator validator = new PastValidator();
        validator.initialize(null);
        return validator.isValid(dateTime, null);
    }
}