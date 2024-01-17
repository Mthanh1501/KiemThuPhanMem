package com.supermarket.BLL;

import com.supermarket.DAL.Receipt_detailDAL;
import com.supermarket.DTO.Receipt_detail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Receipt_detailBLL extends Manager<Receipt_detail>{
    private Receipt_detailDAL receipt_detailDAL;
    private List<Receipt_detail> receipt_detailList;

    public Receipt_detailBLL() {
        receipt_detailDAL = new Receipt_detailDAL();
        receipt_detailList = searchReceipt_details();
    }

    public Receipt_detailDAL getReceipt_detailDAL() {
        return receipt_detailDAL;
    }

    public void setReceipt_detailDAL(Receipt_detailDAL receipt_detailDAL) {
        this.receipt_detailDAL = receipt_detailDAL;
    }

    public List<Receipt_detail> getReceipt_detailList() {
        return receipt_detailList;
    }

    public void setReceipt_detailList(List<Receipt_detail> receipt_detailList) {
        this.receipt_detailList = receipt_detailList;
    }

    public Object[][] getData() {
        return getData(receipt_detailList);
    }

    public boolean addReceipt_detail(Receipt_detail receipt_detail) {
        receipt_detailList.add(receipt_detail);
        return receipt_detailDAL.addReceipt_detail(receipt_detail) != 0;
    }

    public List<Receipt_detail> searchReceipt_details(String... conditions) {
        return receipt_detailDAL.searchReceipt_details(conditions);
    }

    public List<Receipt_detail> findReceipt_details(String key, String value) {
        List<Receipt_detail> list = new ArrayList<>();
        for (Receipt_detail receipt_detail : receipt_detailList) {
            if (getValueByKey(receipt_detail, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(receipt_detail);
            }
        }
        return list;
    }

    public List<Receipt_detail> findReceipt_detailsBy(Map<String, Object> conditions) {
        List<Receipt_detail> receipt_details = receipt_detailList;
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            receipt_details = findObjectsBy(entry.getKey(), entry.getValue(), receipt_details);
        return receipt_details;
    }

    public boolean exists(Receipt_detail receipt_detail) {
        return !findReceipt_detailsBy(Map.of(
            "product_id", receipt_detail.getProduct_id(),
            "quantity", receipt_detail.getQuantity(),
            "total", receipt_detail.getTotal()
        )).isEmpty();
    }

    public boolean exists(Map<String, Object> conditions) {
        return !findReceipt_detailsBy(conditions).isEmpty();
    }

    @Override
    public Object getValueByKey(Receipt_detail receipt_detail, String key) {
        return switch (key) {
            case "receipt_id" -> receipt_detail.getReceipt_id();
            case "product_id" -> receipt_detail.getProduct_id();
            case "quantity" -> receipt_detail.getQuantity();
            case "total" -> receipt_detail.getTotal();
            default -> null;
        };
    }
}
