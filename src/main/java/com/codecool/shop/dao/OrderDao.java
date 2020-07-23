package com.codecool.shop.dao;


import com.codecool.shop.dao.implementation.ProductCategoryDaoJDBC;
import com.codecool.shop.dao.implementation.SupplierDaoJDBC;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.Product;

import java.sql.SQLException;
import java.util.List;

public interface OrderDao {
    void add(Order order, Product product) throws SQLException;

    Order addNewOrderRecord() throws SQLException;

    boolean orderExists(int id) throws SQLException;

    Order find(int id, ProductCategoryDaoJDBC productCategoryDaoJDBC, SupplierDaoJDBC supplierDaoJDBC) throws SQLException;

    void remove(int id) throws SQLException;

    List<Order> getAll(ProductCategoryDaoJDBC productCategoryDaoJDBC, SupplierDaoJDBC supplierDaoJDBC) throws SQLException;
}
