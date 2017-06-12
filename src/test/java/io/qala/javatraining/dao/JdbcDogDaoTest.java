package io.qala.javatraining.dao;

import io.qala.javatraining.JdbcDaoTest;
import io.qala.javatraining.domain.Dog;
import io.qala.javatraining.domain.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collection;

import static io.qala.datagen.RandomShortApi.unicode;
import static io.qala.datagen.RandomValue.length;
import static io.qala.datagen.StringModifier.Impls.suffix;
import static io.qala.javatraining.TestUtils.assertReflectionEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcDaoTest
class JdbcDogDaoTest {
    @BeforeEach void beginTransaction() { connections.beginTransaction(); }
    @AfterEach void rollbackTransaction() { connections.rollback(); }

    @Test void getsTheSameDogAsWasSaved() {
        Dog original = Dog.random();
        dao.createDog(original);
        Dog fromDb = dao.getDog(original.getId());
        assertReflectionEquals(original, fromDb);
    }

    @Test void createdDogsAppearIn_listOfAllDogs() {
        Dog original = Dog.random();
        dao.createDog(original);
        Collection<Dog> allDogs = dao.getAllDogs();
        assertTrue(allDogs.contains(original));
    }

    @Test void findingDogThrows_ifDogWasRemoved() {
        Dog original = Dog.random();
        dao.createDog(original);
        dao.deleteDog(original.getId());
        assertThrows(ObjectNotFoundException.class, ()->dao.getDog(original.getId()));
    }

    @Test void deletingDogConfirms_ifDogIsRemoved() {
        Dog original = Dog.random();
        dao.createDog(original);
        assertTrue(dao.deleteDog(original.getId()));
    }
    @Test void deletingDogConfirms_ifDogDidNotExist() {
        Dog original = Dog.random();
        dao.createDog(original);
        dao.deleteDog(original.getId());
        assertFalse(dao.deleteDog(original.getId()));
    }

    /**This ensures that Java validation and DB constraints are in sync. DB constraints are allowed to be more permissive though.*/
    @Test void dbCanHoldMaxValues_thatJavaCanHold() {
        Dog original = Dog.random();
        // For explanation about 808 see Dog.random()
        original.setTimeOfBirth(OffsetDateTime.ofInstant(Instant.ofEpochMilli(Long.MIN_VALUE + 808), ZoneOffset.MIN))
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
    @Test void dbOperationsAreProtectedFromSqlInjections() {
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