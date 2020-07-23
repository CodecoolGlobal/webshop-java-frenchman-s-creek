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
    private static ProductDaoJDBC instance = null;

    public static ProductDaoJDBC getInstance() {
        return instance == null ? new ProductDaoJDBC() : instance;
    }

    @Override
    public void add(Product product) throws SQLException {
        String sql = String.format(
                "INSERT INTO products (supplier_id, category_id, name, description, image, price, currency) " +
                        "VALUES (%d, %d, '%s', '%s', '%s', %.2f, '%s');",
                product.getSupplier().getId(), product.getProductCategory().getId(), product.getName(), product.getDescription(),
                product.getImageFileName(), product.getDefaultPrice(), product.getDefaultCurrency() + ""
        );
        DatabaseManager.execUpdate(sql);
    }

    @Override
    public Product find(int id) throws SQLException {
        String sql = String.format(
                "SELECT " +
                        ProductUtil.generateTableColumnSelection(DBTable.PRODUCT) + ", " +
                        ProductUtil.generateTableColumnSelection(DBTable.SUPPLIER) + ", " +
                        ProductUtil.generateTableColumnSelection(DBTable.CATEGORY) +
                        " FROM products AS p " +
                        "JOIN suppliers AS s ON p.supplier_id = s.id " +
                        "JOIN categories AS c on p.category_id = c.id " +
                        "WHERE p.id=%d;", id);
        ResultSet resultSet = DatabaseManager.execQuery(sql);
        return resultSet.next() ? ProductUtil.getProductFromResultSet(resultSet) : null;
    }

    @Override
    public void remove(int id) throws SQLException {
        String sql = String.format("DELETE FROM products WHERE id=%d", id);
        DatabaseManager.execUpdate(sql);
    }

    @Override
    public List<Product> getAll() throws SQLException {
        String sql = "SELECT " +
                ProductUtil.generateTableColumnSelection(DBTable.PRODUCT) + ", " +
                ProductUtil.generateTableColumnSelection(DBTable.SUPPLIER) + ", " +
                ProductUtil.generateTableColumnSelection(DBTable.CATEGORY) +
                " FROM products AS p " +
                "JOIN suppliers AS s ON p.supplier_id = s.id " +
                "JOIN categories AS c on p.category_id = c.id;";
        ResultSet resultSet = DatabaseManager.execQuery(sql);

        List<Product> data = new ArrayList<>();
        while (resultSet.next()) {
            Product product = ProductUtil.getProductFromResultSet(resultSet);
            data.add(product);
        }
        return data;
    }

    @Override
    public List<Product> getBy(Supplier supplier) throws SQLException {
        String sql = String.format(
                "SELECT " +
                        ProductUtil.generateTableColumnSelection(DBTable.PRODUCT) + ", " +
                        ProductUtil.generateTableColumnSelection(DBTable.CATEGORY) +
                        " FROM products AS p " +
                        "JOIN categories AS c on p.category_id = c.id " +
                        "WHERE supplier_id = %d;",
                supplier.getId()
        );
        ResultSet resultSet = DatabaseManager.execQuery(sql);
        List<Product> productList = new ArrayList<>();
        while (resultSet.next()) {
            Product product = ProductUtil.getProductFromResultSet(resultSet, supplier);
            productList.add(product);
        }
        return productList;
    }

    @Override
    public List<Product> getBy(ProductCategory productCategory) throws SQLException {
        String sql = String.format(
                "SELECT " +
                        ProductUtil.generateTableColumnSelection(DBTable.PRODUCT) + ", " +
                        ProductUtil.generateTableColumnSelection(DBTable.SUPPLIER) +
                        " FROM products AS p " +
                        "JOIN categories AS c on p.category_id = c.id " +
                        "WHERE supplier_id = %d;",
                productCategory.getId()
        );
        ResultSet resultSet = DatabaseManager.execQuery(sql);
        List<Product> productList = new ArrayList<>();
        while (resultSet.next()) {
            Product product = ProductUtil.getProductFromResultSet(resultSet, productCategory);
            productList.add(product);
        }
        return productList;
    }


}
