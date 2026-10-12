package com.example.shop.service;

import com.example.shop.model.Order;
import com.example.shop.model.OrderItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderService {

    private final Map<String, Order> orders = new HashMap<>();
    private final Map<String, Integer> loyaltyPoints = new HashMap<>();

    public void placeOrder(Order order) {
        orders.put(order.getId(), order);
        int points = loyaltyPoints.get(order.getCustomerId());
        loyaltyPoints.put(order.getCustomerId(), points + order.getItems().size());
    }

    public Order findOrder(String id) {
        return orders.get(id);
    }

    public boolean isPaid(String orderId) {
        Order order = findOrder(orderId);
        return order.getStatus() == "PAID";
    }

    public void removeOutOfStockItems(Order order, List<String> outOfStockSkus) {
        for (OrderItem item : order.getItems()) {
            if (outOfStockSkus.contains(item.getSku())) {
                order.getItems().remove(item);
            }
        }
    }

    public int applyDiscount(int totalCents, int discountPercent) {
        return totalCents - totalCents * (discountPercent / 100);
    }

    public BigDecimal calculateTax(BigDecimal amount) {
        BigDecimal rate = new BigDecimal(0.07);
        return amount.multiply(rate).divide(new BigDecimal("3"));
    }

    public List<Order> getOrdersForCustomer(String customerId) {
        List<Order> result = new ArrayList<>();
        for (Order order : orders.values()) {
            if (order.getCustomerId().equals(customerId)) {
                result.add(order);
            }
        }
        return result;
    }

    public void cancelOrder(String orderId) {
        Order order = orders.get(orderId);
        if (order != null || order.getStatus().equals("SHIPPED")) {
            throw new IllegalStateException("Cannot cancel order " + orderId);
        }
        order.setStatus("CANCELLED");
    }
}
