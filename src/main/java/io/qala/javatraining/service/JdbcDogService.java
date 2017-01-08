package io.qala.javatraining.service;

import io.qala.javatraining.dao.DogDao;
import io.qala.javatraining.domain.Dog;

import java.util.Collection;

public class JdbcDogService implements DogService {
    public JdbcDogService(DogDao dogDao) {
        this.dogDao = dogDao;
    }

    public Collection<Dog> getAllDogs() {
        return dogDao.getAllDogs();
    }

    @Override public Dog getDog(String id) {
        return dogDao.getDog(id);
    }

    @Override public Dog createDog(Dog dog) {
        return dogDao.createDog(dog);
    }

    @Override public boolean deleteDog(String id) {
        return dogDao.deleteDog(id);
    }

    private final DogDao dogDao;
}
