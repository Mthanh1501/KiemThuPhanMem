package com.supermarket.DTO;

public class Discount_detail {
    private int discount_id;
    private int product_id;
    private boolean status;

    public Discount_detail() {
    }

    public Discount_detail(int discount_id, int product_id, boolean status) {
        this.discount_id = discount_id;
        this.product_id = product_id;
        this.status = status;
    }

    public int getDiscount_id() {
        return discount_id;
    }

    public void setDiscount_id(int discount_id) {
        this.discount_id = discount_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        String status1 = !status? "Đang áp dụng" : "Ngừng áp dụng";
        return discount_id + " | " +
            product_id + " | " +
            status1;
    }
}
