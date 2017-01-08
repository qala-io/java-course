package io.qala.javatraining.service;

import io.qala.javatraining.domain.Dog;

import java.util.Collection;

public interface DogService {
    Collection<Dog> getAllDogs();

    Dog getDog(String id);

    Dog createDog(Dog dog);

    boolean deleteDog(String id);
}
