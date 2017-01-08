package io.qala.javatraining.service;

import io.qala.javatraining.dao.ConnectionHolder;
import io.qala.javatraining.domain.Dog;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

public class TransactionalDogService implements DogService {
    public TransactionalDogService(ConnectionHolder connections, DogService toWrapWithTransactions) {
        this.connections = connections;
        this.toWrapWithTransactions = toWrapWithTransactions;
    }

    @Override public Collection<Dog> getAllDogs() {
        Connection connection = beginTransaction();
        Collection<Dog> result;
        try {
            result = toWrapWithTransactions.getAllDogs();
            commit(connection);
        } catch (Throwable e) {
            rollback(connection);
            throw e;
        } finally {
            connections.closeCurrentConnection();
        }
        return result;
    }

    @Override public Dog getDog(String id) {
        Connection connection = beginTransaction();
        Dog result;
        try {
            result = toWrapWithTransactions.getDog(id);
            commit(connection);
        } catch (Throwable e) {
            rollback(connection);
            throw e;
        } finally {
            connections.closeCurrentConnection();
        }
        return result;
    }

    @Override public Dog createDog(Dog dog) {
        Connection connection = beginTransaction();
        Dog result;
        try {
            result = toWrapWithTransactions.createDog(dog);
            commit(connection);
        } catch (Throwable e) {
            rollback(connection);
            throw e;
        } finally {
            connections.closeCurrentConnection();
        }
        return result;
    }

    @Override public boolean deleteDog(String id) {
        Connection connection = beginTransaction();
        boolean result;
        try {
            result = toWrapWithTransactions.deleteDog(id);
            commit(connection);
        } catch (Throwable e) {
            rollback(connection);
            throw e;
        } finally {
            connections.closeCurrentConnection();
        }
        return result;
    }

    private Connection beginTransaction() {
        Connection connection = connections.getCurrentConnection();
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            connections.closeCurrentConnection();
            throw new RuntimeException(e);
        }
        return connection;
    }
    private static void rollback(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private static void commit(Connection connection) {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private final ConnectionHolder connections;
    private final DogService toWrapWithTransactions;
}
