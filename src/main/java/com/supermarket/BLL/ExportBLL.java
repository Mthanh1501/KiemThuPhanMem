package com.supermarket.BLL;

import com.supermarket.DAL.ExportDAL;
import com.supermarket.DTO.Export;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class ExportBLL extends Manager<Export> {
    private ExportDAL exportDAL;
    private List<Export> exportList;

    public ExportBLL() {
        exportDAL = new ExportDAL();
        exportList = searchExport("deleted = 0");
    }

    public ExportDAL getExportNoteDAL() {
        return exportDAL;
    }

    public void setExportNoteDAL(ExportDAL exportDAL) {
        this.exportDAL = exportDAL;
    }

    public List<Export> getExportList() {
        return exportList;
    }

    public void setExportList(List<Export> exportList) {
        this.exportList = exportList;
    }

    public Object[][] getData() {
        return getData(exportList);
    }

    public boolean addExport(Export export) {
        exportList.add(export);
        return exportDAL.addExport(export) != 0;
    }

    public boolean updateExport(Export export) {
        exportList.set(getIndex(export, "id", exportList), export);
        return exportDAL.updateExport(export) != 0;
    }

    public boolean deleteExport(Export export) {
        exportList.remove(getIndex(export, "id",exportList));
        return exportDAL.deleteExport("id = " + export.getId()) != 0;
    }

    public List<Export> searchExport(String... conditions) {
        return exportDAL.searchExport(conditions);
    }

    public List<Export> findExport(String key, String value) {
        List<Export> list = new ArrayList<>();
        for (Export export : exportList) {
            if (getValueByKey(export, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(export);
            }
        }
        return list;
    }

    public List<Export> findExportBy(Map<String, Object> conditions) {
        List<Export> exports = exportList;
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            exports = findObjectsBy(entry.getKey(), entry.getValue(), exports);
        return exports;
    }

    public boolean exists(Export exports) {
        return !findExportBy(Map.of(
            "staff id", exports.getStaff_id(),
            "invoice date", exports.getInvoice_date(),
            "total", exports.getTotal(),
            "reason", exports.getReason()
        )).isEmpty();
    }

    public boolean exists(Map<String, Object> conditions) {
        return !findExportBy(conditions).isEmpty();
    }

    @Override
    public Object getValueByKey(Export exports, String key) {
        return switch (key) {
            case "id" -> exports.getId();
            case "staff id" -> exports.getStaff_id();
            case "invoice date" -> exports.getInvoice_date();
            case "total" -> exports.getTotal();
            case "reason" -> exports.getReason();
            default -> null;
        };
    }
}
