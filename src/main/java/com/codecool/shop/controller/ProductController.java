package com.codecool.shop.controller;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.*;
import com.codecool.shop.model.*;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.model.Product;
import lombok.SneakyThrows;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/"})
public class ProductController extends HttpServlet {

    private ProductDao productDataStore;
    private ProductCategoryDao productCategoryDataStore;
    private SupplierDao supplierDataStore;
    private TemplateEngine engine;
    private WebContext context;

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        setup(req, resp);

        int orderProductCount = 0;
        Order order = null;
        HttpSession shoppingCartSession = req.getSession();
        try {
            order = (Order) shoppingCartSession.getAttribute("order");
            orderProductCount = order.getLineItemList().size();

        } catch (Exception ignored) {}

        context.setVariable("category", productCategoryDataStore.find(1));
        context.setVariable("products", productDataStore.getAll());
        context.setVariable("suppliers", supplierDataStore.getAll());
        context.setVariable("orderProductCount", orderProductCount);

        engine.process("product/index.html", context, resp.getWriter());
    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        setup(req, resp);

        context.setVariable("category", productCategoryDataStore.find(1));
        context.setVariable("products", productDataStore.getBy(productCategoryDataStore.find(1)));
        context.setVariable("suppliers", supplierDataStore.getAll());

        //get product Id from post
        String productIdString = req.getParameter("productId");


        // get product with posted Id
        Product product = productDataStore.find(Integer.parseInt(productIdString));

        HttpSession shoppingCartSession = req.getSession();
        Order order = null;
        try {
            order = (Order) shoppingCartSession.getAttribute("order");
            order.add(product);
        } catch (Exception e) {
            order = new Order();
            order.add(product);
        }
        context.setVariable("orderProductCount", order.getLineItemList().size());
        shoppingCartSession.setAttribute("order", order);
        engine.process("product/index.html", context, resp.getWriter());
    }

    private void setup(HttpServletRequest req, HttpServletResponse resp) {
        productDataStore = ProductDaoJDBC.getInstance();
        productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        supplierDataStore = SupplierDaoMem.getInstance();
        engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        context = new WebContext(req, resp, req.getServletContext());
    }
}
