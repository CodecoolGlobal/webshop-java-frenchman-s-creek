package com.codecool.shop.controller;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.OrderDaoJDBC;
import com.codecool.shop.dao.implementation.ProductCategoryDaoJDBC;
import com.codecool.shop.dao.implementation.SupplierDaoJDBC;
import com.codecool.shop.email.Mailer;
import com.codecool.shop.payment.CreditCard;
import com.codecool.shop.payment.StripePayment;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.customer_orders.OrderToJSON;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = {"/payment-page.html"})
public class PaymentPage extends HttpServlet {

    private static final long serialVersionUID = 1L;
    TemplateEngine engine;
    WebContext context;
    OrderDao orderDataStore;
    ProductCategoryDao productCategoryDataStore = ProductCategoryDaoJDBC.getInstance();
    SupplierDao supplierDataStore = SupplierDaoJDBC.getInstance();

    public PaymentPage() throws IOException {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        orderDataStore = OrderDaoJDBC.getInstance();
        engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        context = new WebContext(req, resp, req.getServletContext());

        try {
            context.setVariable("orderProducts", orderDataStore.find(1, (ProductCategoryDaoJDBC) productCategoryDataStore,
                    (SupplierDaoJDBC) supplierDataStore).getLineItems());
            context.setVariable("order", orderDataStore.find(1, (ProductCategoryDaoJDBC) productCategoryDataStore,
                    (SupplierDaoJDBC) supplierDataStore));
        } catch (Exception e) {
            e.printStackTrace();
        }
        engine.process("product/payment-page.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cardHolder = req.getParameter("card-holder");
        String cardNumber = req.getParameter("card-number");
        String expMonth = req.getParameter("exp-month");
        String expYear = req.getParameter("exp-year");
        String cvc = req.getParameter("cvc");

        CreditCard creditCard = new CreditCard(cardHolder, cardNumber, expMonth, expYear, cvc);
        StripePayment stripePayment = new StripePayment(creditCard);
        boolean success = stripePayment.executePayment();

        if (success) {
            resp.sendRedirect("order-confirmation");
            try {
                OrderToJSON.convert(orderDataStore.find(1, (ProductCategoryDaoJDBC) productCategoryDataStore,
                        (SupplierDaoJDBC) supplierDataStore));
            } catch (Exception e) {
                e.printStackTrace();
            }
            (new Mailer("pythonsendmailtest75@gmail.com", "lpiiamlxlfsnzwxs", "bogdan.gheboianu.2013@gmail.com", "[CodeCoolShop] Order Confirmation", "This is my test message!")).start();
        } else {
            context.setVariable("payment-error", true);
            engine.process("product/payment-page.html", context, resp.getWriter());
        }
    }

}
