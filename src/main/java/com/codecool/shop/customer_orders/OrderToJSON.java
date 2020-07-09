package com.codecool.shop.customer_orders;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.codecool.shop.model.LineItem;
import com.codecool.shop.model.Order;

import org.json.simple.JSONObject;

public class OrderToJSON {

    public static void convert(Order order) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", order.getId());
        jsonObject.put("total", order.total());
        List<HashMap<String, String>> products = new ArrayList<>();
        for (LineItem item : order.getLineItemList()) {
            HashMap<String, String> product = new HashMap<String, String>();
            product.put("id", String.valueOf(item.getProduct().getId()));
            product.put("name", item.getProduct().getName());
            product.put("supplier", item.getProduct().getSupplier().getName());
            product.put("quantity", String.valueOf(order.getLineItemList().stream().filter(i -> i.getProduct().getId() == item.getProduct().getId()).findFirst().get().getQuantity()));
            products.add(product);
        }
        jsonObject.put("products", products);
        
        try {
            Path currentRelativePath = Paths.get("");
            String s = currentRelativePath.toAbsolutePath().toString() + "\\src\\main\\java\\com\\codecool\\shop\\customer_orders\\";
            FileWriter file = new FileWriter(s + "order-" + order.getId() + ".json");
            file.write(jsonObject.toJSONString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}