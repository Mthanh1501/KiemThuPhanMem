package com.supermarket.DAL;

import com.supermarket.DTO.Promotion_gift;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Promotion_giftDAL extends Manager{
    public Promotion_giftDAL() {
        super("promotion_gift",
            List.of("promotion_id",
                "product_id",
                "quantity"));
    }

    public List<Promotion_gift> convertToPromotion_gifts(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Promotion_gift(
                    Integer.parseInt(row.get(0)), // promotion_id
                    Integer.parseInt(row.get(1)), // product_id
                    Double.parseDouble(row.get(2)) // quantity
                );
            } catch (Exception e) {
                System.out.println("Error occurred in Promotion_giftDAL.convertToPromotion_gifts(): " + e.getMessage());
            }
            return new Promotion_gift();
        });
    }

    public int addPromotion_gift(Promotion_gift promotion_gift) {
        try {
            return create(promotion_gift.getPromotion_id(),
                promotion_gift.getProduct_id(),
                promotion_gift.getQuantity()
            ); // promotion_gift khi tạo mặc định deleted = 0
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Promotion_giftDAL.addPromotion_gift(): " + e.getMessage());
        }
        return 0;
    }

    public int updatePromotion_gift(Promotion_gift promotion_gift) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(promotion_gift.getPromotion_id());
            updateValues.add(promotion_gift.getProduct_id());
            updateValues.add(promotion_gift.getQuantity());
            return update(updateValues, "promotion_id = " + promotion_gift.getPromotion_id());
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Promotion_giftDAL.updatePromotion_gift(): " + e.getMessage());
        }
        return 0;
    }

//    public int deletePromotion_gift(String... conditions) {
//        try {
//            List<Object> updateValues = new ArrayList<>();
//            updateValues.add(true);
//            return update(updateValues, conditions);
//        } catch (SQLException | IOException e) {
//            System.out.println("Error occurred in Promotion_giftDAL.deletePromotion_gift(): " + e.getMessage());
//        }
//        return 0;
//    }

    public List<Promotion_gift> searchPromotion_gifts(String... conditions) {
        try {
            return convertToPromotion_gifts(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Promotion_giftDAL.searchPromotion_gifts(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
