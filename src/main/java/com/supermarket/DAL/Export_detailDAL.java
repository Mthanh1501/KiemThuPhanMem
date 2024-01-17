package com.supermarket.DAL;

import com.supermarket.DTO.Export_detail;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class Export_detailDAL extends Manager{
    public Export_detailDAL() {
        super("export_detail",
            List.of("export_id",
                "shipment_id",
                "quantity",
                "total"));
    }

    public List<Export_detail> convertToExport_details(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Export_detail(
                    Integer.parseInt(row.get(0)),
                    Integer.parseInt(row.get(1)),
                    Double.parseDouble(row.get(2)),
                    Double.parseDouble(row.get(3))
                );
            } catch (Exception e) {
                System.out.println("Error occurred in StaffDAL.convertToStaffs(): " + e.getMessage());
            }
            return new Export_detail();
        });
    }

    public int addExport_detail(Export_detail exportDetail) {
        try {
            return create(exportDetail.getExport_id(),
                exportDetail.getShipment_id(),
                exportDetail.getQuantity(),
                exportDetail.getTotal()
            ); // export_detail khi tạo mặc định deleted = 0
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Export_datailDAL.addExport_datail(): " + e.getMessage());
        }
        return 0;
    }

    public int updateExport_detail(Export_detail export) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(export.getExport_id());
            updateValues.add(export.getShipment_id());
            updateValues.add(export.getQuantity());
            updateValues.add(export.getTotal());
            return update(updateValues, "export_id = " + export.getExport_id());
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Export_noteDAL.updateExport_note(): " + e.getMessage());
        }
        return 0;
    }

//    public int deleteExport_detail(String... conditions) {
//        try {
//            List<Object> updateValues = new ArrayList<>();
//            updateValues.add(true);
//            return  update(updateValues,conditions);
//        } catch (SQLException | IOException e) {
//            System.out.println("Error occurred in Export_detailDAL.deleteExport_detail(): " + e.getMessage());
//        }
//        return 0;
//    }

    public List<Export_detail> searchExport_detail(String... conditions) {
        try {
            return convertToExport_details(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Export_detailDAL.searchExport_detail(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
