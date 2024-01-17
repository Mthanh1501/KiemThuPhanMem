package com.supermarket.DAL;

import com.supermarket.DTO.Staff;
import com.supermarket.utils.Date;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StaffDAL extends Manager{
    public StaffDAL() {
        super("staff",
            List.of("id",
                "name",
                "gender",
                "birthdate",
                "phone",
                "address",
                "email",
                "entry_date",
                "deleted"));
    }

    public List<Staff> convertToStaffs(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Staff(
                    Integer.parseInt(row.get(0)), // id
                    row.get(1), // name
                    Boolean.parseBoolean(row.get(2)), // gender
                    Date.parseDate(row.get(3)), // birthday
                    row.get(4), // phone
                    row.get(5), // address
                    row.get(6), // email
                    Date.parseDate(row.get(7)), // entry_date
                    Boolean.parseBoolean(row.get(8))    // deleted
                );
            } catch (Exception e) {
                System.out.println("Error occurred in StaffDAL.convertToStaffs(): " + e.getMessage());
            }
            return new Staff();
        });
    }

    public int addStaff(Staff staff) {
        try {
            return create(staff.getId(),
                staff.getName(),
                staff.getGender(),
                staff.getBirthday(),
                staff.getPhone(),
                staff.getAddress(),
                staff.getEmail(),
                staff.getEntry_date(),
                false
            ); // staff khi tạo mặc định deleted = 0
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in StaffDAL.addStaff(): " + e.getMessage());
        }
        return 0;
    }

    public int updateStaff(Staff staff) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(staff.getId());
            updateValues.add(staff.getName());
            updateValues.add(staff.getGender());
            updateValues.add(staff.getBirthday());
            updateValues.add(staff.getPhone());
            updateValues.add(staff.getAddress());
            updateValues.add(staff.getEmail());
            updateValues.add(staff.getEntry_date());
            updateValues.add(staff.isDeleted());
            return update(updateValues, "id = " + staff.getId());
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in StaffDAL.updateStaff(): " + e.getMessage());
        }
        return 0;
    }

    public int deleteStaff(String... conditions) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(true);
            return update(updateValues, conditions);
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in StaffDAL.deleteStaff(): " + e.getMessage());
        }
        return 0;
    }

    public List<Staff> searchStaffs(String... conditions) {
        try {
            return convertToStaffs(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in StaffDAL.searchStaffs(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
