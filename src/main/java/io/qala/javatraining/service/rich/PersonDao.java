package io.qala.javatraining.service.rich;

import io.qala.javatraining.domain.ObjectNotFoundException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.qala.datagen.RandomShortApi.positiveLong;

class PersonDao {
    Person get(long id) {
        Person person = people.get(id);
        if (person == null) throw new ObjectNotFoundException(Person.class, id + "");
        return person;
    }

    void save(Person p) {
        p.setId(positiveLong());
        people.put(p.getId(), p);
    }

    private final Map<Long, Person> people = new ConcurrentHashMap<>();
}
