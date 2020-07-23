package com.codecool.shop.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private List<LineItem> lineItemList = new ArrayList<>();
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<LineItem> getLineItemList() {
        return lineItemList;
    }

    public void add(Product product) {
        boolean isInCart = false;
        for (LineItem element : lineItemList) {
            System.out.println(element.getProduct().getId());
            System.out.println(product.getId());
            System.out.println("===========");
            if (element.getProduct().getId() == product.getId()) {
                element.setQuantity(element.getQuantity() + 1);
                isInCart = true;
            }
        }
        if (!isInCart) {
            lineItemList.add(new LineItem(product));
        }
    }

    public void remove(Product product) {
        lineItemList.removeIf(element -> element.getProduct().equals(product));
    }

    public BigDecimal total() {
        float sum = 0;
        for (LineItem element : lineItemList) {
            sum += element.getSubtotal();
        }
        BigDecimal sumRounded = new BigDecimal(Float.toString(sum));
        sumRounded = sumRounded.setScale(2, RoundingMode.HALF_UP);
        return sumRounded;
    }
}
