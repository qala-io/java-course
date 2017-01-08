package io.qala.javatraining.dao;

import io.qala.javatraining.domain.Dog;
import io.qala.javatraining.domain.ObjectNotFoundException;
import org.h2.jdbcx.JdbcDataSource;

import java.sql.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;

import static io.qala.datagen.RandomShortApi.english;

@SuppressWarnings(/*Can't configure datasource for IntelliJ to recognize H2 Tables*/"SqlResolve")
public class JdbcDogDao implements DogDao {

    public JdbcDogDao() {
        dataSource = new JdbcDataSource();
        // Tests create multiple instances of JdbcDogDao which would have table created every time it's instantiated.
        // Which would fail. So we want to separate each instance of DAO by creating different DB every time it's created.
        String dbName = english(10);
        dataSource.setUrl("jdbc:h2:mem:" + dbName + ";DB_CLOSE_DELAY=-1");
        dataSource.setUser("sa");
        dataSource.setPassword("");
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(CREATE_DOG_TABLE_DDL);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override public Collection<Dog> getAllDogs() {
        Collection<Dog> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery("select * from DOG");
                while (rs.next()) result.add(createDogFromCurrentRecord(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override public Dog getDog(String id) {
        try (Connection connection = dataSource.getConnection()) {
            Dog dog = null;
            try (PreparedStatement statement = connection.prepareStatement("select * from DOG where ID = ?")) {
                statement.setString(1, id);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) dog = createDogFromCurrentRecord(rs);
            }
            if (dog != null) return dog;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new ObjectNotFoundException(Dog.class, id);
    }

    @Override public Dog createDog(Dog dog) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("insert into DOG values(?, ?, ?, ?, ?)")) {
                statement.setString(1, dog.getId());
                statement.setString(2, dog.getName());
                if (dog.getTimeOfBirth() != null)   statement.setTimestamp(3, Timestamp.from(dog.getTimeOfBirth().toInstant()));
                else                                statement.setTimestamp(3, null);
                statement.setDouble(4, dog.getHeight());
                statement.setDouble(5, dog.getWeight());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dog;
    }

    @Override public boolean deleteDog(String id) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("delete from DOG where ID = ?")) {
                statement.setString(1, id);
                return statement.executeUpdate() > 0;
            }
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

    private final JdbcDataSource dataSource;
    private static final String CREATE_DOG_TABLE_DDL = "create table DOG (\n" +
            "  ID varchar(36) primary key,\n" +
            "  NAME nvarchar(1000) not null,\n" +
            "  BIRTH_TIME timestamp default sysdate,\n" +
            "  HEIGHT double,\n" +
            "  WEIGHT double\n" +
            ");";
}
