package io.qala.javatraining.service.rich;

import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static io.qala.datagen.RandomShortApi.sample;
import static java.util.Collections.emptyList;
import static org.testng.Assert.assertEquals;

@Test
public class PersonTest {
    public void returnsProjectCount_ifProjectArePresent() {
        Person p = new Person()
                .setProjects(projects())
                .setRelatives(sample(emptyList(), relatives(), null));

        PersonStatistics statistics = p.buildStatistics();
        assertEquals(statistics.getNumOfProjects(), (Integer) p.getProjects().size());
    }

    public void returnsRelativesCount_ifRelativesArePresent() {
        Person p = new Person()
                .setRelatives(relatives())
                .setProjects(sample(emptyList(), projects(), null));

        PersonStatistics statistics = p.buildStatistics();
        assertEquals(statistics.getNumOfRelatives(), (Integer) p.getRelatives().size());
    }

    private static List relatives() { return Arrays.asList(new Object(), new Object()); }
    private static List projects() { return Arrays.asList(new Object(), new Object()); }
}