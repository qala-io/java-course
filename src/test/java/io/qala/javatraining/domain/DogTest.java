package io.qala.javatraining.domain;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

import static io.qala.datagen.RandomDate.after;
import static io.qala.datagen.RandomDate.beforeNow;
import static io.qala.datagen.RandomShortApi.*;
import static java.time.Instant.now;
import static org.testng.Assert.assertEquals;

/**
 * Usually we try to have 1 logical assertion per test. Otherwise tests become hard to read and they fail due
 * to multiple reasons. We violated this rule here because: a) these tests are very trivial b) otherwise they would
 * take much more space and <i>that</i> would make our tests less readable. So we're just trying to balance between
 * 2 evils.
 */
@Test
public class DogTest {
    public void validName_passesValidation() {
        Dog dog = Dog.random();
        assertNameValidationPasses(dog.setName(unicodeWithoutBoundarySpaces(1)), "min boundary");
        assertNameValidationPasses(dog.setName(unicodeWithoutBoundarySpaces(100)), "max boundary");
        assertNameValidationPasses(dog.setName(unicodeWithoutBoundarySpaces(2, 99)), "middle value");
    }
    public void invalidName_failsValidation() {
        Dog dog = Dog.random();
        assertValidationFails(dog.setName(nullOrBlank()), "size must be between 1 and 100");
        assertValidationFails(dog.setName(unicodeWithoutBoundarySpaces(101)), "size must be between 1 and 100");
    }

    public void futureBirthDate_failsValidation() {
        Dog dog = Dog.random();
        assertValidationFails(dog.setTimeOfBirth(after(now()).offsetDateTime()), "must be a past date");
    }
    public void nullBirthDate_passesValidation() {
        Dog dog = Dog.random();
        assertBirthDateValidationPasses(dog.setTimeOfBirth(null), "null birth date");
    }
    public void birthDateInPast_passesValidation() {
        Dog dog = Dog.random();
        assertBirthDateValidationPasses(dog.setTimeOfBirth(beforeNow().offsetDateTime()), "birth date in the past");
    }

    public void positiveHeightOrWeight_passesValidation() {
        assertSizesValidationPasses(Dog.random().setHeight(Double.MIN_VALUE), "positive height");
        assertSizesValidationPasses(Dog.random().setWeight(Double.MIN_VALUE), "positive weight");
    }
    public void zeroHeightOrWeight_failsValidation() {
        assertValidationFails(Dog.random().setHeight(0), "must be greater than 0");
        assertValidationFails(Dog.random().setWeight(0), "must be greater than 0");
    }
    public void negativeHeightOrWeight_failsValidation() {
        assertValidationFails(Dog.random().setHeight(negativeDouble()), "must be greater than 0");
        assertValidationFails(Dog.random().setWeight(negativeDouble()), "must be greater than 0");
    }

    private static void assertValidationFails(Dog dog, String expectedError) {
        Set<ConstraintViolation<Dog>> errors = VALIDATOR.validate(dog);
        assertEquals(errors.iterator().next().getMessage(), expectedError);
        assertEquals(errors.size(), 1, "Should've been the only one: " + expectedError);
    }

    private static void assertNameValidationPasses(Dog dog, String caseName) {
        Set<ConstraintViolation<Dog>> errors = VALIDATOR.validate(dog);
        assertEquals(errors.size(), 0, "Failed: " + caseName + ", value was: [" + dog.getName() + "].");
    }
    private static void assertBirthDateValidationPasses(Dog dog, String caseName) {
        Set<ConstraintViolation<Dog>> errors = VALIDATOR.validate(dog);
        assertEquals(errors.size(), 0, "Failed: " + caseName + ", value was: [" + dog.getTimeOfBirth() + "].");
    }
    private static void assertSizesValidationPasses(Dog dog, String caseName) {
        Set<ConstraintViolation<Dog>> errors = VALIDATOR.validate(dog);
        assertEquals(errors.size(), 0, "Failed: " + caseName + ", height was: [" + dog.getHeight() +
                "], weight was: [" + dog.getWeight() + "].");
    }

    @BeforeClass
    private static void initValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        VALIDATOR = factory.getValidator();
    }

    private static Validator VALIDATOR;
}