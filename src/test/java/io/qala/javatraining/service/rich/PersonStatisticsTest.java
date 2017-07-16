package io.qala.javatraining.service.rich;

import org.testng.annotations.Test;


import static io.qala.datagen.RandomShortApi.nullOr;
import static io.qala.datagen.RandomShortApi.positiveInteger;
import static io.qala.datagen.RandomShortApi.sample;
import static org.testng.Assert.*;

@Test
public class PersonStatisticsTest {
    public void statsIsNotEmpty_ifAnyOfFieldsIsPresent() {
        Integer nOfProjects = nullableInt();
        PersonStatistics stats = new PersonStatistics(null)
                .setNumOfProjects(nOfProjects)
                .setNumOfRelatives(isEmpty(nOfProjects) ? positiveInteger() : nullableInt());
        assertTrue(stats.isNotEmpty());
    }
    public void statsIsEmpty_ifAllFieldsAreEmpty() {
        PersonStatistics stats = new PersonStatistics(null)
                .setNumOfProjects(nullOr(0))
                .setNumOfRelatives(nullOr(0));
        assertFalse(stats.isNotEmpty());
    }

    private static Integer nullableInt()      { return sample(0, null, positiveInteger()); }
    private static boolean isEmpty(Integer i) { return i == null || i.equals(0); }
}