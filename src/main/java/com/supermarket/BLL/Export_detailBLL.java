package com.supermarket.BLL;

import com.supermarket.DAL.Export_detailDAL;
import com.supermarket.DTO.Export_detail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Export_detailBLL extends Manager<Export_detail>{
    private Export_detailDAL exportDetailDAL;
    private List<Export_detail> exportList;

    public Export_detailBLL() {
        exportDetailDAL = new Export_detailDAL();
        exportList = searchExport();
    }

    public Export_detailDAL getExportDetailDAL() {
        return exportDetailDAL;
    }

    public void setExportDetailDAL(Export_detailDAL exportDetailDAL) {
        this.exportDetailDAL = exportDetailDAL;
    }

    public List<Export_detail> getExportList() {
        return exportList;
    }

    public void setExportList(List<Export_detail> exportList) {
        this.exportList = exportList;
    }

    public Object[][] getData() {
        return getData(exportList);
    }

    public boolean addExport_detail(Export_detail exportDetail) {
        exportList.add(exportDetail);
        return exportDetailDAL.addExport_detail(exportDetail) != 0;
    }

    public boolean updateExport_detail(Export_detail exportDetail) {
        exportList.set(getIndex(exportDetail,List.of("export_note_id","shipment_id"), exportList), exportDetail);
        return exportDetailDAL.updateExport_detail(exportDetail) != 0;
    }


    public List<Export_detail> searchExport(String... conditions) {
        return exportDetailDAL.searchExport_detail(conditions);
    }

    public List<Export_detail> findExport(String key, String value) {
        List<Export_detail> list = new ArrayList<>();
        for (Export_detail exportDetail : exportList) {
            if (getValueByKey(exportDetail, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(exportDetail);
            }
        }
        return list;
    }

    public List<Export_detail> findExportBy(Map<String, Object> conditions) {
        List<Export_detail> exports = exportList;
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            exports = findObjectsBy(entry.getKey(), entry.getValue(), exports);
        return exports;
    }

    public boolean exists(Export_detail exports) {
        return !findExportBy(Map.of(
            "export_note_id",exports.getExport_id(),
            "shipment_id", exports.getShipment_id(),
            "quantity", exports.getQuantity(),
            "total", exports.getTotal()
        )).isEmpty();
    }

    public boolean exists(Map<String, Object> conditions) {
        return !findExportBy(conditions).isEmpty();
    }

    @Override
    public Object getValueByKey(Export_detail exports, String key) {
        return switch (key) {
            case "export note id" -> exports.getExport_id();
            case "shipment id" -> exports.getShipment_id();
            case "quantity" -> exports.getQuantity();
            case "total" -> exports.getTotal();
            default -> null;
        };
    }
}
