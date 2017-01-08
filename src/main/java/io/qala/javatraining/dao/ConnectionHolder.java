package io.qala.javatraining.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionHolder {
    public ConnectionHolder(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getCurrentConnection() {
        Connection connection = connections.get();
        if(connection == null) {
            try {
                connection = dataSource.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            connections.set(connection);
        }
        return connection;
    }
    public void closeCurrentConnection() {
        Connection connection = connections.get();
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        connections.remove();
    }

    private final DataSource dataSource;
    private final ThreadLocal<Connection> connections = new ThreadLocal<>();
}
