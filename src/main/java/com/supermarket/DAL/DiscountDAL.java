package com.supermarket.DAL;

import com.supermarket.DTO.Discount;
import com.supermarket.utils.Date;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DiscountDAL extends Manager {
    public DiscountDAL() {
        super("discount",
            List.of("id",
                "percent",
                "start_date",
                "end_date",
                "status"
                ));
    }

    public List<Discount> convertToDiscounts(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Discount(
                    Integer.parseInt(row.get(0)), // id
                    Double.parseDouble(row.get(1)), // percent
                    Date.parseDate(row.get(2)), // start_date
                    Date.parseDate(row.get(3)), // end_date
                    Boolean.parseBoolean(row.get(4)) // status
                );
            } catch (Exception e) {
                System.out.println("Error occurred in DiscountDAL.convertToDiscounts(): " + e.getMessage());
            }
            return new Discount();
        });
    }

    public int addDiscount(Discount discount) {
        try {
            return create(discount.getId(),
                discount.getPercent(),
                discount.getStart_date(),
                discount.getEnd_date(),
                discount.isStatus()
            ); // discount khi tạo mặc định deleted = 0
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in DiscountDAL.addDiscount(): " + e.getMessage());
        }
        return 0;
    }

    public int updateDiscount (Discount discount) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(discount.getId());
            updateValues.add(discount.getPercent());
            updateValues.add(discount.getStart_date());
            updateValues.add(discount.getEnd_date());
            updateValues.add(discount.isStatus());
            return update(updateValues, "id = " + discount.getId());
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in DiscountDAL.updateDiscount(): " + e.getMessage());
        }
        return 0;
    }

    public List<Discount> searchDiscounts(String... conditions) {
        try {
            return convertToDiscounts(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in DiscountDAL.searchDiscounts(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
