package io.qala.javatraining.service.rich;

class PersonService {
    private final PersonDao personDao;
    private final StatisticsDao statsDao;

    PersonService(PersonDao personDao, StatisticsDao statsDao) {
        this.personDao = personDao;
        this.statsDao = statsDao;
    }

    PersonStatistics getStatistics(long personId) {
        Person p = personDao.get(personId);
        PersonStatistics result = p.buildStatistics();
        if(result.isNotEmpty()) statsDao.save(result);
        return result;
    }
}
