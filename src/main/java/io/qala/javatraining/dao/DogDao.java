package io.qala.javatraining.dao;

import io.qala.javatraining.domain.Dog;

import java.util.Collection;

public interface DogDao {
    Collection<Dog> getAllDogs();

    Dog getDog(String id);

    Dog createDog(Dog dog);

    Dog deleteDog(String id);
}
