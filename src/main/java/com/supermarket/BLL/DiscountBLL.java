package com.supermarket.BLL;

import com.supermarket.DAL.DiscountDAL;
import com.supermarket.DTO.Discount;
import com.supermarket.utils.VNString;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DiscountBLL extends Manager<Discount> {
    private DiscountDAL discountDAL;
    private List<Discount> discountList;

    public DiscountBLL() {
        discountDAL = new DiscountDAL();
        discountList = searchDiscounts();
    }

    public DiscountDAL getDiscountDAL() {
        return discountDAL;
    }

    public void setDiscountDAL(DiscountDAL discountDAL) {
        this.discountDAL = discountDAL;
    }

    public List<Discount> getDiscountList() {
        return discountList;
    }

    public void setDiscountList(List<Discount> discountList) {
        this.discountList = discountList;
    }

    public Object[][] getData() {
        return getData(discountList);
    }

    public Pair<Boolean, String> addDiscount(Discount discount) {
        Pair<Boolean, String> result;

        result = validatePercent(String.valueOf(discount.getPercent()));
        if(!result.getKey()){
            return new Pair<>(false,result.getValue());
        }

        result = exists(discount);
        if (result.getKey()) {
            return new Pair<>(false,result.getValue());
        }
        discountList.add(discount);
        discountDAL.addDiscount(discount);
        return new Pair<>(true,"");
    }

    public Pair<Boolean, String> updateDiscount(Discount discount) {
        Pair<Boolean, String> result;

        result = validatePercent(String.valueOf(discount.getPercent()));
        if(!result.getKey()){
            return new Pair<>(false,result.getValue());
        }

        discountList.set(getIndex(discount, "id", discountList), discount);
        discountDAL.updateDiscount(discount);
        return new Pair<>(true,"");

    }

    public List<Discount> searchDiscounts(String... conditions) {
        return discountDAL.searchDiscounts(conditions);
    }

    public List<Discount> findDiscounts(String key, String value) {
        List<Discount> list = new ArrayList<>();
        for (Discount discount : discountList) {
            if (getValueByKey(discount, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(discount);
            }
        }
        return list;
    }

    public List<Discount> findDiscountsBy(Map<String, Object> conditions) {
        List<Discount> discounts = discountList;
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            discounts = findObjectsBy(entry.getKey(), entry.getValue(), discounts);
        return discounts;
    }

    public Pair<Boolean, String> exists(Discount newDiscount){
        List<Discount> discounts = discountDAL.searchDiscounts("id = '" + newDiscount.getId() + "'");
        if(!discounts.isEmpty()){
            return new Pair<>(true, "Đợt khuyến mãi đã tồn tại.");
        }

        return new Pair<>(false, "");
    }

    private static Pair<Boolean, String> validatePercent(String percent){
        if (percent.isBlank())
            return new Pair<>(false, "Phần trăm giảm giá không được để trống.");
        if (!VNString.checkRangeOfPercent(percent))
            return new Pair<>(false, "Phần trăm giảm giá phải trong khoảng (0, 100).");
        return new Pair<>(true, percent);
    }
    @Override
    public Object getValueByKey(Discount discount, String key) {
        return switch (key) {
            case "id" -> discount.getId();
            case "percent" -> discount.getPercent();
            case "start_date" -> discount.getStart_date();
            case "end_date" -> discount.getEnd_date();
            case "status" -> discount.isStatus();
            default -> null;
        };
    }
}
