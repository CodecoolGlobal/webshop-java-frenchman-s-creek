package com.codecool.shop.dao.implementation;

import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductUtil {
    static Product getProductFromResultSet(ResultSet resultSet) throws SQLException {
        ProductCategory productCategory = extractProductCategoryFromResultSet(resultSet);
        Supplier supplier = extractSupplierFromResultSet(resultSet);
        return extractProductFromResultSet(resultSet, supplier, productCategory);
    }

    static Product getProductFromResultSet(ResultSet resultSet, Supplier supplier) throws SQLException {
        ProductCategory productCategory = extractProductCategoryFromResultSet(resultSet);
        return extractProductFromResultSet(resultSet, supplier, productCategory);
    }

    static Product getProductFromResultSet(ResultSet resultSet, ProductCategory productCategory) throws SQLException {
        Supplier supplier = extractSupplierFromResultSet(resultSet);
        return extractProductFromResultSet(resultSet, supplier, productCategory);
    }

    private static Product extractProductFromResultSet(ResultSet resultSet, Supplier supplier, ProductCategory productCategory) throws SQLException {
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

    private static Supplier extractSupplierFromResultSet(ResultSet resultSet) throws SQLException {
        Supplier supplier = new Supplier(
                resultSet.getString("sup_name"),
                resultSet.getString("sup_desc")
        );
        supplier.setId(resultSet.getInt("sup_id"));
        return supplier;
    }

    private static ProductCategory extractProductCategoryFromResultSet(ResultSet resultSet) throws SQLException {
        ProductCategory productCategory = new ProductCategory(
                resultSet.getString("cat_name"),
                resultSet.getString("department"),
                resultSet.getString("cat_desc")
        );
        productCategory.setId(resultSet.getInt("cat_id"));
        return productCategory;
    }

    public static String generateTableColumnSelection(DBTable table) {
        String query = null;
        switch (table) {
            case PRODUCT:
                query = "p.id AS prod_id, p.name AS prod_name, p.description prod_desc, image, price, currency";
                break;
            case SUPPLIER:
                query = "s.id AS sup_id, s.name AS sup_name, s.description AS sup_desc";
                break;
            case CATEGORY:
                query = "c.id AS cat_id, c.name AS cat_name, department, c.description AS cat_desc ";
                break;
        }
        return query;
    }
}
