package training.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.Test;
import training.java.domain.Dog;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static io.qala.datagen.RandomShortApi.negativeDouble;
import static io.qala.datagen.RandomShortApi.nullOrBlank;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@WebAppConfiguration @ContextConfiguration(locations = {"classpath:/test-context.xml", "classpath:/web-context.xml"})
@Test
@SuppressWarnings("WeakerAccess")
public class DogEndpointTest extends AbstractTestNGSpringContextTests {
    @Autowired DogEndpoints dogs;

    public void createdDog_isReturnedInPostRequest() {
        Dog original = Dog.random();
        Dog fromDb = dogs.createDog(original);

        assertDatesEqual(original.getTimeOfBirth(), fromDb.getTimeOfBirth());
        assertReflectionEquals(withoutDates(original), withoutDates(fromDb));
    }

    public void getsTheSameDogAsWasSaved() {
        Dog original = Dog.random();
        dogs.createDog(original);
        Dog fromDb = dogs.findDog(original.getId());

        assertDatesEqual(original.getTimeOfBirth(), fromDb.getTimeOfBirth());
        assertReflectionEquals(withoutDates(original), withoutDates(fromDb));
    }

    public void createdDogsAppearIn_listOfAllDogs() {
        Dog dog = dogs.createDog();
        List<Dog> fromDb = dogs.listDogs();
        assertTrue(fromDb.contains(dog), "All Dogs " + fromDb + " didn't contain " + dog);
    }
    public void invokesValidationBeforeSaving() {
        Dog invalidDog = Dog.random().setName(nullOrBlank()).setHeight(negativeDouble());
        ValidationRestError[] errors = dogs.createDogAndFailValidation(invalidDog);
        assertEquals(errors.length, 2);
        assertHasValidationError(errors, "height", "DecimalMin", "must be greater than 0");
        assertHasValidationError(errors, "name", "NotBlankSized", "size must be between 1 and 100");
    }

    @Test(expectedExceptions = Error404Exception.class)
    public void returns404_ifNotExistingDogIsRequested() {
        dogs.findDog(UUID.randomUUID().toString());
    }

    /**
     * reflectionEquals can't handle the dates since a lot of crazy stuff happens with dates when saving/retrieving:
     * e.g. Offsets turn into ZoneIds. So we have to check the dates separately and then let reflectionEquals handle
     * the rest of the fields.
     */
    private Dog withoutDates(Dog dog) {
        dog.setTimeOfBirth(null);
        return dog;
    }

    private void assertDatesEqual(OffsetDateTime expected, OffsetDateTime actual) {
        if (expected == null) assertEquals(null, actual);
        else                  assertEquals(actual.toInstant(), expected.toInstant());
    }
    private void assertHasValidationError(ValidationRestError[] errors, String field, String errorCode, String errorMsg) {
        for(ValidationRestError error: errors) {
            if(     error.getObjectName().equals("dog") &&
                    error.getField().equals(field) &&
                    error.getErrorCode().equals(errorCode) &&
                    error.getErrorMessage().equals(errorMsg)) {
                return;
            }
        }
        fail("Couldn't find error: " + field + ", " + errorCode + ", " + errorMsg + ". Actual errors: " + Arrays.deepToString(errors));
    }
}
