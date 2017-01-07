package io.qala.javatraining.service;

import io.qala.javatraining.dao.InMemoryDogDao;
import io.qala.javatraining.domain.Dog;

import java.util.Collection;

public class DogService {
    private final InMemoryDogDao dogDao;

    public DogService(InMemoryDogDao dogDao) {
        this.dogDao = dogDao;
    }

    public Collection<Dog> getAllDogs() {
        return dogDao.getAllDogs();
    }

    public Dog getDog(String id) {
        return dogDao.getDog(id);
    }

    public Dog createDog(Dog dog) {
        return dogDao.createDog(dog);
    }

    public Dog deleteDog(String id) {
        return dogDao.deleteDog(id);
    }
}
