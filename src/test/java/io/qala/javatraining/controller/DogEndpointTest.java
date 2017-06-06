package io.qala.javatraining.controller;

import io.qala.javatraining.MockMvcTest;
import io.qala.javatraining.domain.Dog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import static io.qala.javatraining.TestUtils.assertReflectionEquals;

@MockMvcTest @Test
public class DogEndpointTest extends AbstractTestNGSpringContextTests {

    public void getsTheSameDogAsWasSaved() {
        Dog original = Dog.random();
        dogs.createDog(original);
        Dog fromDb = dogs.findDog(original.getId());

        assertReflectionEquals(original, fromDb);
    }

    @Autowired private DogEndpoints dogs;
}
