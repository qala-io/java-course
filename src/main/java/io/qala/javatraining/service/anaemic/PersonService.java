package io.qala.javatraining.service.anaemic;

import org.apache.commons.collections4.CollectionUtils;

class PersonService {
    private final PersonDao personDao;
    private final StatisticsDao statsDao;

    PersonService(PersonDao personDao, StatisticsDao statsDao) {
        this.personDao = personDao;
        this.statsDao = statsDao;
    }

    PersonStatistics getStatistics(long personId) {
        Person p = personDao.get(personId);
        PersonStatistics result = new PersonStatistics(p);
        if(CollectionUtils.isNotEmpty(p.getRelatives())) result.setNumOfRelatives(p.getRelatives().size());
        if(CollectionUtils.isNotEmpty(p.getProjects( ))) result.setNumOfProjects(p.getProjects().size());

        if((result.getNumOfProjects() != null && result.getNumOfProjects() != 0)
                || (result.getNumOfRelatives() != null && result.getNumOfRelatives() != 0))
            statsDao.save(result);//saves only if it's not empty
        return result;
    }
}
