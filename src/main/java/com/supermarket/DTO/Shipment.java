package com.supermarket.DTO;

import com.supermarket.utils.Date;

public class Shipment {
    private int id;
    private int product_id;
    private double unit_price;
    private double quantity;
    private double remain;
    private Date mfg;
    private Date exp;
    private String sku;
    private int import_id;
    private boolean deleted;

    public Shipment() {
    }

    public Shipment(int id, int product_id) {
        this.id = id;
        this.product_id = product_id;
    }

    public Shipment(int id, int product_id, double unit_price, double quantity, double remain, Date mfg, Date exp, String sku, int import_id, boolean deleted) {
        this.id = id;
        this.product_id = product_id;
        this.unit_price = unit_price;
        this.quantity = quantity;
        this.remain = remain;
        this.mfg = mfg;
        this.exp = exp;
        this.sku = sku;
        this.import_id = import_id;
        this.deleted = deleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getRemain() {
        return remain;
    }

    public void setRemain(double remain) {
        this.remain = remain;
    }

    public Date getMfg() {
        return mfg;
    }

    public void setMfg(Date mfg) {
        this.mfg = mfg;
    }

    public Date getExp() {
        return exp;
    }

    public void setExp(Date exp) {
        this.exp = exp;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getImport_id() {
        return import_id;
    }

    public void setImport_id(int import_id) {
        this.import_id = import_id;
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
            product_id + " | " +
            unit_price + " | " +
            quantity + " | " +
            remain + " | " +
            mfg + " | " +
            exp + " | " +
            sku + " | " +
            import_id;
    }
}
