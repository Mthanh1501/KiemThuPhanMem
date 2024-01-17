package com.supermarket.DAL;

import com.supermarket.DTO.Discount_detail;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Discount_detailDAL extends Manager{
    public Discount_detailDAL() {
        super("discount_detail",
            List.of("discount_id",
                "product_id",
                "status"
            ));
    }

    public List<Discount_detail> convertToDiscountDetails(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Discount_detail(
                    Integer.parseInt(row.get(0)), // discount_id
                    Integer.parseInt(row.get(1)), // product_id
                    Boolean.parseBoolean(row.get(2)) //status
                );
            } catch (Exception e) {
                System.out.println("Error occurred in Discount_detailDAL.convertToDiscountDetails(): " + e.getMessage());
            }
            return new Discount_detail();
        });
    }

    public int addDiscountDetail(Discount_detail discount_detail) {
        try {
            return create(discount_detail.getDiscount_id(),
                discount_detail.getProduct_id(),
                discount_detail.isStatus()
            );
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Discount_detailDAL.addDiscountDetail(): " + e.getMessage());
        }
        return 0;
    }

    public int updateDiscountDetail(Discount_detail discount_detail) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(discount_detail.getDiscount_id());
            updateValues.add(discount_detail.getProduct_id());
            updateValues.add(discount_detail.isStatus());
            return update(updateValues,
                "discount_id = " + discount_detail.getDiscount_id(),
                "product_id = " +discount_detail.getProduct_id());
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Discount_detailDAL.updateDiscountDetail(): " + e.getMessage());
        }
        return 0;
    }

    public List<Discount_detail> searchDiscountDetails(String... conditions) {
        try {
            return convertToDiscountDetails(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Discount_detailsDAL.searchDiscountDetails(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
