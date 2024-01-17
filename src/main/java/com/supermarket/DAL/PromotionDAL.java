package com.supermarket.DAL;

import com.supermarket.DTO.Promotion;
import com.supermarket.utils.Date;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PromotionDAL extends Manager{
    public PromotionDAL() {
        super("promotion",
            List.of("id",
                "start_date",
                "end_date",
                "status"));
    }

    public List<Promotion> convertToPromotions(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Promotion(
                    Integer.parseInt(row.get(0)), // id
                    Date.parseDate(row.get(1)),  // start_date
                    Date.parseDate(row.get(2)),  // end_date
                    Boolean.parseBoolean(row.get(3))// status
                );
            } catch (Exception e) {
                System.out.println("Error occurred in PromotionDAL.convertToPromotions(): " + e.getMessage());
            }
            return new Promotion();
        });
    }

    public int addPromotion(Promotion promotion) {
        try {
            return create(promotion.getId(),
                promotion.getStart_date(),
                promotion.getEnd_date(),
                promotion.isStatus()
            ); // promotion khi tạo mặc định deleted = 0
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in PromotionDAL.addPromotion(): " + e.getMessage());
        }
        return 0;
    }

    public int updatePromotion(Promotion promotion) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(promotion.getId());
            updateValues.add(promotion.getStart_date());
            updateValues.add(promotion.getEnd_date());
            updateValues.add(promotion.isStatus());
            return update(updateValues, "id = " + promotion.getId());
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in PromotionDAL.updatePromotion(): " + e.getMessage());
        }
        return 0;
    }

    public List<Promotion> searchPromotions(String... conditions) {
        try {
            return convertToPromotions(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in PromotionDAL.searchPromotions(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
