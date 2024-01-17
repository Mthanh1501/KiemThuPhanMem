package com.supermarket.BLL;

import com.supermarket.DAL.SupplierDAL;
import com.supermarket.DTO.Supplier;
import com.supermarket.utils.VNString;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SupplierBLL extends Manager<Supplier> {
    private SupplierDAL supplierDAL;
    private List<Supplier> supplierList;

    public SupplierBLL() {
        supplierDAL = new SupplierDAL();
        supplierList = searchSuppliers("deleted = 0");
    }

    public SupplierDAL getSupplierDAL() {
        return supplierDAL;
    }

    public void setSupplierDAL(SupplierDAL supplierDAL) {
        this.supplierDAL = supplierDAL;
    }

    public List<Supplier> getSupplierList() {
        return supplierList;
    }

    public void setSupplierList(List<Supplier> supplierList) {
        this.supplierList = supplierList;
    }

    public Object[][] getData() {
        return getData(supplierList);
    }

    public Pair<Boolean, String> addSupplier(Supplier supplier) {
        Pair<Boolean, String> result;

        result = validateName(supplier.getName());
        if(!result.getKey()){
            return new Pair<>(false,result.getValue());
        }

        result = validatePhone(supplier.getPhone());
        if(!result.getKey()){
            return new Pair<>(false,result.getValue());
        }

        result = validateEmail(supplier.getEmail());
        if(!result.getKey()){
            return new Pair<>(false,result.getValue());
        }
        result = exists(supplier);
        if (result.getKey()) {
            return new Pair<>(false,result.getValue());
        }
        supplierList.add(supplier);
        supplierDAL.addSupplier(supplier);
        return new Pair<>(true,"");
    }

    public Pair<Boolean, String>  updateSupplier(Supplier supplier) {
        Pair<Boolean, String> result;

        result = validateName(supplier.getName());
        if(!result.getKey()){
            return new Pair<>(false,result.getValue());
        }

        result = validatePhone(supplier.getPhone());
        if(!result.getKey()){
            return new Pair<>(false,result.getValue());
        }

        result = validateEmail(supplier.getEmail());
        if(!result.getKey()){
            return new Pair<>(false,result.getValue());
        }
        supplierList.set(getIndex(supplier, "id", supplierList), supplier);
        supplierDAL.updateSupplier(supplier);
        return new Pair<>(true,"");
    }

    public boolean deleteSupplier(Supplier supplier) {
        supplierList.remove(getIndex(supplier, "id", supplierList));
        return supplierDAL.deleteSupplier("id = " + supplier.getId()) != 0;
    }

    public List<Supplier> searchSuppliers(String... conditions) {
        return supplierDAL.searchSuppliers(conditions);
    }

    public List<Supplier> findSuppliers(String key, String value) {
        List<Supplier> list = new ArrayList<>();
        for (Supplier supplier : supplierList) {
            if (getValueByKey(supplier, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(supplier);
            }
        }
        return list;
    }

    public List<Supplier> findSuppliersBy(Map<String, Object> conditions) {
        List<Supplier> suppliers = supplierList;
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            suppliers = findObjectsBy(entry.getKey(), entry.getValue(), suppliers);
        return suppliers;
    }

    public Pair<Boolean, String> exists(Supplier newSupplier){
        List<Supplier> suppliers = supplierDAL.searchSuppliers("phone = '" + newSupplier.getPhone() + "'", "deleted = 0");
        if(!suppliers.isEmpty()){
            return new Pair<>(true, "Số điện thoại nhà cung cấp đã tồn tại.");
        }
        suppliers = supplierDAL.searchSuppliers("email = '" + newSupplier.getEmail()+ "'", "deleted = 0");
        if(!suppliers.isEmpty()){
            return new Pair<>(true, "Email nhà cung cấp đã tồn tại.");
        }
        return new Pair<>(false, "");
    }

    private static Pair<Boolean, String> validateName(String name) {
        if (name.isBlank())
            return new Pair<>(false, "Tên nhà cung cấp không được để trống.");
        if (VNString.containsSpecial(name))
            return new Pair<>(false, "Tên nhà cung cấp không được chứa ký tự đặc biệt.");
        if (VNString.containsNumber(name))
            return new Pair<>(false, "Tên nhà cung cấp không được chứa số.");
        return new Pair<>(true, name);
    }
    public Pair<Boolean, String>validatePhone(String phone){
        if(phone.isBlank())
            return new Pair<>(false,"Số điện thoại nhà cung cấp không được bỏ trống.");
        if(!VNString.checkFormatPhone(phone))
            return new Pair<>(false,"Số điện thoại nhà cung cấp phải bắt đầu với \"0x\" hoặc \"+84x\" hoặc \"84x\" với \"x\" thuộc \\{\\\\3, 5, 7, 8, 9\\}\\\\.");
        return new Pair<>(true,phone);
    }



    public Pair<Boolean, String>validateEmail(String email){
        if(email.isBlank())
            return new Pair<>(false,"Email nhà cung cấp không được để trống.");
        if(VNString.containsUnicode(email))
            return new Pair<>(false,"Email nhà cung cấp không được chứa unicode.");
        if(!VNString.checkFormatOfEmail(email))
            return new Pair<>(false,"Email nhà cung cấp phải theo định dạng (username@domain.name).");
        return new Pair<>(true,email);
    }


    @Override
    public Object getValueByKey(Supplier supplier, String key) {
        return switch (key) {
            case "id" -> supplier.getId();
            case "name" -> supplier.getName();
            case "phone" -> supplier.getPhone();
            case "address" -> supplier.getAddress();
            case "email" -> supplier.getEmail();

            default -> null;
        };
    }
}
