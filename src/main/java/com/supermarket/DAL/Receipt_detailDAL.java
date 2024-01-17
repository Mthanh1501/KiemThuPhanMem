package com.supermarket.DAL;

import com.supermarket.DTO.Receipt_detail;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Receipt_detailDAL extends Manager{
    public Receipt_detailDAL() {
        super("receipt_detail",
            List.of("receipt_id",
                "product_id",
                "quantity",
                "total"));
    }

    public List<Receipt_detail> convertToReceipt_details(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Receipt_detail(
                    Integer.parseInt(row.get(0)), // receipt_id
                    Integer.parseInt(row.get(1)), // product_id
                    Double.parseDouble(row.get(2)), // quantity
                    Double.parseDouble(row.get(3)) // total
                );
            } catch (Exception e) {
                System.out.println("Error occurred in Receipt_detailDAL.convertToReceipt_details(): " + e.getMessage());
            }
            return new Receipt_detail();
        });
    }

    public int addReceipt_detail(Receipt_detail receipt_detail) {
        try {
            return create(receipt_detail.getReceipt_id(),
                receipt_detail.getProduct_id(),
                receipt_detail.getQuantity(),
                receipt_detail.getTotal()
            ); // receipt_detail khi tạo mặc định deleted = 0
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Receipt_detailDAL.addReceipt_detail(): " + e.getMessage());
        }
        return 0;
    }


    public List<Receipt_detail> searchReceipt_details(String... conditions) {
        try {
            return convertToReceipt_details(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Receipt_detailDAL.searchReceipt_details(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
