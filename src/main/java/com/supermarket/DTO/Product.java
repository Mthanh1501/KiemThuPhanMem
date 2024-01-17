package com.supermarket.DTO;

public class Product {
    private int id;
    private String name;
    private int brand_id;
    private int category_id;
    private String unit;
    private double cost;
    private double quantity;
    private String image;
    private String barcode;
    private boolean deleted;

    public Product() {
    }

    public Product(int id, String name, int brand_id, int category_id, String unit, double cost, double quantity, String image, String barcode, boolean deleted) {
        this.id = id;
        this.name = name;
        this.brand_id = brand_id;
        this.category_id = category_id;
        this.unit = unit;
        this.cost = cost;
        this.quantity = quantity;
        this.image = image;
        this.barcode = barcode;
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

    public int getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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
            brand_id + " | " +
            category_id + " | " +
            unit + " | " +
            cost + " | " +
            quantity + " | " +
            barcode;
    }
}
