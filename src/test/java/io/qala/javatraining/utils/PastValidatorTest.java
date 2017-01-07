package io.qala.javatraining.utils;

import io.qala.javatraining.utils.PastValidator;
import org.testng.annotations.Test;

import java.time.OffsetDateTime;

import static io.qala.datagen.RandomDate.after;
import static io.qala.datagen.RandomDate.beforeNow;
import static java.time.Instant.now;
import static org.testng.Assert.*;

@Test
public class PastValidatorTest {

    public void futureDatesFailValidation() {
        assertFalse(isValid(after(now()).offsetDateTime()));
    }
    public void pastDatesPassValidation() {
        assertTrue(isValid(beforeNow().offsetDateTime()));
    }
    public void nullDatesPassValidation_becauseTheyAreIgnored() {
        assertTrue(isValid(null));
    }

    private boolean isValid(OffsetDateTime dateTime) {
        PastValidator validator = new PastValidator();
        validator.initialize(null);
        return validator.isValid(dateTime, null);
    }
}