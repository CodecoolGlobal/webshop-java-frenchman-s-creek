package com.codecool.shop.model;

public class LineItem {
    private Product product;
    private int quantity = 1;

    public LineItem(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            quantity = 0;
        }
        this.quantity = quantity;
    }

    public float getSubtotal() {
        return quantity * product.getDefaultPrice();
    }
}
