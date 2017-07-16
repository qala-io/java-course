package io.qala.javatraining.service.rich;

import io.qala.javatraining.domain.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static io.qala.datagen.RandomShortApi.nullOr;
import static io.qala.datagen.RandomShortApi.positiveLong;
import static io.qala.datagen.RandomShortApi.sample;
import static java.util.Collections.emptyList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.testng.Assert.*;

@Transactional @Test @ContextConfiguration("classpath:/dao-context.xml")
public class PersonServiceComponentTest extends AbstractTestNGSpringContextTests {
    @Autowired PersonService service;
    @Autowired StatisticsDao statsDao;
    @Autowired PersonDao     personDao;

    public void savesStatistics_ifNotEmpty() {
        List projects = sample(emptyList(), projects(), null);
        List relatives = isEmpty(projects) ? relatives() : sample(emptyList(), relatives(), null);
        Person p = save(new Person().setProjects(projects).setRelatives(relatives));

        service.getStatistics(p.getId());
        assertNotNull(statsDao.getPersonStats(p.getId()));
    }
    public void doesNotSaveStats_ifEmpty() {
        Person p = save(new Person()
            .setProjects(nullOr(emptyList()))
            .setRelatives(nullOr(emptyList())));

        service.getStatistics(p.getId());
        assertNull(statsDao.getPersonStats(p.getId()));
    }

    @Test(expectedExceptions = ObjectNotFoundException.class)
    public void reThrowsObjectNotFound_ifPersonDoesNotExist() {
        service.getStatistics(positiveLong());
    }

    private static List relatives() { return Arrays.asList(new Object(), new Object()); }
    private static List projects()  { return Arrays.asList(new Object(), new Object()); }

    private Person save(Person p) {
        personDao.save(p);
        if(p.getRelatives() != null)
            for (Object o : p.getRelatives())
                //save relatives
                ;
        if(p.getProjects() != null)
            for(Object o : p.getProjects())
                //save projects
                ;
        return p;
    }
}