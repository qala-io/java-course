package io.qala.javatraining.service;

import io.qala.javatraining.ServiceLayerTest;
import io.qala.javatraining.domain.Dog;
import io.qala.javatraining.domain.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static io.qala.datagen.RandomShortApi.unicode;
import static org.junit.Assert.fail;

@ServiceLayerTest @Test
public class JdbcDogServiceTest extends AbstractTestNGSpringContextTests {

    /** Find the description of the puzzle in the root README.md */
    @Test(expectedExceptions = ObjectNotFoundException.class)
    public void ifTransactionFails_thenNoneOfDogsGetSaved() {
        Dog invalidDog = Dog.random().setName(unicode(1001));
        Dog validDog = Dog.random();
        List<Dog> dogs = Arrays.asList(validDog, invalidDog);
        try {
            dogService.createNewDogsAndIgnoreAlreadySaved(dogs);
            fail("Invalid Dog should've failed constraint.");
        } catch (Exception e) {
            //this should throw ObjectNotFoundException since Spring Tx rolls back transactions if exceptions happen
            dogService.getDog(validDog.getId());
        }
    }

    @Autowired private DogService dogService;
}