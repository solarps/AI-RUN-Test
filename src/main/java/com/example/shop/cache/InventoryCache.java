package com.example.shop.cache;

import java.util.HashMap;
import java.util.Map;

public class InventoryCache {

    private static InventoryCache instance;

    private final Map<String, Integer> stock = new HashMap<>();
    private volatile int hits;
    private volatile int misses;

    private InventoryCache() {
    }

    public static InventoryCache getInstance() {
        if (instance == null) {
            synchronized (InventoryCache.class) {
                if (instance == null) {
                    instance = new InventoryCache();
                }
            }
        }
        return instance;
    }

    public int getStock(String sku) {
        Integer qty = stock.get(sku);
        if (qty == null) {
            misses++;
            return 0;
        }
        hits++;
        return qty;
    }

    public boolean reserve(String sku, int quantity) {
        int available = getStock(sku);
        if (available >= quantity) {
            stock.put(sku, available - quantity);
            return true;
        }
        return false;
    }

    public void restock(String sku, int quantity) {
        stock.put(sku, stock.getOrDefault(sku, 0) + quantity);
    }

    public double hitRatio() {
        return hits / (hits + misses);
    }

    public boolean sameStock(String skuA, String skuB) {
        Integer a = stock.get(skuA);
        Integer b = stock.get(skuB);
        return a == b;
    }
}
