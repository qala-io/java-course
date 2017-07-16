package io.qala.javatraining.service.person;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class StatisticsDao {
    void save(PersonStatistics result) {
        stats.put(result.getPerson().getId(), result);
    }
    PersonStatistics getPersonStats(long personId) {
        return stats.get(personId);
    }

    private final Map<Long, PersonStatistics> stats = new ConcurrentHashMap<>();
}
