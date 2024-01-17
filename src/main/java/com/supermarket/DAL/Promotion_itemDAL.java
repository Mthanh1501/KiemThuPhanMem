package com.supermarket.DAL;

import com.supermarket.DTO.Promotion_item;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Promotion_itemDAL extends Manager{
    public Promotion_itemDAL() {
        super("promotion_item",
            List.of("promotion_id",
                "product_id",
                "quantity"));
    }

    public List<Promotion_item> convertToPromotion_items(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Promotion_item(
                    Integer.parseInt(row.get(0)), // promotion_id
                    Integer.parseInt(row.get(1)), // product_id
                    Double.parseDouble(row.get(2)) // quantity
                );
            } catch (Exception e) {
                System.out.println("Error occurred in Promotion_itemDAL.convertToPromotion_items(): " + e.getMessage());
            }
            return new Promotion_item();
        });
    }

    public int addPromotion_item(Promotion_item promotion_item) {
        try {
            return create(promotion_item.getPromotion_id(),
                promotion_item.getProduct_id(),
                promotion_item.getQuantity()
            ); // promotion_item khi tạo mặc định deleted = 0
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Promotion_itemDAL.addPromotion_item(): " + e.getMessage());
        }
        return 0;
    }

    public int updatePromotion_item(Promotion_item promotion_item) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(promotion_item.getPromotion_id());
            updateValues.add(promotion_item.getProduct_id());
            updateValues.add(promotion_item.getQuantity());
            return update(updateValues, "promotion_id = " + promotion_item.getPromotion_id());
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Promotion_itemDAL.updatePromotion_item(): " + e.getMessage());
        }
        return 0;
    }

    public List<Promotion_item> searchPromotion_items(String... conditions) {
        try {
            return convertToPromotion_items(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Promotion_itemDAL.searchPromotion_items(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
