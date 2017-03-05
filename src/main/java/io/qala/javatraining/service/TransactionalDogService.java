package io.qala.javatraining.service;

import io.qala.javatraining.dao.JdbcConnectionHolder;
import io.qala.javatraining.domain.Dog;

import java.util.Collection;

public class TransactionalDogService implements DogService {
    public TransactionalDogService(JdbcConnectionHolder connections, DogService toWrapWithTransactions) {
        this.connections = connections;
        this.toWrapWithTransactions = toWrapWithTransactions;
    }

    @Override public Collection<Dog> getAllDogs() {
        Collection<Dog> result;
        try {
            connections.beginTransaction();
            result = toWrapWithTransactions.getAllDogs();
            connections.commit();
        } catch (Throwable e) {
            connections.rollback();
            throw e;
        } finally { connections.closeCurrentConnection(); }
        return result;
    }

    @Override public Dog getDog(String id) {
        Dog result;
        try {
            connections.beginTransaction();
            result = toWrapWithTransactions.getDog(id);
            connections.commit();
        } catch (Throwable e) {
            connections.rollback();
            throw e;
        } finally { connections.closeCurrentConnection(); }
        return result;
    }

    @Override public Dog createDog(Dog dog) {
        Dog result;
        try {
            connections.beginTransaction();
            result = toWrapWithTransactions.createDog(dog);
            connections.commit();
        } catch (Throwable e) {
            connections.rollback();
            throw e;
        } finally { connections.closeCurrentConnection(); }
        return result;
    }

    @Override public boolean deleteDog(String id) {
        boolean result;
        try {
            connections.beginTransaction();
            result = toWrapWithTransactions.deleteDog(id);
            connections.commit();
        } catch (Throwable e) {
            connections.rollback();
            throw e;
        } finally { connections.closeCurrentConnection(); }
        return result;
    }

    private final JdbcConnectionHolder connections;
    private final DogService toWrapWithTransactions;
}
