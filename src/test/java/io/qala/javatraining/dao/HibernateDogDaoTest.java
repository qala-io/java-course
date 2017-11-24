package io.qala.javatraining.dao;

import io.qala.javatraining.HibernateDaoTest;
import io.qala.javatraining.domain.Dog;
import io.qala.javatraining.domain.Human;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

import static io.qala.datagen.RandomShortApi.sample;
import static org.testng.Assert.assertTrue;

@Test @HibernateDaoTest
public class HibernateDogDaoTest extends AbstractTransactionalTestNGSpringContextTests {
    public void savesDogWithOwners() {
        Dog dog = dao.createDog(Dog.random());
        Human owner = sample(dog.getOwners());
        assertTrue(dog.getOwners().contains(owner));
    }

    @Autowired private HibernateDogDao dao;
}