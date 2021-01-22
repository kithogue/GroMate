package com.example.gromate.gromate.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Map;

@IgnoreExtraProperties
public class OrderItemSummary extends OrderItem {
    private String id;
    private boolean selected = false;

    public OrderItemSummary() {
    }

    public OrderItemSummary(String name, String description, int quantity, String unit) {
        super(name, description, quantity, unit);
    }

    @Exclude
    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> result = super.toMap();
        result.put("id", id);
        result.put("selected", selected);
        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void toggleSelected() {
        selected = !selected;
    }
}
