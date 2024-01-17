package com.supermarket.DTO;

public class Brand {
    private int id;
    private String name;
    private int supplier_id;
    private boolean deleted;

    public Brand() {
    }

    public Brand(int id, String name, int supplier_id, boolean deleted) {
        this.id = id;
        this.name = name;
        this.supplier_id = supplier_id;
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

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
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
            supplier_id;
    }
}
