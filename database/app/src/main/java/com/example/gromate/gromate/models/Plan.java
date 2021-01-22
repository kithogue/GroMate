package com.example.gromate.gromate.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@IgnoreExtraProperties
public class Plan {

    private String id;
    private String name;
    private String description;
    private String orders = "";
    private String orderNames = "";
    private boolean selected = false;

    public Plan() {
    }

    public Plan(String id, String name, String description, Set<Order> selectedOrders) {
        this.id = id;
        this.name = name;
        this.description = description;
        for (Order order : selectedOrders) {
            orders += order.getId() + ", ";
            orderNames += order.getName() + ", ";
        }
        if (orders.length() > 0) {
            orders = orders.substring(0, orders.length() - 1);
            orderNames = orderNames.substring(0, orderNames.length() - 1);
        }
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

    public String getOrders() {
        return orders;
    }

    public String getOrderNames() {
        return orderNames;
    }

    public boolean isSelected() {
        return selected;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("description", description);
        result.put("orders", orders);
        result.put("orderNames", orderNames);
        result.put("selected", selected);
        return result;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
