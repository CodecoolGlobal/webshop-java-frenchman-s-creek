package com.codecool.shop.controller;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.*;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.Product;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/"})
public class ProductController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductDao productDataStore = ProductDaoJDBC.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        SupplierDao supplierDataStore = SupplierDaoMem.getInstance();
        OrderDao orderDataStore = OrderDaoMem.getInstance();
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        try {
            context.setVariable("category", productCategoryDataStore.find(1));
            context.setVariable("products", productDataStore.getAll());
            context.setVariable("suppliers", supplierDataStore.getAll());
            Order order = orderDataStore.find(1);
            if (order != null) {
                context.setVariable("orderProductCount", order.getItemsNr());
            }
        } catch (Exception e) {
            context.setVariable("dbError", "Invalid database operation!");
            System.out.println(e.getMessage());
        }

        engine.process("product/index.html", context, resp.getWriter());
    }

  
    

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductDao productDataStore = ProductDaoMem.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        OrderDao orderDataStore = OrderDaoMem.getInstance();
        SupplierDao supplierDataStore = SupplierDaoMem.getInstance();
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        try {
            context.setVariable("category", productCategoryDataStore.find(1));
            context.setVariable("products", productDataStore.getBy(productCategoryDataStore.find(1)));
            context.setVariable("suppliers", supplierDataStore.getAll());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            context.setVariable("dbError", "Invalid database operation!");
        }

        //get first order
        Order order = orderDataStore.find(1);

        //get product Id from post
        String productIdString = req.getParameter("productId");

        try {
            //get product with posted Id
            Product product = productDataStore.find(Integer.parseInt(productIdString));

            //add product to order
            order.add(product);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            context.setVariable("dbError", "Invalid product found in database!");
        }

        engine.process("product/index.html", context, resp.getWriter());
    }
}
