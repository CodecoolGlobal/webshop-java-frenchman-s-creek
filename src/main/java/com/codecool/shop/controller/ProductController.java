package com.codecool.shop.controller;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.OrderDaoMem;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.dao.implementation.SupplierDaoMem;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/"})
public class ProductController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductDao productDataStore = ProductDaoMem.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        SupplierDao supplierDataStore = SupplierDaoMem.getInstance();

        OrderDao orderDataStore = OrderDaoMem.getInstance();

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        context.setVariable("category", productCategoryDataStore.find(1));
        context.setVariable("products", productDataStore.getBy(productCategoryDataStore.find(1)));
        context.setVariable("suppliers", supplierDataStore.getAll());
        context.setVariable("order", orderDataStore.find(1));
        // // Alternative setting of the template context:
        // Map<String, Object> params = new HashMap<>();
        // params.put("category", productCategoryDataStore.find(1));
        // params.put("products", productDataStore.getBy(productCategoryDataStore.find(1)));
        // context.setVariables(params);

       
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
        context.setVariable("category", productCategoryDataStore.find(1));
        
        context.setVariable("suppliers", supplierDataStore.getAll());


        //get first order
        Order order = orderDataStore.find(1);

        //get product Id from post
        String productIdString = req.getParameter("productId");

        //get product with posted Id
        Product product = productDataStore.find(Integer.parseInt(productIdString));

        //add product to order
        order.add(product);

        //add order to context
        context.setVariable("order", order);

        // String categoryType = req.getParameter("sort_category");
        // System.out.println(categoryType);
        // ProductCategory cat = productCategoryDataStore.findByName(categoryType);
        // System.out.println(cat);
        // String supplierType = req.getParameter("sort_supplier");
        // System.out.println(supplierType);
        // Supplier sup = supplierDataStore.findByName(supplierType);
        // System.out.println(sup);
        // if(categoryType.equals("Tablet")){
        //     context.setVariable("products", productDataStore.getBy(cat));
        //     System.out.println(productDataStore.getBy(cat));
        // }
        // if(supplierType.equals("Amazon") || supplierType.equals("Lenovo")){
        //     context.setVariable("products", productDataStore.getBy(sup));
        //     System.out.println(productDataStore.getBy(sup));
        // }
        

        //send context to template
        engine.process("product/index.html", context, resp.getWriter());
    }
}
