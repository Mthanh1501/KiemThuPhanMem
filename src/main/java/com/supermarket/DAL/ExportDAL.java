package com.supermarket.DAL;

import com.supermarket.DTO.Export;
import com.supermarket.utils.Date;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ExportDAL extends Manager{
    public ExportDAL() {
        super("export",
            List.of("id",
                "staff_id",
                "invoice_date",
                "total",
                "reason",
                "deleted"));
    }

    public List<Export> convertToExport(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Export(
                    Integer.parseInt(row.get(0)),
                    Integer.parseInt(row.get(1)),
                    Date.parseDate(row.get(2)),
                    Double.parseDouble(row.get(3)),
                    row.get(4),
                    Boolean.parseBoolean(row.get(5))
                );
            } catch (Exception e) {
                System.out.println("Error occurred in ExportDAL.convertToExport(): " + e.getMessage());
            }
            return new Export();
        });
    }

    public int addExport(Export export) {
        try {
            return create(export.getId(),
                export.getStaff_id(),
                export.getInvoice_date(),
                export.getTotal(),
                export.getReason(),
                false
            ); // export_detail khi tạo mặc định deleted = 0
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in ExportDAL.addExport(): " + e.getMessage());
        }
        return 0;
    }

    public int updateExport(Export export) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(export.getId());
            updateValues.add(export.getStaff_id());
            updateValues.add(export.getInvoice_date());
            updateValues.add(export.getTotal());
            updateValues.add(export.getReason());
            updateValues.add(export.isDeleted());
            return update(updateValues, "id = " + export.getId());
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in ExportDAL.updateExport(): " + e.getMessage());
        }
        return 0;
    }

    public int deleteExport(String... conditions) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(true);
            return  update(updateValues,conditions);
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in ExportDAL.deleteExport(): " + e.getMessage());
        }
        return 0;
    }
    public List<Export> searchExport(String... conditions) {
        try {
            return convertToExport(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in ExportDAL.searchExport(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
