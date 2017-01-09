package io.qala.javatraining.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SuppressWarnings(/*This class has public methods but they are used only in current package as for now*/"WeakerAccess")
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
        } catch (SQLException e) { throw new RuntimeException(e); }
        connections.remove();
    }

    public Connection beginTransaction() {
        Connection connection = getCurrentConnection();
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            closeCurrentConnection();
            throw new RuntimeException(e);
        }
        return connection;
    }

    public void rollback() {
        try {
            getCurrentConnection().rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void commit() {
        try {
            getCurrentConnection().commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private final DataSource dataSource;
    private final ThreadLocal<Connection> connections = new ThreadLocal<>();
}
