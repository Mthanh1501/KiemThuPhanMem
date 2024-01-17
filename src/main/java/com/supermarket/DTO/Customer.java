package com.supermarket.DTO;

import com.supermarket.utils.Date;

public class Customer {
    private int id;
    private String name;
    private boolean gender;
    private Date birthday;
    private String phone;
    private boolean membership;
    private Date signed_up_date;
    private int point;
    private boolean deleted;

    public Customer() {
    }

    public Customer(int id, String name, boolean gender, Date birthday, String phone, boolean membership, Date signed_up_date, int point, boolean deleted) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.phone = phone;
        this.membership = membership;
        this.signed_up_date = signed_up_date;
        this.point = point;
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

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isMembership() {
        return membership;
    }

    public void setMembership(boolean membership) {
        this.membership = membership;
    }

    public Date getSigned_up_date() {
        return signed_up_date;
    }

    public void setSigned_up_date(Date signed_up_date) {
        this.signed_up_date = signed_up_date;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        String gender1 = gender?  "Nam": "Nữ";
        String membership1 = membership?  "Có": "Không";
        return id + " | " +
            name + " | " +
            gender1 + " | " +
            birthday + " | " +
            phone + " | " +
            membership1 + " | " +
            signed_up_date + " | " +
            point;
    }
}
