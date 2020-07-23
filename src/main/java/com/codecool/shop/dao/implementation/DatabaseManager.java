package com.codecool.shop.dao.implementation;

import com.codecool.shop.connection.dbConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static Connection conn;
    private static Statement stmt;

    public static void execUpdate(String sql) throws SQLException {
        prepareExecution();
        stmt.executeUpdate(sql);
        conn.close();
    }

    public static ResultSet execQuery(String sql) throws SQLException {
        prepareExecution();
        ResultSet rs = stmt.executeQuery(sql);
        conn.close();
        return rs;
    }

    private static void prepareExecution() throws SQLException {
        conn = dbConnection.getConnection();
        assert conn != null;
        stmt = conn.createStatement();
    }
}
