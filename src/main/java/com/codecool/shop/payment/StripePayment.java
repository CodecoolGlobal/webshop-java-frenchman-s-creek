package com.codecool.shop.payment;

public class StripePayment {
    private final CreditCard creditCard;
    private boolean success;

    public StripePayment(CreditCard creditCard) {
        this.creditCard = creditCard;
        this.success = false;
    }

    public boolean executePayment() {
        return success;
    }
}