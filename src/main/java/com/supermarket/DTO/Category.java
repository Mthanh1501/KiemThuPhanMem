package com.supermarket.DTO;

public class Category {
    private int id;
    private String name;
    private double quantity;
    private boolean deleted;

    public Category() {
    }

    public Category(int id, String name, double quantity, boolean deleted) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.deleted = deleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return id + " | " +
            name + " | " +
            quantity;
    }
}
