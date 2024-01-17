package com.supermarket.DTO;

import com.supermarket.utils.Date;

public class Discount {
    private int id;
    private double percent;
    private Date start_date;
    private Date end_date;
    private boolean status;

    public Discount() {
    }

    public Discount(int id, double percent, Date start_date, Date end_date, boolean status) {
        this.id = id;
        this.percent = percent;
        this.start_date = start_date;
        this.end_date = end_date;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
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
        return id + " | " +
            percent + " | " +
            start_date + " | " +
            end_date+ " | " +
            status1;
    }
}
