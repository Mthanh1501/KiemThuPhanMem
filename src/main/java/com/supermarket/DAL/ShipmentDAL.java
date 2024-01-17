package com.supermarket.DAL;

import com.supermarket.DTO.Shipment;
import com.supermarket.utils.Date;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShipmentDAL extends Manager{
    public ShipmentDAL() {
        super ("shipment",
            List.of("id",
                "product_id",
                "unit_price",
                "quantity",
                "remain",
                "mfg",
                "exp",
                "sku",
                "import_id",
                "deleted"));
    }

    public List<Shipment> convertToShipments(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Shipment(
                    Integer.parseInt(row.get(0)),
                    Integer.parseInt(row.get(1)),
                    Double.parseDouble(row.get(2)),
                    Double.parseDouble(row.get(3)),
                    Double.parseDouble(row.get(4)),
                    Date.parseDate(row.get(5)),
                    Date.parseDate(row.get(6)),
                    row.get(7),
                    Integer.parseInt(row.get(8)),
                    Boolean.parseBoolean(row.get(9))
                );
            } catch (Exception e) {
                System.out.println("Error occurred in ShipmentDAL.convertToShipment(): " + e.getMessage());
            }
            return new Shipment();
        });
    }

    public int addShipment(Shipment shipment) {
        try {
            return create(shipment.getId(),
                shipment.getProduct_id(),
                shipment.getUnit_price(),
                shipment.getQuantity(),
                shipment.getRemain(),
                shipment.getMfg(),
                shipment.getExp(),
                shipment.getSku(),
                shipment.getImport_id(),
                false
            ); // staff khi tạo mặc định deleted = 0
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in ShipmentDAL.addShipment(): " + e.getMessage());
        }
        return 0;
    }

    public int updateShipment(Shipment shipment) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(shipment.getId());
            updateValues.add(shipment.getProduct_id());
            updateValues.add(shipment.getUnit_price());
            updateValues.add(shipment.getQuantity());
            updateValues.add(shipment.getRemain());
            updateValues.add(shipment.getMfg());
            updateValues.add(shipment.getExp());
            updateValues.add(shipment.getSku());
            updateValues.add(shipment.getImport_id());
            updateValues.add(shipment.isDeleted());
            return update(updateValues, "id = " + shipment.getId());
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in ShipmentDAL.updateShipment(): " + e.getMessage());
        }
        return 0;
    }

    public int deleteShipment(String... conditions) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(true);
            return update(updateValues, conditions);
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in ShipmentDAL.deleteShipment(): " + e.getMessage());
        }
        return 0;
    }

    public List<Shipment> searchShipments(String... conditions) {
        try {
            return convertToShipments(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in ShipmentDAL.searchShipments(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
