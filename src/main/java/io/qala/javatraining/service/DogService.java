package io.qala.javatraining.service;

import io.qala.javatraining.dao.DogDao;
import io.qala.javatraining.domain.Dog;

import java.util.Collection;

public class DogService {
    private final DogDao dogDao;

    public DogService(DogDao dogDao) {
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
