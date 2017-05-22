package io.qala.javatraining.dao;

import io.qala.javatraining.HibernateDaoTest;
import io.qala.javatraining.domain.Dog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolationException;

import static io.qala.datagen.RandomShortApi.english;

@Test @HibernateDaoTest
public class HibernateDogDaoTest extends AbstractTransactionalTestNGSpringContextTests {

    @Test(expectedExceptions = ConstraintViolationException.class, expectedExceptionsMessageRegExp = "Validation failed.*")
    public void validationIsInvoked_byOrm_duringSaving() {
        Dog original = Dog.random();
        original.setName(english(101)/*fails a validation rule*/);
        dao.createDog(original);
    }

    @Autowired private HibernateDogDao dao;
}