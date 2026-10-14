package com.example.shop.model;

import java.math.BigDecimal;

public class OrderItem {

    private final String sku;
    private final int quantity;
    private final BigDecimal price;

    public OrderItem(String sku, int quantity, double price) {
        this.sku = sku;
        this.quantity = quantity;
        this.price = new BigDecimal(price);
    }

    public String getSku() {
        return sku;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
