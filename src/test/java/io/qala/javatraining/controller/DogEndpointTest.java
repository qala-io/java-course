package io.qala.javatraining.controller;

import io.qala.javatraining.MockMvcTest;
import io.qala.javatraining.domain.Dog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static io.qala.datagen.RandomShortApi.negativeDouble;
import static io.qala.datagen.RandomShortApi.nullOrBlank;
import static io.qala.javatraining.TestUtils.assertReflectionEquals;
import static org.testng.Assert.*;

@MockMvcTest @Test
public class DogEndpointTest extends AbstractTestNGSpringContextTests {
    @Autowired private DogEndpoints dogs;

    public void createdDog_isReturnedInPostRequest() {
        Dog original = Dog.random();
        Dog fromDb = dogs.createDog(original);
        assertReflectionEquals(original, fromDb);
    }

    public void getsTheSameDogAsWasSaved() {
        Dog original = Dog.random();
        dogs.createDog(original);
        Dog fromDb = dogs.findDog(original.getId());

        assertReflectionEquals(original, fromDb);
    }

    public void returnsEmptyList_ifNoDogsExist() {
        dogs.deleteAll();
        List<Dog> dogs = this.dogs.listDogs();
        assertEquals(dogs.size(), 0);
    }
    public void invokesValidationBeforeSaving() {
        Dog invalidDog = Dog.random().setName(nullOrBlank()).setHeight(negativeDouble());
        ValidationRestError[] errors = dogs.createDogAndFailValidation(invalidDog);
        assertEquals(errors.length, 2);
        assertHasValidationError(errors, "height", "DecimalMin", "must be greater than 0");
        assertHasValidationError(errors, "name", "NotBlankSized", "size must be between 1 and 100");
    }

    @Test(expectedExceptions = Error404Exception.class)
    public void gettingReturns404_ifDogIsRemoved() {
        Dog dog = dogs.createDog();
        dogs.deleteDog(dog.getId());
        dogs.findDog(dog.getId());
    }
    @Test
    public void deleteDoesNothing_ifDogDoesNotExist() {
        dogs.deleteDog(UUID.randomUUID().toString());//doesn't throw anything
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
