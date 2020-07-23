package com.codecool.shop.dao.implementation;

import com.codecool.shop.connection.dbConnection;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.model.ProductCategory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductCategoryDaoJDBC implements ProductCategoryDao {
    private dbConnection connection = dbConnection.getInstance();
    private static ProductCategoryDaoJDBC instance = null;

    private ProductCategoryDaoJDBC() throws IOException {
    }

    public static ProductCategoryDaoJDBC getInstance() throws IOException {
        if (instance == null) {
            instance = new ProductCategoryDaoJDBC();
        }
        return instance;
    }

    @Override
    public void add(ProductCategory category) throws SQLException {
        Connection conn = connection.getConnection();
        assert conn != null;

        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO categories (name, department, description) VALUES (?, ?, ?);"
        );
        stmt.setString(1, category.getName());
        stmt.setString(2, category.getDepartment());
        stmt.setString(3, category.getDescription());

        stmt.executeUpdate();
    }

    @Override
    public ProductCategory find(int id) throws SQLException {
        Connection conn = connection.getConnection();
        assert conn != null;

        PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM categories WHERE id = ?;"
        );
        stmt.setInt(1, id);

        ResultSet resultSet = stmt.executeQuery();

        if(resultSet.next()) {
            ProductCategory productCategory = new ProductCategory(
                    resultSet.getString("name"),
                    resultSet.getString("department"),
                    resultSet.getString("description")
            );
            productCategory.setId(id);
            return productCategory;
        }
        return null;
    }

    @Override
    public ProductCategory findByName(String text) throws SQLException {
        Connection conn = connection.getConnection();
        assert conn != null;

        PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM categories WHERE name = ?;"
        );
        stmt.setString(1, text);

        ResultSet resultSet = stmt.executeQuery();

        if(resultSet.next()) {
            ProductCategory productCategory = new ProductCategory(
                    text,
                    resultSet.getString("department"),
                    resultSet.getString("description")
            );
            productCategory.setId(resultSet.getInt("id"));
            return productCategory;
        }
        return null;
    }

    @Override
    public void remove(int id) throws SQLException {
        Connection conn = connection.getConnection();
        assert conn != null;

        PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM categories WHERE id = ?;"
        );
        stmt.setInt(1, id);

        stmt.executeUpdate();
    }

    @Override
    public List<ProductCategory> getAll() throws SQLException {
        Connection conn = connection.getConnection();
        assert conn != null;

        PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM categories;"
        );

        ResultSet resultSet = stmt.executeQuery();
        List<ProductCategory> productCategoryList = new ArrayList<>();
        while (resultSet.next()) {
            ProductCategory productCategory = new ProductCategory(
                    resultSet.getString("name"),
                    resultSet.getString("department"),
                    resultSet.getString("description")
            );
            productCategory.setId(resultSet.getInt("id"));
            productCategoryList.add(productCategory);
        }
        return productCategoryList;
    }
}
