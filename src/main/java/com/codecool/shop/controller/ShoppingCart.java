package com.codecool.shop.controller;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.OrderDaoMem;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
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
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/shopping-cart.html"})
public class ShoppingCart extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //ProductDao productDataStore = ProductDaoMem.getInstance();
        //ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        OrderDao orderDataStore = OrderDaoMem.getInstance();

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        //context.setVariable("category", productCategoryDataStore.find(1));
        //context.setVariable("products", productDataStore.getBy(productCategoryDataStore.find(1)));
        context.setVariable("order", orderDataStore.find(1));
        engine.process("product/shopping-cart.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductDao productDataStore = ProductDaoMem.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        OrderDao orderDataStore = OrderDaoMem.getInstance();

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        //context.setVariable("category", productCategoryDataStore.find(1));
        context.setVariable("products", productDataStore.getAll());


        //get first order
        Order order = orderDataStore.find(1);

        String prodIdString = req.getParameter("prodId");
        Product product = productDataStore.find(Integer.parseInt(prodIdString));

        String prodQuantityString = req.getParameter("prodQuantity");
        int quantity = Integer.parseInt(prodQuantityString);
        order.remove(product);
        for (int i = 1; i <= quantity; i++) {
            order.add(product);
        }

        context.setVariable("order", order);
        engine.process("product/shopping-cart.html", context, resp.getWriter());
    }
}
