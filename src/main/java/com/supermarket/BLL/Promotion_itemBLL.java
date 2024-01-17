package com.supermarket.BLL;

import com.supermarket.DAL.Promotion_itemDAL;
import com.supermarket.DTO.Promotion_item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Promotion_itemBLL extends Manager<Promotion_item>{
    private Promotion_itemDAL promotion_itemDAL;
    private List<Promotion_item> promotion_itemList;

    public Promotion_itemBLL() {
        promotion_itemDAL = new Promotion_itemDAL();
        promotion_itemList = searchPromotion_items();
    }

    public Promotion_itemDAL getPromotion_itemDAL() {
        return promotion_itemDAL;
    }

    public void setPromotion_itemDAL(Promotion_itemDAL promotion_itemDAL) {
        this.promotion_itemDAL = promotion_itemDAL;
    }

    public List<Promotion_item> getPromotion_itemList() {
        return promotion_itemList;
    }

    public void setPromotion_itemList(List<Promotion_item> promotion_itemList) {
        this.promotion_itemList = promotion_itemList;
    }

    public Object[][] getData() {
        return getData(promotion_itemList);
    }

    public boolean addPromotion_item(Promotion_item promotion_item) {
        promotion_itemList.add(promotion_item);
        return promotion_itemDAL.addPromotion_item(promotion_item) != 0;
    }

    public boolean updatePromotion_item(Promotion_item promotion_item) {
        promotion_itemList.set(getIndex(promotion_item, "promotion_id", promotion_itemList), promotion_item);
        return promotion_itemDAL.updatePromotion_item(promotion_item) != 0;
    }

    public List<Promotion_item> searchPromotion_items(String... conditions) {
        return promotion_itemDAL.searchPromotion_items(conditions);
    }

    public List<Promotion_item> findPromotion_items(String key, String value) {
        List<Promotion_item> list = new ArrayList<>();
        for (Promotion_item promotion_item : promotion_itemList) {
            if (getValueByKey(promotion_item, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(promotion_item);
            }
        }
        return list;
    }

    public List<Promotion_item> findPromotion_itemsBy(Map<String, Object> conditions) {
        List<Promotion_item> promotion_items = promotion_itemList;
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            promotion_items = findObjectsBy(entry.getKey(), entry.getValue(), promotion_items);
        return promotion_items;
    }

    public boolean exists(Promotion_item promotion_item) {
        return !findPromotion_itemsBy(Map.of(
            "product_id", promotion_item.getProduct_id(),
            "quantity", promotion_item.getQuantity()
        )).isEmpty();
    }

    public boolean exists(Map<String, Object> conditions) {
        return !findPromotion_itemsBy(conditions).isEmpty();
    }

    @Override
    public Object getValueByKey(Promotion_item promotion_item, String key) {
        return switch (key) {
            case "promotion_id" -> promotion_item.getPromotion_id();
            case "product_id" -> promotion_item.getProduct_id();
            case "quantity" -> promotion_item.getQuantity();
            default -> null;
        };
    }
}
