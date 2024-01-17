package com.supermarket.DTO;

import com.supermarket.utils.Date;

public class Import {
    private int id;
    private int staff_id;
    private Date received_date;
    private double total;
    private int supplier_id;

    public Import() {
    }

    public Import(int id, int staff_id, Date received_date, double total, int supplier_id) {
        this.id = id;
        this.staff_id = staff_id;
        this.received_date = received_date;
        this.total = total;
        this.supplier_id = supplier_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }

    public Date getReceived_date() {
        return received_date;
    }

    public void setReceived_date(Date received_date) {
        this.received_date = received_date;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }


    @Override
    public String toString() {
        return id  + " | " +
            staff_id + " | " +
            received_date + " | " +
            total + " | " +
            supplier_id;
    }
}
