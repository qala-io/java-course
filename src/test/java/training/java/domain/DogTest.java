package training.java.domain;

import org.testng.annotations.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static io.qala.datagen.RandomShortApi.nullOrBlank;
import static io.qala.datagen.RandomShortApi.unicodeWithoutBoundarySpaces;
import static org.testng.Assert.assertEquals;

@Test
public class DogTest {
    public void validName_passesValidation() {
        Dog dog = Dog.random();
        assertValidationPasses(dog.setName(unicodeWithoutBoundarySpaces(1)), "Min boundary");
        assertValidationPasses(dog.setName(unicodeWithoutBoundarySpaces(100)), "Max boundary");
        assertValidationPasses(dog.setName(unicodeWithoutBoundarySpaces(2, 99)), "Middle value");
    }
    public void invalidName_failsValidation() {
        Dog dog = Dog.random();
        assertValidationFails(dog.setName(nullOrBlank()), "size must be between 1 and 100");
        assertValidationFails(dog.setName(unicodeWithoutBoundarySpaces(101)), "size must be between 1 and 100");
    }

    private static void assertValidationFails(Dog dog, String expectedError) {
        Set<ConstraintViolation<Dog>> errors = validator().validate(dog);
        assertEquals(errors.iterator().next().getMessage(), expectedError);
        assertEquals(errors.size(), 1, "Should've been the only one: " + expectedError);
    }
    private static void assertValidationPasses(Dog dog, String caseName) {
        Set<ConstraintViolation<Dog>> errors = validator().validate(dog);
        assertEquals(errors.size(), 0, "Failed: " + caseName);
    }
    private static Validator validator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return factory.getValidator();
    }
}