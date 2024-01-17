package com.supermarket.BLL;

import com.supermarket.DAL.ImportDAL;
import com.supermarket.DTO.Import;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class ImportBLL extends Manager<Import> {
    private ImportDAL importNoteDAL;
    private List<Import> importList;

    public ImportBLL() {
        importNoteDAL = new ImportDAL();
        importList = searchImport();
    }

    public ImportDAL getImportDAL() {
        return importNoteDAL;
    }

    public void setImportDAL(ImportDAL importNoteDAL) {
        this.importNoteDAL = importNoteDAL;
    }

    public List<Import> getImportList() {
        return importList;
    }

    public void setStaffList(List<Import> importList) {
        this.importList = importList;
    }

    public Object[][] getData() {
        return getData(importList);
    }

    public boolean addImport(Import importnote) {
        importList.add(importnote);
        return importNoteDAL.addImport(importnote) != 0;
    }

    public List<Import> searchImport(String... conditions) {
        return importNoteDAL.searchImport(conditions);
    }

    public List<Import> findImport(String key, String value) {
        List<Import> list = new ArrayList<>();
        for (Import importnote : importList) {
            if (getValueByKey(importnote, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(importnote);
            }
        }
        return list;
    }

    public List<Import> findImportBy(Map<String, Object> conditions) {
        List<Import> imports = importList;
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            imports = findObjectsBy(entry.getKey(), entry.getValue(), imports);
        return imports;
    }

    public boolean exists(Import imports) {
        return !findImportBy(Map.of(
            "staff_id", imports.getStaff_id(),
            "received_date", imports.getReceived_date(),
            "total", imports.getTotal(),
            "supplier_id", imports.getSupplier_id()
        )).isEmpty();
    }

    public boolean exists(Map<String, Object> conditions) {
        return !findImportBy(conditions).isEmpty();
    }

    @Override
    public Object getValueByKey(Import imports, String key) {
        return switch (key) {
            case "id" -> imports.getId();
            case "staff_id" -> imports.getStaff_id();
            case "received date" -> imports.getReceived_date();
            case "total" -> imports.getTotal();
            case "supplier_id" -> imports.getSupplier_id();
            default -> null;
        };
    }

    public static void main(String[] args) {
        ImportBLL importBLL = new ImportBLL();
        System.out.println(importBLL.getImportList());
    }
}
