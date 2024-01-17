package com.supermarket.BLL;

import com.supermarket.DAL.ReceiptDAL;
import com.supermarket.DTO.Receipt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReceiptBLL extends Manager<Receipt>{
    private ReceiptDAL receiptDAL;
    private List<Receipt> receiptList;

    public ReceiptBLL() {
        receiptDAL = new ReceiptDAL();
        receiptList = searchReceipts();
    }

    public ReceiptDAL getReceiptDAL() {
        return receiptDAL;
    }

    public void setReceiptDAL(ReceiptDAL receiptDAL) {
        this.receiptDAL = receiptDAL;
    }

    public List<Receipt> getReceiptList() {
        return receiptList;
    }

    public void setReceiptList(List<Receipt> receiptList) {
        this.receiptList = receiptList;
    }

    public Object[][] getData() {
        return getData(receiptList);
    }

    public boolean addReceipt(Receipt receipt) {
        receiptList.add(receipt);
        return receiptDAL.addReceipt(receipt) != 0;
    }

    public List<Receipt> searchReceipts(String... conditions) {
        return receiptDAL.searchReceipts(conditions);
    }

    public List<Receipt> findReceipts(String key, String value) {
        List<Receipt> list = new ArrayList<>();
        for (Receipt receipt : receiptList) {
            if (getValueByKey(receipt, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(receipt);
            }
        }
        return list;
    }

    public List<Receipt> findReceiptsBy(Map<String, Object> conditions) {
        List<Receipt> receipts = receiptList;
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            receipts = findObjectsBy(entry.getKey(), entry.getValue(), receipts);
        return receipts;
    }

    public boolean exists(Receipt receipt) {
        return !findReceiptsBy(Map.of(
            "staff_id", receipt.getStaff_id(),
            "invoice_date", receipt.getInvoice_date(),
            "total", receipt.getTotal(),
            "received", receipt.getReceived(),
            "excess", receipt.getExcess()
        )).isEmpty();
    }

    public boolean exists(Map<String, Object> conditions) {
        return !findReceiptsBy(conditions).isEmpty();
    }

    @Override
    public Object getValueByKey(Receipt receipt, String key) {
        return switch (key) {
            case "id" -> receipt.getId();
            case "staff_id" -> receipt.getStaff_id();
            case "invoice_date" -> receipt.getInvoice_date();
            case "total" -> receipt.getTotal();
            case "received" -> receipt.getReceived();
            case "excess" -> receipt.getExcess();
            default -> null;
        };
    }
}
