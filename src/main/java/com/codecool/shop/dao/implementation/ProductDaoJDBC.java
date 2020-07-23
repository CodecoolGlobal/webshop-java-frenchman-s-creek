package com.codecool.shop.dao.implementation;

import com.codecool.shop.connection.dbConnection;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import jdk.javadoc.internal.doclets.toolkit.taglets.UserTaglet;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoJDBC implements ProductDao {

    private dbConnection connection = dbConnection.getInstance();
    private static ProductDaoJDBC instance = null;

    private ProductDaoJDBC() throws IOException {
    }

    public static ProductDaoJDBC getInstance() throws IOException {
        if (instance == null) {
            instance = new ProductDaoJDBC();
        }
        return instance;
    }

    @Override
    public void add(Product product) throws SQLException {
        Connection conn = connection.getConnection();
        assert conn != null;

        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO products (supplier_id, category_id, name, description, image, price, currency) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?);"
        );
        stmt.setInt(1, product.getSupplier().getId());
        stmt.setInt(2, product.getProductCategory().getId());
        stmt.setString(3, product.getName());
        stmt.setString(4, product.getDescription());
        stmt.setString(5, product.getImageFileName());
        stmt.setFloat(6, product.getDefaultPrice());
        stmt.setString(7, product.getDefaultCurrency() + "");

        stmt.executeUpdate();
    }

    @Override
    public Product find(int id) throws SQLException {
        Connection conn = connection.getConnection();
        assert conn != null;

        PreparedStatement stmt = conn.prepareStatement(
                "SELECT p.id AS prod_id, p.name AS prod_name, p.description prod_desc, image, price, currency, " +
                        "s.id AS sup_id, s.name AS sup_name, s.description AS sup_desc, " +
                        "c.id AS cat_id, c.name AS cat_name, department, c.description AS cat_desc " +
                        "FROM products AS p " +
                        "JOIN suppliers AS s ON p.supplier_id = s.id " +
                        "JOIN categories AS c on p.category_id = c.id " +
                        "WHERE p.id = ?;"
        );
        stmt.setInt(1, id);

        ResultSet resultSet = stmt.executeQuery();

        if (resultSet.next()) {
            ProductCategory productCategory = new ProductCategory(
                    resultSet.getString("cat_name"),
                    resultSet.getString("department"),
                    resultSet.getString("cat_desc")
            );
            productCategory.setId(resultSet.getInt("cat_id"));
            Supplier supplier = new Supplier(
                    resultSet.getString("sup_name"),
                    resultSet.getString("sup_desc")
            );
            supplier.setId(resultSet.getInt("sup_id"));
            Product product = new Product(
                    resultSet.getString("prod_name"),
                    resultSet.getFloat("price"),
                    resultSet.getString("currency"),
                    resultSet.getString("prod_desc"),
                    resultSet.getString("image"),
                    productCategory,
                    supplier
            );
            product.setId(resultSet.getInt("prod_id"));
            return product;
        }
        return null;
    }

    @Override
    public void remove(int id) throws SQLException {
        Connection conn = connection.getConnection();
        assert conn != null;

        PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM products WHERE id = ?;"
        );
        stmt.setInt(1, id);

        stmt.executeUpdate();
    }

    @Override
    public List<Product> getAll() throws SQLException {
        Connection conn = connection.getConnection();
        assert conn != null;

        PreparedStatement stmt = conn.prepareStatement(
                "SELECT p.id AS prod_id, p.name AS prod_name, p.description prod_desc, image, price, currency, " +
                        "s.id AS sup_id, s.name AS sup_name, s.description AS sup_desc, " +
                        "c.id AS cat_id, c.name AS cat_name, department, c.description AS cat_desc " +
                        "FROM products AS p " +
                        "JOIN suppliers AS s ON p.supplier_id = s.id " +
                        "JOIN categories AS c on p.category_id = c.id;"
        );
        ResultSet resultSet = stmt.executeQuery();

        List<Product> data = new ArrayList<>();
        while (resultSet.next()) {
            ProductCategory productCategory = new ProductCategory(
                    resultSet.getString("cat_name"),
                    resultSet.getString("department"),
                    resultSet.getString("cat_desc")
            );
            productCategory.setId(resultSet.getInt("cat_id"));
            Supplier supplier = new Supplier(
                    resultSet.getString("sup_name"),
                    resultSet.getString("sup_desc")
            );
            supplier.setId(resultSet.getInt("sup_id"));
            Product product = new Product(
                    resultSet.getString("prod_name"),
                    resultSet.getFloat("price"),
                    resultSet.getString("currency"),
                    resultSet.getString("prod_desc"),
                    resultSet.getString("image"),
                    productCategory,
                    supplier
            );
            product.setId(resultSet.getInt("prod_id"));
            data.add(product);
        }
        return data;
    }

    @Override
    public List<Product> getBy(Supplier supplier) throws SQLException {
        Connection conn = connection.getConnection();
        assert conn != null;

        PreparedStatement stmt = conn.prepareStatement(
                "SELECT p.id AS prod_id, p.name AS prod_name, p.description prod_desc, image, price, currency, " +
                        "c.id AS cat_id, c.name AS cat_name, department, c.description AS cat_desc " +
                        "FROM products AS p " +
                        "JOIN categories AS c on p.category_id = c.id " +
                        "WHERE supplier_id = ?;"
        );
        stmt.setInt(1, supplier.getId());

        ResultSet resultSet = stmt.executeQuery();
        List<Product> productList = new ArrayList<>();
        while (resultSet.next()) {
            ProductCategory productCategory = new ProductCategory(
                    resultSet.getString("cat_name"),
                    resultSet.getString("department"),
                    resultSet.getString("cat_desc")
            );
            productCategory.setId(resultSet.getInt("cat_id"));
            Product product = new Product(
                    resultSet.getString("prod_name"),
                    resultSet.getFloat("price"),
                    resultSet.getString("currency"),
                    resultSet.getString("prod_desc"),
                    resultSet.getString("image"),
                    productCategory,
                    supplier
            );
            product.setId(resultSet.getInt("prod_id"));
            productList.add(product);
        }
        return productList;
    }

    @Override
    public List<Product> getBy(ProductCategory productCategory) throws SQLException {
        Connection conn = connection.getConnection();
        assert conn != null;

        PreparedStatement stmt = conn.prepareStatement(
                "SELECT p.id AS prod_id, p.name AS prod_name, p.description prod_desc, image, price, currency, " +
                        "s.id AS sup_id, s.name AS sup_name, s.description AS sup_desc " +
                        "FROM products AS p " +
                        "JOIN suppliers AS s on p.supplier_id = s.id " +
                        "WHERE category_id = ?;"
        );
        stmt.setInt(1, productCategory.getId());

        ResultSet resultSet = stmt.executeQuery();
        List<Product> productList = new ArrayList<>();
        while (resultSet.next()) {
            Supplier supplier = new Supplier(
                    resultSet.getString("sup_name"),
                    resultSet.getString("sup_desc")
            );
            supplier.setId(resultSet.getInt("sup_id"));
            Product product = new Product(
                    resultSet.getString("prod_name"),
                    resultSet.getFloat("price"),
                    resultSet.getString("currency"),
                    resultSet.getString("prod_desc"),
                    resultSet.getString("image"),
                    productCategory,
                    supplier
            );
            product.setId(resultSet.getInt("prod_id"));
            productList.add(product);
        }
        return productList;
    }
}
