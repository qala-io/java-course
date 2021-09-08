package io.qala.javatraining.service;

import io.qala.javatraining.dao.DogDao;
import io.qala.javatraining.domain.Dog;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public class DbDogService implements DogService {
    public DbDogService(DogDao dogDao) {
        this.dogDao = dogDao;
    }

    @Transactional @Override public Collection<Dog> getAllDogs() {
        return dogDao.getAllDogs();
    }

    @Transactional @Override public Dog getDog(String id) {
        return dogDao.getDog(id);
    }

    @Transactional @Override public Dog createDog(Dog dog) {
        return dogDao.createDog(dog);
    }

    @Transactional @Override public boolean deleteDog(String id) {
        return dogDao.deleteDog(id);
    }

    private final DogDao dogDao;
}
