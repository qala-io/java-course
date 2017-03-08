package io.qala.javatraining.dao;

import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;

@SuppressWarnings(/*This class has public methods but they are used only in current package as for now*/"WeakerAccess")
public class JdbcConnectionHolder {
    public JdbcConnectionHolder(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getCurrentConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }

    private final DataSource dataSource;
}
