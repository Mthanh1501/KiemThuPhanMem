package com.supermarket.DTO;

public class Export_detail {
    private int export_id;
    private int shipment_id;
    private double quantity;
    private double total;

    public Export_detail() {
    }

    public Export_detail(int export_id, int shipment_id, double quantity, double total) {
        this.export_id = export_id;
        this.shipment_id = shipment_id;
        this.quantity = quantity;
        this.total = total;
    }

    public int getExport_id() {
        return export_id;
    }

    public void setExport_id(int export_id) {
        this.export_id = export_id;
    }

    public int getShipment_id() {
        return shipment_id;
    }

    public void setShipment_id(int shipment_id) {
        this.shipment_id = shipment_id;
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
        return export_id + " | " +
            shipment_id + " | " +
            quantity + " | " +
            total;
    }
}
