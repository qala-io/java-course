package io.qala.javatraining.service;

import io.qala.javatraining.domain.Dog;

import java.util.Collection;
import java.util.List;

public interface DogService {
    Collection<Dog> getAllDogs();

    Dog getDog(String id);

    Dog createDog(Dog dog);

    boolean deleteDog(String id);

    void createNewDogsAndIgnoreAlreadySaved(List<Dog> dogs);
}
