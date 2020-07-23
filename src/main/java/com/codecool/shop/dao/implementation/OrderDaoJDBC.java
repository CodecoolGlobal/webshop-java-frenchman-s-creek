package com.codecool.shop.dao.implementation;

import com.codecool.shop.connection.dbConnection;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.LineItem;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.Product;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoJDBC implements OrderDao {
    private dbConnection connection = dbConnection.getInstance();
    private static OrderDaoJDBC instance = null;

    private OrderDaoJDBC() throws IOException {
    }

    public static OrderDaoJDBC getInstance() throws IOException {
        if (instance == null) {
            instance = new OrderDaoJDBC();
        }
        return instance;
    }

    @Override
    public Order addNewOrderRecord() throws SQLException {
        Connection conn = connection.getConnection();
        assert conn != null;

        Statement stmt = conn.createStatement();
        stmt.executeUpdate("INSERT INTO orders DEFAULT VALUES;", Statement.RETURN_GENERATED_KEYS);
        ResultSet resultSet = stmt.getGeneratedKeys();

        if (resultSet.next()) {
            Order order = new Order();
            order.setId(resultSet.getInt(1));
            return order;
        }
        return null;
    }

    @Override
    public void add(Order order, Product product) throws SQLException {
        Connection conn = connection.getConnection();
        assert conn != null;

        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO orders_items (order_id, product_id, product_quantity) " +
                        "VALUES (?, ?, ?);"
        );
        stmt.setInt(1, order.getId());
        stmt.setInt(2, product.getId());
        stmt.setInt(3, 1);

        stmt.executeUpdate();
    }

    @Override
    public boolean orderExists(int id) throws SQLException {
        Connection conn = connection.getConnection();
        assert conn != null;

        PreparedStatement stmt = conn.prepareStatement(
                "SELECT 1 FROM orders WHERE id = ?;"
        );
        stmt.setInt(1, id);

        ResultSet resultSet = stmt.executeQuery();

        return resultSet.next();
    }

    @Override
    public Order find(int id, ProductCategoryDaoJDBC productCategoryDaoJDBC, SupplierDaoJDBC supplierDaoJDBC) throws SQLException {
        Connection conn = connection.getConnection();
        assert conn != null;

        PreparedStatement stmt = conn.prepareStatement(
                "SELECT orders_items.*, products.* " +
                        "FROM orders " +
                        "JOIN orders_items ON orders.id = orders_items.order_id " +
                        "JOIN products ON orders_items.product_id = products.id " +
                        "WHERE orders.id = ?;"
        );
        stmt.setInt(1, id);
        ResultSet resultSet = stmt.executeQuery();

        List<LineItem> lineItemList = new ArrayList<>();
        while (resultSet.next()) {
            Product product = new Product(
                    resultSet.getString("name"),
                    resultSet.getFloat("price"),
                    resultSet.getString("currency"),
                    resultSet.getString("description"),
                    resultSet.getString("image"),
                    productCategoryDaoJDBC.find(resultSet.getInt("category_id")),
                    supplierDaoJDBC.find(resultSet.getInt("supplier_id"))
            );
            LineItem lineItem = new LineItem(product);
            lineItemList.add(lineItem);
        }
        Order order = new Order();
        if (lineItemList.size() > 0) {
            order.setId(resultSet.getInt("order_id"));
        }
        order.setLineItemList(lineItemList);

        return order;
    }

    @Override
    public void remove(int id) throws SQLException {
        Connection conn = connection.getConnection();
        assert conn != null;

        PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM orders_items WHERE order_id = ?;"
        );
        stmt.setInt(1, id);
        stmt.executeUpdate();
        stmt = conn.prepareStatement(
                "DELETE FROM orders WHERE id = ?;"
        );
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }

    @Override
    public List<Order> getAll(ProductCategoryDaoJDBC productCategoryDaoJDBC, SupplierDaoJDBC supplierDaoJDBC) throws SQLException {
        Connection conn = connection.getConnection();
        assert conn != null;

        PreparedStatement stmt = conn.prepareStatement(
                "SELECT orders_items.*, products.* " +
                        "FROM orders " +
                        "JOIN orders_items ON orders.id = orders_items.order_id " +
                        "JOIN products ON orders_items.product_id = products.id;"
        );
        ResultSet resultSet = stmt.executeQuery();

        List<Order> orderList = new ArrayList<>();
        List<LineItem> lineItemList = new ArrayList<>();

        while (resultSet.next()) {
            Product product = new Product(
                    resultSet.getString("name"),
                    resultSet.getFloat("price"),
                    resultSet.getString("currency"),
                    resultSet.getString("description"),
                    resultSet.getString("image"),
                    productCategoryDaoJDBC.find(resultSet.getInt("category_id")),
                    supplierDaoJDBC.find(resultSet.getInt("supplier_id"))
            );
            LineItem lineItem = new LineItem(product);
            lineItemList.add(lineItem);
        }
        Order order = new Order();
        order.setId(resultSet.getInt("order_id"));
        order.setLineItemList(lineItemList);

        orderList.add(order);

        return orderList;
    }
}
