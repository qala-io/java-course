package training.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.Test;
import training.java.domain.Dog;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:/test-context.xml", "classpath:/web-context.xml"})
public class DogEndpointTest extends AbstractTestNGSpringContextTests {
    @Autowired DogEndpoints dogs;
    @Autowired ApplicationContext appContext;

    @Test public void getsTheSameDogAsWasSaved() {
        Dog dog = Dog.random();
        dogs.createDog(dog);
        Dog fromDb = dogs.findDog(dog.getId());
        assertReflectionEquals(dog, fromDb);
    }
}
