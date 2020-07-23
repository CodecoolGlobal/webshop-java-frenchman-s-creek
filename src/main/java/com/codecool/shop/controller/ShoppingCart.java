package com.codecool.shop.controller;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.OrderDaoMem;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoJDBC;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.model.LineItem;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.Product;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/shopping-cart"})
public class ShoppingCart extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        // Get all products from shopping cart session
        try {
            HttpSession shoppingCartSession = req.getSession();
            Order order = (Order) shoppingCartSession.getAttribute("order");
            context.setVariable("order", order);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            context.setVariable("order", null);
        }

        engine.process("product/shopping-cart.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductDao productDataStore = ProductDaoJDBC.getInstance();
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        try {
            context.setVariable("products", productDataStore.getAll());
        } catch (SQLException e) {
            context.setVariable("dbError", "Invalid database operation!");
        }


        HttpSession shoppingCartSession = req.getSession();
        Order order = (Order) shoppingCartSession.getAttribute("order");

        String temp = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(temp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert json != null;

        try {
            String prodIdString = (String) json.get("prodId");
            Product product = productDataStore.find(Integer.parseInt(prodIdString));

            String prodQuantityString = (String) json.get("quantity");
            int quantity = Integer.parseInt(prodQuantityString);
            order.remove(product);
            for (int i = 1; i <= quantity; i++) {
                order.add(product);
            }
        } catch (SQLException e) {
            context.setVariable("dbError", "Invalid database operation!");
        }

        context.setVariable("order", order);
        engine.process("product/shopping-cart.html", context, resp.getWriter());
    }
}
