package io.qala.javatraining;

import io.qala.javatraining.domain.Dog;
import org.unitils.reflectionassert.ReflectionAssert;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUtils {

    /** Unitils' method can't handle Java8 Dates so we have to give them a special treat. */
    public static void assertReflectionEquals(Dog expected, Dog actual) {
        OffsetDateTime expectedTime = expected.getTimeOfBirth();
        OffsetDateTime actualTime = actual.getTimeOfBirth();

        ReflectionAssert.assertReflectionEquals(withoutDates(expected), withoutDates(actual));
        assertDatesEqual(expectedTime, actualTime);

        expected.setTimeOfBirth(expectedTime);
        actual.setTimeOfBirth(actualTime);
    }
    /**
     * reflectionEquals can't handle the dates since a lot of crazy stuff happens with dates when saving/retrieving:
     * e.g. Offsets turn into ZoneIds. So we have to check the dates separately and then let reflectionEquals handle
     * the rest of the fields.
     */
    private static Dog withoutDates(Dog dog) {
        dog.setTimeOfBirth(null);
        return dog;
    }

    private static void assertDatesEqual(OffsetDateTime expected, OffsetDateTime actual) {
        if (expected == null)   assertEquals(null, actual);
        else if(actual == null) assertEquals(null, expected);
        else                    assertEquals(actual.toInstant(), expected.toInstant());
    }
}
