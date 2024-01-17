package com.supermarket.BLL;

import com.supermarket.DAL.Discount_detailDAL;
import com.supermarket.DTO.Discount_detail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Discount_detailBLL extends Manager<Discount_detail> {
    private Discount_detailDAL discount_detailDAL;
    private List<Discount_detail> discount_detailList;

    public Discount_detailBLL() {
        discount_detailDAL = new Discount_detailDAL();
        discount_detailList = searchDiscount_details();
    }

    public Discount_detailDAL getDiscount_detailDAL() {
        return discount_detailDAL;
    }

    public void setDiscount_detailDAL(Discount_detailDAL discount_detailDAL) {
        this.discount_detailDAL = discount_detailDAL;
    }

    public List<Discount_detail> getDiscount_detailList() {
        return discount_detailList;
    }

    public void setDiscount_detailList(List<Discount_detail> discount_detailList) {
        this.discount_detailList = discount_detailList;
    }

    public Object[][] getData() {
        return getData(discount_detailList);
    }

    public boolean addDiscount_detail(Discount_detail discount_detail) {
        discount_detailList.add(discount_detail);
        return discount_detailDAL.addDiscountDetail(discount_detail) != 0;
    }

    public boolean updateDiscount_detail(Discount_detail discount_detail) {
        discount_detailList.set(getIndex(discount_detail, List.of("discount_id","product_id"), discount_detailList), discount_detail);
        return discount_detailDAL.updateDiscountDetail(discount_detail) != 0;
    }

    public List<Discount_detail> searchDiscount_details(String... conditions) {
        return discount_detailDAL.searchDiscountDetails(conditions);
    }

    public List<Discount_detail> findDiscount_details(String key, String value) {
        List<Discount_detail> list = new ArrayList<>();
        for (Discount_detail discount_detail : discount_detailList) {
            if (getValueByKey(discount_detail, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(discount_detail);
            }
        }
        return list;
    }

    public List<Discount_detail> findDiscount_detailsBy(Map<String, Object> conditions) {
        List<Discount_detail> discount_details = discount_detailList;
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            discount_details = findObjectsBy(entry.getKey(), entry.getValue(), discount_details);
        return discount_details;
    }

    public boolean exists(Discount_detail discount_detail) {
        return !findDiscount_detailsBy(Map.of(
            "discount_id", discount_detail.getDiscount_id(),
            "product_id", discount_detail.getProduct_id(),
            "status",discount_detail.isStatus()
        )).isEmpty();
    }

    public boolean exists(Map<String, Object> conditions) {
        return !findDiscount_detailsBy(conditions).isEmpty();
    }

    @Override
    public Object getValueByKey(Discount_detail discount_detail, String key) {
        return switch (key) {
            case "discount_id" -> discount_detail.getDiscount_id();
            case "product_id" -> discount_detail.getProduct_id();
            case "status" -> discount_detail.isStatus();
            default -> null;
        };
    }
}
