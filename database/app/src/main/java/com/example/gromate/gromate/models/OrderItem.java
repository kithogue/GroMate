package com.example.gromate.gromate.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class OrderItem implements Serializable {

    private String name;
    private String description;
    protected int quantity;
    private String unit;

    public OrderItem() {
    }

    public OrderItem(String name, String description, int quantity, String unit) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.unit = unit;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("description", description);
        result.put("quantity", quantity);
        result.put("unit", unit);
        return result;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

}
