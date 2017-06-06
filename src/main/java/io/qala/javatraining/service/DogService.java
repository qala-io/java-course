package io.qala.javatraining.service;

import io.qala.javatraining.dao.DogDao;
import io.qala.javatraining.domain.Dog;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public class DogService {
    public DogService(DogDao dogDao) {
        this.dogDao = dogDao;
    }

    @Transactional public final Collection<Dog> getAllDogs() {
        return dogDao.getAllDogs();
    }

    @Transactional public final Dog getDog(String id) {
        return dogDao.getDog(id);
    }

    @Transactional public final Dog createDog(Dog dog) {
        return dogDao.createDog(dog);
    }

    @Transactional public final boolean deleteDog(String id) {
        return dogDao.deleteDog(id);
    }

    private final DogDao dogDao;
}
