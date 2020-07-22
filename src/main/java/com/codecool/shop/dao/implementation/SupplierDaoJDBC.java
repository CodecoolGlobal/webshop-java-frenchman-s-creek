package com.codecool.shop.dao.implementation;

import com.codecool.shop.connection.dbConnection;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Supplier;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierDaoJDBC implements SupplierDao {
    private dbConnection connection = dbConnection.getInstance();
    private static SupplierDaoJDBC instance = null;

    private SupplierDaoJDBC() throws IOException {
    }

    public static SupplierDaoJDBC getInstance() throws IOException {
        if (instance == null) {
            instance = new SupplierDaoJDBC();
        }
        return instance;
    }

    @Override
    public void add(Supplier supplier) throws SQLException {
        Connection conn = connection.getConnection();
        assert conn != null;

        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO suppliers (name, description) VALUES (?, ?);"
        );
        stmt.setString(1, supplier.getName());
        stmt.setString(2, supplier.getDescription());

        stmt.executeUpdate();
    }

    @Override
    public Supplier find(int id) throws SQLException {
        Connection conn = connection.getConnection();
        assert conn != null;

        PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM suppliers WHERE id = ?;"
        );
        stmt.setInt(1, id);

        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            Supplier supplier = new Supplier(
                    resultSet.getString("name"),
                    resultSet.getString("description")
            );
            supplier.setId(id);
            return supplier;
        }
        return null;
    }

    @Override
    public Supplier findByName(String text) throws SQLException {
        Connection conn = connection.getConnection();
        assert conn != null;

        PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM suppliers WHERE name = ?;"
        );
        stmt.setString(1, text);

        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            Supplier supplier = new Supplier(
                    text,
                    resultSet.getString("description")
            );
            supplier.setId(resultSet.getInt("id"));
            return supplier;
        }
        return null;
    }

    @Override
    public void remove(int id) throws SQLException {
        Connection conn = connection.getConnection();
        assert conn != null;

        PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM suppliers WHERE id = ?;"
        );
        stmt.setInt(1, id);

        stmt.executeUpdate();
    }

    @Override
    public List<Supplier> getAll() throws SQLException {
        Connection conn = connection.getConnection();
        assert conn != null;

        PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM suppliers;"
        );

        ResultSet resultSet = stmt.executeQuery();
        List<Supplier> supplierList = new ArrayList<>();
        while(resultSet.next()) {
            Supplier supplier = new Supplier(
                    resultSet.getString("name"),
                    resultSet.getString("description")
            );
            supplier.setId(resultSet.getInt("id"));
            supplierList.add(supplier);
        }
        return supplierList;
    }
}
