package com.supermarket.DTO;

public class Promotion_gift {
    private int promotion_id;
    private int product_id;
    private double quantity;

    public Promotion_gift() {
    }

    public Promotion_gift(int promotion_id, int product_id, double quantity) {
        this.promotion_id = promotion_id;
        this.product_id = product_id;
        this.quantity = quantity;
    }

    public int getPromotion_id() {
        return promotion_id;
    }

    public void setPromotion_id(int promotion_id) {
        this.promotion_id = promotion_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return promotion_id + " | " +
            product_id + " | " +
            quantity;
    }
}
