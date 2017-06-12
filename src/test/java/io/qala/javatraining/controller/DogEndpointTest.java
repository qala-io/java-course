package io.qala.javatraining.controller;

import io.qala.javatraining.MockMvcTest;
import io.qala.javatraining.domain.Dog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static io.qala.datagen.RandomShortApi.negativeDouble;
import static io.qala.datagen.RandomShortApi.nullOrBlank;
import static io.qala.javatraining.TestUtils.assertReflectionEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

@MockMvcTest
class DogEndpointTest {

    @Test void createdDog_isReturnedInPostRequest() {
        Dog original = Dog.random();
        Dog fromDb = dogs.createDog(original);
        assertReflectionEquals(original, fromDb);
    }

    @Test void getsTheSameDogAsWasSaved() {
        Dog original = Dog.random();
        dogs.createDog(original);
        Dog fromDb = dogs.findDog(original.getId());

        assertReflectionEquals(original, fromDb);
    }

    @Test void returnsEmptyList_ifNoDogsExist() {
        dogs.deleteAll();
        List<Dog> dogs = this.dogs.listDogs();
        assertEquals(dogs.size(), 0);
    }
    @Test void invokesValidationBeforeSaving() {
        Dog invalidDog = Dog.random().setName(nullOrBlank()).setHeight(negativeDouble());
        ValidationRestError[] errors = dogs.createDogAndFailValidation(invalidDog);
        assertEquals(errors.length, 2);
        assertHasValidationError(errors, "height", "DecimalMin", "must be greater than 0");
        assertHasValidationError(errors, "name", "NotBlankSized", "size must be between 1 and 100");
    }

    @Test void gettingReturns404_ifDogIsRemoved() {
        Dog dog = dogs.createDog();
        dogs.deleteDog(dog.getId());
        assertThrows(Error404Exception.class, ()->dogs.findDog(dog.getId()));
    }
    @Test void deleteDoesNothing_ifDogDoesNotExist() {
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

    @Autowired private DogEndpoints dogs;
}
