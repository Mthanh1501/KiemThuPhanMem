package com.supermarket.BLL;

import com.supermarket.DAL.StaffDAL;
import com.supermarket.DTO.Staff;
import com.supermarket.utils.VNString;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StaffBLL extends Manager<Staff>{
    private StaffDAL staffDAL;
    private List<Staff> staffList;

    public StaffBLL() {
        staffDAL = new StaffDAL();
        staffList = searchStaffs("deleted = 0");
    }

    public StaffDAL getStaffDAL() {
        return staffDAL;
    }

    public void setStaffDAL(StaffDAL staffDAL) {
        this.staffDAL = staffDAL;
    }

    public List<Staff> getStaffList() {
        return staffList;
    }

    public void setStaffList(List<Staff> staffList) {
        this.staffList = staffList;
    }

    public Object[][] getData() {
        return getData(staffList);
    }



    public Pair<Boolean, String> addStaff(Staff staff) {
        Pair<Boolean, String> result;
        result = validateName(staff.getName());
        if(!result.getKey()){
            return new Pair<>(false,result.getValue());
        }
        result = validatePhone(staff.getPhone());
        if(!result.getKey()){
            return new Pair<>(false,result.getValue());

        }
        result = validateEmail(staff.getEmail());
        if(!result.getKey()){
            return new Pair<>(false,result.getValue());

        }
        result = exists(staff);
        if (result.getKey()) {
            return new Pair<>(false,result.getValue());

        }
        staffList.add(staff);
        staffDAL.addStaff(staff);
        return new Pair<>(true,"");
    }

    public Pair<Boolean, String> updateStaff(Staff staff) {
        Pair<Boolean, String> result;

        result = validateName(staff.getName());
        if(!result.getKey()){
            return new Pair<>(false,result.getValue());
        }

        result = validatePhone(staff.getPhone());
        if(!result.getKey()){
            return new Pair<>(false,result.getValue());
        }

        result = validateEmail(staff.getEmail());
        if(!result.getKey()){
            return new Pair<>(false,result.getValue());
        }
        staffList.set(getIndex(staff, "id", staffList), staff);
        staffDAL.updateStaff(staff);
        return new Pair<>(true,"");
    }

    public boolean deleteStaff(Staff staff) {
        staffList.remove(getIndex(staff, "id", staffList));
        return staffDAL.deleteStaff("id = " + staff.getId()) != 0;
    }

    public List<Staff> searchStaffs(String... conditions) {
        return staffDAL.searchStaffs(conditions);
    }

    public List<Staff> findStaffs(String key, String value) {
        List<Staff> list = new ArrayList<>();
        for (Staff staff : staffList) {
            if (getValueByKey(staff, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(staff);
            }
        }
        return list;
    }

    public List<Staff> findStaffsBy(Map<String, Object> conditions) {
        List<Staff> staffs = staffList;
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            staffs = findObjectsBy(entry.getKey(), entry.getValue(), staffs);
        return staffs;
    }


    public Pair<Boolean, String> exists(Staff newStaff){
        List<Staff> staffs = staffDAL.searchStaffs("phone = '" + newStaff.getPhone() + "'", "deleted = 0");
        if(!staffs.isEmpty()){
            return new Pair<>(true, "Số điện thoại nhân viên đã tồn tại.");
        }
        staffs = staffDAL.searchStaffs("email = '" + newStaff.getEmail()+ "'", "deleted = 0");
        if(!staffs.isEmpty()){
            return new Pair<>(true, "Email nhân viên đã tồn tại.");
        }
        return new Pair<>(false, "");
    }




    public Pair<Boolean, String>validateName(String name){
        if(name.isBlank())
            return new Pair<>(false,"Tên nhân viên không được bỏ trống.");
        if(VNString.containsNumber(name))
            return new Pair<>(false,"Tên nhân viên không không được chứa số.");
        if(VNString.containsSpecial(name))
            return new Pair<>(false,"Tên nhân viên không không được chứa ký tự đặc biệt.");
        return new Pair<>(true,name);
    }



    public Pair<Boolean, String>validatePhone(String phone){
        if(phone.isBlank())
            return new Pair<>(false,"Số điện thoại nhân viên không được bỏ trống.");
        if(!VNString.checkFormatPhone(phone))
            return new Pair<>(false,"Số điện thoại nhân viên phải bắt đầu với \"0x\" hoặc \"+84x\" hoặc \"84x\" với \"x\" thuộc \\{\\\\3, 5, 7, 8, 9\\}\\\\.");
        return new Pair<>(true,phone);
    }



    public Pair<Boolean, String>validateEmail(String email){
        if(email.isBlank())
            return new Pair<>(false,"Email nhân viên không được để trống.");
        if(VNString.containsUnicode(email))
            return new Pair<>(false,"Email nhân viên không được chứa unicode.");
        if(!VNString.checkFormatOfEmail(email))
            return new Pair<>(false,"Email nhân viên phải theo định dạng (username@domain.name).");
        return new Pair<>(true,email);
    }



//    public boolean validateDate(Date date) throws Exception {
//
//        if (!ParseDate(date.toString())) {
//            JOptionPane.showMessageDialog(null, "Định dạng ngày không hợp lệ.", "Thông báo", JOptionPane.ERROR_MESSAGE);
//            return false;
//        }
//
//        int day = date.getDate();
//        int month = date.getMonth();
//        int year = date.getYear();
//        if (!Date.isValidDay(day, month, year)) {
//            JOptionPane.showMessageDialog(null, "Ngày không hợp lệ.", "Thông báo", JOptionPane.ERROR_MESSAGE);
//            return false;
//        }
//
//        return true;
//    }
//    public static boolean ParseDate(String str) {
//        if (str.matches("^\\d{4}[-/]((0?[1-9]|1[0-2])[-/](0?[1-9]|[12][0-9]|3[01]))$")) {
//            return true;
//        } else {
//            return false;
//        }
//    }



    @Override
    public Object getValueByKey(Staff staff, String key) {
        return switch (key) {
            case "id" -> staff.getId();
            case "name" -> staff.getName();
            case "gender" -> staff.getGender();
            case "birthday" -> staff.getBirthday();
            case "phone" -> staff.getPhone();
            case "address" -> staff.getAddress();
            case "email" -> staff.getEmail();
            case "entry_date" -> staff.getEntry_date();
            default -> null;
        };
    }
}
