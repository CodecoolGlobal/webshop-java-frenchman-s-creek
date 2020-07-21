package com.codecool.shop.connection;


import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.LogManager;


public class dbConnection {
    private static final Properties dbProperties = new Properties();
    private static dbConnection instance;

    private dbConnection() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("connection.properties");
        dbProperties.load(inputStream);
    }

    public static dbConnection getInstance() throws IOException {
        if (instance == null) instance = new dbConnection();
        return instance;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/codecoolshop", "postgres",
                ("1234"));
    }



}
