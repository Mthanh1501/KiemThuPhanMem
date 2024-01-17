package com.supermarket.DTO;

import com.supermarket.utils.Date;

public class Export {
    private int id;
    private int staff_id;
    private Date invoice_date;
    private double total;
    private String reason;
    private boolean deleted;

    public Export() {
    }

    public Export(int id, int staff_id, Date invoice_date, double total, String reason, boolean deleted) {
        this.id = id;
        this.staff_id = staff_id;
        this.invoice_date = invoice_date;
        this.total = total;
        this.reason = reason;
        this.deleted = deleted;
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

    public Date getInvoice_date() {
        return invoice_date;
    }

    public void setInvoice_date(Date invoice_date) {
        this.invoice_date = invoice_date;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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
            staff_id + " | " +
            invoice_date + " | " +
            total + " | " +
            reason;
    }
}
