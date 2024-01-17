package com.supermarket.DTO;

import com.supermarket.utils.Date;

public class Staff {
    private int id;
    private String name;
    private boolean gender;
    private Date birthday;
    private String phone;
    private String address;
    private String email;
    private Date entry_date;
    private boolean deleted;

    public Staff() {
    }

    public Staff(int id, String name, boolean gender, Date birthday, String phone, String address, String email, Date entry_date, boolean deleted) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.entry_date = entry_date;
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

    public boolean getGender() {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getEntry_date() {
        return entry_date;
    }

    public void setEntry_date(Date entry_date) {
        this.entry_date = entry_date;
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
        return id + " | " +
            name + " | " +
            gender1 + " | " +
            birthday + " | " +
            phone + " | " +
            address + " | " +
            email + " | " +
            entry_date;
    }
}
