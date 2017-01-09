package io.qala.javatraining.dao;

import io.qala.javatraining.JdbcDaoTest;
import io.qala.javatraining.domain.Dog;
import io.qala.javatraining.domain.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collection;

import static io.qala.datagen.RandomShortApi.unicode;
import static io.qala.datagen.RandomValue.length;
import static io.qala.datagen.StringModifier.Impls.suffix;
import static io.qala.javatraining.TestUtils.assertReflectionEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@JdbcDaoTest @Test
public class JdbcDogDaoTest extends AbstractTestNGSpringContextTests {
    @BeforeMethod void beginTransaction() { connections.beginTransaction(); }
    @AfterMethod void rollbackTransaction() { connections.rollback(); }

    public void getsTheSameDogAsWasSaved() {
        Dog original = Dog.random();
        dao.createDog(original);
        Dog fromDb = dao.getDog(original.getId());
        assertReflectionEquals(original, fromDb);
    }

    public void createdDogsAppearIn_listOfAllDogs() {
        Dog original = Dog.random();
        dao.createDog(original);
        Collection<Dog> allDogs = dao.getAllDogs();
        assertTrue(allDogs.contains(original));
    }

    @Test(expectedExceptions = ObjectNotFoundException.class)
    public void findingDogThrows_ifDogWasRemoved() {
        Dog original = Dog.random();
        dao.createDog(original);
        dao.deleteDog(original.getId());
        dao.getDog(original.getId());
    }

    public void deletingDogConfirms_ifDogIsRemoved() {
        Dog original = Dog.random();
        dao.createDog(original);
        assertTrue(dao.deleteDog(original.getId()));
    }
    public void deletingDogConfirms_ifDogDidNotExist() {
        Dog original = Dog.random();
        dao.createDog(original);
        dao.deleteDog(original.getId());
        assertFalse(dao.deleteDog(original.getId()));
    }

    /**This ensures that Java validation and DB constraints are in sync. DB constraints are allowed to be more permissive though.*/
    public void dbCanHoldMaxValues_thatJavaCanHold() {
        Dog original = Dog.random();
        original.setTimeOfBirth(OffsetDateTime.ofInstant(Instant.ofEpochMilli(Long.MAX_VALUE), ZoneOffset.MIN))
                .setName(unicode(100))
                .setHeight(Double.MAX_VALUE).setWeight(Double.MAX_VALUE);
        dao.createDog(original);
        Dog fromDb = dao.getDog(original.getId());
        assertReflectionEquals(original, fromDb);
    }

    /**
     * SQL Injections are hideous vulnerabilities. Usually they check it in System Tests. But it's much more effective
     * and robust to test against them on DAO level.
     */
    public void dbOperationsAreProtectedFromSqlInjections() {
        String sqlInjection = length(20).with(suffix("'\"")).english();
        Dog original = Dog.random().setId(sqlInjection).setName(sqlInjection);//place #1
        dao.createDog(original);
        Dog fromDb = dao.getDog(sqlInjection);//place #2
        assertReflectionEquals(original, fromDb);

        dao.deleteDog(sqlInjection);//place #3
    }

    @Autowired private DogDao dao;
    @Autowired private JdbcConnectionHolder connections;
}