package com.supermarket.BLL;

import com.supermarket.DAL.Promotion_giftDAL;
import com.supermarket.DTO.Promotion_gift;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Promotion_giftBLL extends Manager<Promotion_gift>{
    private Promotion_giftDAL promotion_giftDAL;
    private List<Promotion_gift> promotion_giftList;

    public Promotion_giftBLL() {
        promotion_giftDAL = new Promotion_giftDAL();
        promotion_giftList = searchPromotion_gifts();
    }

    public Promotion_giftDAL getPromotion_giftDAL() {
        return promotion_giftDAL;
    }

    public void setPromotion_giftDAL(Promotion_giftDAL promotion_giftDAL) {
        this.promotion_giftDAL = promotion_giftDAL;
    }

    public List<Promotion_gift> getPromotion_giftList() {
        return promotion_giftList;
    }

    public void setPromotion_giftList(List<Promotion_gift> promotion_giftList) {
        this.promotion_giftList = promotion_giftList;
    }

    public Object[][] getData() {
        return getData(promotion_giftList);
    }

    public boolean addPromotion_gift(Promotion_gift promotion_gift) {
        promotion_giftList.add(promotion_gift);
        return promotion_giftDAL.addPromotion_gift(promotion_gift) != 0;
    }

    public boolean updatePromotion_gift(Promotion_gift promotion_gift) {
        promotion_giftList.set(getIndex(promotion_gift, "promotion_id", promotion_giftList), promotion_gift);
        return promotion_giftDAL.updatePromotion_gift(promotion_gift) != 0;
    }

    public List<Promotion_gift> searchPromotion_gifts(String... conditions) {
        return promotion_giftDAL.searchPromotion_gifts(conditions);
    }

    public List<Promotion_gift> findPromotion_gifts(String key, String value) {
        List<Promotion_gift> list = new ArrayList<>();
        for (Promotion_gift promotion_gift : promotion_giftList) {
            if (getValueByKey(promotion_gift, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(promotion_gift);
            }
        }
        return list;
    }

    public List<Promotion_gift> findPromotion_giftsBy(Map<String, Object> conditions) {
        List<Promotion_gift> promotion_gifts = promotion_giftList;
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            promotion_gifts = findObjectsBy(entry.getKey(), entry.getValue(), promotion_gifts);
        return promotion_gifts;
    }

    public boolean exists(Promotion_gift promotion_gift) {
        return !findPromotion_giftsBy(Map.of(
            "product_id", promotion_gift.getProduct_id(),
            "quantity", promotion_gift.getQuantity()
        )).isEmpty();
    }

    public boolean exists(Map<String, Object> conditions) {
        return !findPromotion_giftsBy(conditions).isEmpty();
    }

    @Override
    public Object getValueByKey(Promotion_gift promotion_gift, String key) {
        return switch (key) {
            case "promotion_id" -> promotion_gift.getPromotion_id();
            case "product_id" -> promotion_gift.getProduct_id();
            case "quantity" -> promotion_gift.getQuantity();
            default -> null;
        };
    }
}
