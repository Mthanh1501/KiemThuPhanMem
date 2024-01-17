package com.supermarket.BLL;

import com.supermarket.DAL.ShipmentDAL;
import com.supermarket.DTO.Shipment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShipmentBLL extends Manager<Shipment>{
    private ShipmentDAL shipmentDAL;
    private List<Shipment> shipmentList;

    public ShipmentBLL() {
        shipmentDAL = new ShipmentDAL();
        shipmentList = searchShipments("deleted = 0");
    }

    public ShipmentDAL getShipmentDAL() { return shipmentDAL; }

    public void setShipmentDAL(ShipmentDAL shipmentDAL) {this.shipmentDAL=shipmentDAL;}

    public List<Shipment> getShipmentList() { return shipmentList; }

    public void setShipmentList(List<Shipment> shipmentList) { this.shipmentList=shipmentList;}

    public Object[][] getData() {
        return getData(shipmentList);
    }

    public boolean addShipment(Shipment shipment) {
        shipmentList.add(shipment);
        return shipmentDAL.addShipment(shipment) != 0;
    }

    public boolean updateShipment(Shipment shipment) {
        shipmentList.set(getIndex(shipment, "id", shipmentList), shipment);
        return shipmentDAL.updateShipment(shipment) != 0;
    }

    public boolean deleteShipment(Shipment shipment) {
        shipmentList.remove(getIndex(shipment, "id", shipmentList));
        return shipmentDAL.deleteShipment("id = " + shipment.getId()) != 0;
    }
    public List<Shipment> searchShipments(String... conditions) {
        return shipmentDAL.searchShipments(conditions);
    }

    public List<Shipment> findShipments(String key, String value) {
        List<Shipment> list = new ArrayList<>();
        for (Shipment shipment : shipmentList) {
            if (getValueByKey(shipment, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(shipment);
            }
        }
        return list;
    }

    public List<Shipment> findShipmentsBy(Map<String, Object> conditions) {
        List<Shipment> shipments = shipmentList;
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            shipments = findObjectsBy(entry.getKey(), entry.getValue(), shipments);
        return shipments;
    }

    public boolean exists(Shipment shipment) {
        return !findShipmentsBy(Map.of(
            "product_id", shipment.getProduct_id(),
            "unit_price", shipment.getUnit_price(),
            "quantity", shipment.getQuantity(),
            "remain", shipment.getRemain(),
            "mfg", shipment.getMfg(),
            "exp", shipment.getExp(),
            "sku", shipment.getSku(),
            "import_note_id",shipment.getImport_id()
        )).isEmpty();
    }

    public boolean exists(Map<String, Object> conditions) {
        return !findShipmentsBy(conditions).isEmpty();
    }

    @Override
    public Object getValueByKey(Shipment shipment, String key) {
        return switch (key) {
            case "id" -> shipment.getId();
            case "product_id" -> shipment.getProduct_id();
            case "unit_price" -> shipment.getUnit_price();
            case "quantity" -> shipment.getQuantity();
            case "remain" -> shipment.getRemain();
            case "mfg" -> shipment.getMfg();
            case "exp" -> shipment.getExp();
            case "sku" -> shipment.getSku();
            case "import_note_id"-> shipment.getImport_id();
            default -> null;
        };
    }
}
