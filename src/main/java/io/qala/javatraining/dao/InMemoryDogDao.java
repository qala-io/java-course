package io.qala.javatraining.dao;

import io.qala.javatraining.domain.Dog;
import io.qala.javatraining.domain.ObjectNotFoundException;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryDogDao {
    private static final ConcurrentMap<String, Dog> ALL_DOGS = new ConcurrentHashMap<>();

    public Collection<Dog> getAllDogs() {
        return ALL_DOGS.values();
    }

    public Dog getDog(String id) {
        Dog found = ALL_DOGS.get(id);
        if (found == null) throw new ObjectNotFoundException(Dog.class, id);
        return found;
    }

    public Dog createDog(Dog dog) {
        ALL_DOGS.put(dog.getId(), dog);
        return dog;
    }

    public Dog deleteDog(String id) {
        Dog removed = ALL_DOGS.remove(id);
        if (removed == null) throw new ObjectNotFoundException(Dog.class, id);
        return removed;
    }
}
