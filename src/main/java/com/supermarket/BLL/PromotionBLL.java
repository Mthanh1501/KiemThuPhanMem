package com.supermarket.BLL;

import com.supermarket.DAL.PromotionDAL;
import com.supermarket.DTO.Promotion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PromotionBLL extends Manager<Promotion>{
    private PromotionDAL promotionDAL;
    private List<Promotion> promotionList;

    public PromotionBLL() {
        promotionDAL = new PromotionDAL();
        promotionList = searchPromotions("status = 0");
    }

    public PromotionDAL getPromotionDAL() {
        return promotionDAL;
    }

    public void setPromotionDAL(PromotionDAL promotionDAL) {
        this.promotionDAL = promotionDAL;
    }

    public List<Promotion> getPromotionList() {
        return promotionList;
    }

    public void setPromotionList(List<Promotion> promotionList) {
        this.promotionList = promotionList;
    }

    public Object[][] getData() {
        return getData(promotionList);
    }

    public boolean addPromotion(Promotion promotion) {
        promotionList.add(promotion);
        return promotionDAL.addPromotion(promotion) != 0;
    }

    public boolean updatePromotion(Promotion promotion) {
        promotionList.set(getIndex(promotion, "id", promotionList), promotion);
        return promotionDAL.updatePromotion(promotion) != 0;
    }

    public List<Promotion> searchPromotions(String... conditions) {
        return promotionDAL.searchPromotions(conditions);
    }

    public List<Promotion> findPromotions(String key, String value) {
        List<Promotion> list = new ArrayList<>();
        for (Promotion promotion : promotionList) {
            if (getValueByKey(promotion, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(promotion);
            }
        }
        return list;
    }

    public List<Promotion> findPromotionsBy(Map<String, Object> conditions) {
        List<Promotion> promotions = promotionList;
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            promotions = findObjectsBy(entry.getKey(), entry.getValue(), promotions);
        return promotions;
    }

    public boolean exists(Promotion promotion) {
        return !findPromotionsBy(Map.of(
            "start_date", promotion.getStart_date(),
            "end_date", promotion.getEnd_date(),
            "status", promotion.isStatus()
        )).isEmpty();
    }

    public boolean exists(Map<String, Object> conditions) {
        return !findPromotionsBy(conditions).isEmpty();
    }

    @Override
    public Object getValueByKey(Promotion promotion, String key) {
        return switch (key) {
            case "id" -> promotion.getId();
            case "start_date" -> promotion.getStart_date();
            case "end_date" -> promotion.getEnd_date();
            case "status" -> promotion.isStatus();
            default -> null;
        };
    }
}
