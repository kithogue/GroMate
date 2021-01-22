package com.example.gromate.gromate.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class Order implements Comparable<Order> {
    private String id;
    private String name;
    private String description;
    @PropertyName("items")
    private List<OrderItem> items;

    @SuppressWarnings("unused")
    public Order() {
    }

    public Order(String id, String name, String description, OrderItem[] items) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.items = Arrays.asList(items);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("description", description);
        List<Map<String, Object>> orderItems = new ArrayList<>();
        for (OrderItem item : items) {
            orderItems.add(item.toMap());
        }
        result.put("items", orderItems);
        return result;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id.equals(order.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public int compareTo(Order o) {
        return o.name.compareTo(name);
    }
}
