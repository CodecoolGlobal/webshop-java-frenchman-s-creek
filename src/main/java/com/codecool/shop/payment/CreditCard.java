package com.codecool.shop.payment;

public class CreditCard {
    private final String cardHolder;
    private final String cardNumber;
    private final String expMonth;
    private final String expYear;
    private final String cvc;
    
    public CreditCard(String cardHolder, String cardNumber, String expMonth, String expYear, String cvc) {
        this.cardHolder = cardHolder;
        this.cardNumber = cardNumber;
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.cvc = cvc;
    }

    public String getCardHolder() {
        return this.cardHolder;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public String getExpMonth() {
        return this.expMonth;
    }

    public String getExpYear() {
        return this.expYear;
    }

    public String getCvc() {
        return this.cvc;
    }
}