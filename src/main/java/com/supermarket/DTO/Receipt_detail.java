package com.supermarket.DTO;

public class Receipt_detail {
    private int receipt_id;
    private int product_id;
    private double quantity;
    private double total;

    public Receipt_detail() {
    }

    public Receipt_detail(int receipt_id, int product_id, double quantity, double total) {
        this.receipt_id = receipt_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.total = total;
    }

    public int getReceipt_id() {
        return receipt_id;
    }

    public void setReceipt_id(int receipt_id) {
        this.receipt_id = receipt_id;
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

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }


    @Override
    public String toString() {
        return receipt_id + " | " +
            product_id + " | " +
            quantity + " | " +
            total;
    }
}
