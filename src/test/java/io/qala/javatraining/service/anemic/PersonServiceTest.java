package io.qala.javatraining.service.anemic;

import io.qala.javatraining.domain.ObjectNotFoundException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static io.qala.datagen.RandomShortApi.nullOr;
import static io.qala.datagen.RandomShortApi.sample;
import static java.util.Collections.emptyList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

@Test
public class PersonServiceTest {
    private PersonService service;
    private PersonDao personDao;
    private StatisticsDao statsDao;

    @BeforeMethod public void initMocks() {
        statsDao = mock(StatisticsDao.class);
        personDao = mock(PersonDao.class);
        service = new PersonService(personDao, statsDao);
    }

    public void returnsProjectCount_ifProjectsArePresent() {
        Person p = new Person()
                .setProjects(projects())
                .setRelatives(sample(emptyList(), relatives(), null));
        doReturn(p).when(personDao).get(1L);

        PersonStatistics statistics = service.getStatistics(1L);
        assertEquals(statistics.getNumOfProjects(), (Integer) p.getProjects().size());
    }
    public void returnsRelativesCount_ifRelativesArePresent() {
        Person p = new Person()
                .setRelatives(relatives())
                .setProjects(sample(emptyList(), projects(), null));
        doReturn(p).when(personDao).get(1L);

        PersonStatistics statistics = service.getStatistics(1L);
        assertEquals(statistics.getNumOfRelatives(), (Integer) p.getRelatives().size());
    }
    public void savesStatistics_ifAnyOfFieldsAreNotNull() {
        List projects = sample(emptyList(), projects(), null);
        List relatives = isEmpty(projects) ? relatives() : sample(emptyList(), relatives(), null);
        Person p = new Person().setProjects(projects).setRelatives(relatives);
        doReturn(p).when(personDao).get(1L);

        service.getStatistics(1L);
        verify(statsDao).save(any(PersonStatistics.class));
    }
    public void doesNotSaveStats_ifAllFieldsAreEmpty() {
        Person p = new Person()
                .setProjects(nullOr(emptyList()))
                .setRelatives(nullOr(emptyList()));
        doReturn(p).when(personDao).get(1L);

        service.getStatistics(1L);
        verify(statsDao, never()).save(any());
    }

    @Test(expectedExceptions = ObjectNotFoundException.class)
    public void reThrowsObjectNotFound_ifPersonDoesNotExist() {
        doThrow(new ObjectNotFoundException(Person.class, "1L")).when(personDao).get(1L);
        service.getStatistics(1L);
    }

    private static List relatives() { return Arrays.asList(new Object(), new Object()); }
    private static List projects() { return Arrays.asList(new Object(), new Object()); }
}