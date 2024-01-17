package com.supermarket.DAL;

import com.supermarket.DTO.Receipt;
import com.supermarket.utils.Date;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReceiptDAL extends Manager {
    public ReceiptDAL() {
        super("receipt",
            List.of("id",
                "staff_id",
                "invoice_date",
                "total",
                "received",
                "excess"));
    }

    public List<Receipt> convertToReceipts(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Receipt(
                    Integer.parseInt(row.get(0)), // id
                    Integer.parseInt(row.get(1)), // staff_id
                    Date.parseDate(row.get(2)), // invoice_date
                    Double.parseDouble(row.get(3)), // total
                    Double.parseDouble(row.get(4)), // received
                    Double.parseDouble(row.get(5)) // excess
                );
            } catch (Exception e) {
                System.out.println("Error occurred in ReceiptDAL.convertToReceipts(): " + e.getMessage());
            }
            return new Receipt();
        });
    }

    public int addReceipt(Receipt receipt) {
        try {
            return create(receipt.getId(),
                receipt.getStaff_id(),
                receipt.getInvoice_date(),
                receipt.getTotal(),
                receipt.getReceived(),
                receipt.getExcess()
            ); // receipt khi tạo mặc định deleted = 0
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in ReceiptDAL.addReceipt(): " + e.getMessage());
        }
        return 0;
    }

    public List<Receipt> searchReceipts(String... conditions) {
        try {
            return convertToReceipts(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in ReceiptDAL.searchReceipts(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
