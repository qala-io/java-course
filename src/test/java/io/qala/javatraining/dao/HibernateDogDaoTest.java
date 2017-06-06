package io.qala.javatraining.dao;

import io.qala.javatraining.HibernateDaoTest;
import io.qala.javatraining.domain.Dog;
import io.qala.javatraining.domain.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collection;

import static io.qala.datagen.RandomShortApi.unicode;
import static io.qala.datagen.RandomValue.length;
import static io.qala.datagen.StringModifier.Impls.suffix;
import static io.qala.javatraining.TestUtils.assertReflectionEquals;
import static org.testng.Assert.*;

@Test @HibernateDaoTest
public class HibernateDogDaoTest extends AbstractTransactionalTestNGSpringContextTests {
    /** Find the description of the puzzle in the root README.md */
    public void getsTheSameDogAsWasSaved() {
        Dog original = Dog.random();
        dao.createDog(original);
        Dog fromDb = dao.getDog(original.getId());
        assertReflectionEquals(original, fromDb);
    }

    @Autowired private HibernateDogDao dao;
}