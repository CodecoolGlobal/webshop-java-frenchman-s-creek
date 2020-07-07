package com.codecool.shop.model;

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

    public void add(Product product) {
        boolean isInCart = false;
        for (LineItem element : lineItemList) {
            if (element.getProduct().equals(product)) {
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

    public float total() {
        float sum = 0;
        for (LineItem element : lineItemList) {
            sum += element.getSubtotal();
        }
        return sum;
    }

    public int getItemsNr() {
        int sum = 0;
        for (LineItem element : lineItemList) {
            sum += element.getQuantity();
        }
        return sum;
    }
}
