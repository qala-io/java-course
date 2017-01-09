package io.qala.javatraining.dao;

import io.qala.javatraining.domain.Dog;
import io.qala.javatraining.domain.ObjectNotFoundException;

import java.sql.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

@SuppressWarnings(/*Can't configure datasource for IntelliJ to recognize H2 Tables*/"SqlResolve")
public class JdbcDogDao implements DogDao {

    public JdbcDogDao(JdbcConnectionHolder connections) {
        this.connections = connections;
    }

    @Override public Collection<Dog> getAllDogs() {
        Collection<Dog> result = new ArrayList<>();
        Connection connection = connections.getCurrentConnection();
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM DOG");
            while (rs.next()) result.add(createDogFromCurrentRecord(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override public Dog getDog(String id) {
        Connection connection = connections.getCurrentConnection();
        Dog dog = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM DOG WHERE ID = ?")) {
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) dog = createDogFromCurrentRecord(rs);
            if (dog != null) return dog;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new ObjectNotFoundException(Dog.class, id);
    }

    @Override public Dog createDog(Dog dog) {
        Connection connection = connections.getCurrentConnection();
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO DOG VALUES(?, ?, ?, ?, ?)")) {
            statement.setString(1, dog.getId());
            statement.setString(2, dog.getName());
            if (dog.getTimeOfBirth() != null)   statement.setTimestamp(3, Timestamp.from(dog.getTimeOfBirth().toInstant()));
            else                                statement.setTimestamp(3, null);
            statement.setDouble(4, dog.getHeight());
            statement.setDouble(5, dog.getWeight());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dog;
    }

    @Override public boolean deleteDog(String id) {
        Connection connection = connections.getCurrentConnection();
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM DOG WHERE ID = ?")) {
            statement.setString(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Dog createDogFromCurrentRecord(ResultSet rs) throws SQLException {
        Dog dog = new Dog();
        dog.setId(rs.getString("ID"));
        dog.setName(rs.getString("NAME"));
        Timestamp birthTime = rs.getTimestamp("BIRTH_TIME");
        if (birthTime != null) dog.setTimeOfBirth(OffsetDateTime.ofInstant(birthTime.toInstant(), ZoneOffset.UTC));
        dog.setHeight(rs.getDouble("HEIGHT"));
        dog.setWeight(rs.getDouble("WEIGHT"));
        return dog;
    }

    private final JdbcConnectionHolder connections;
}
