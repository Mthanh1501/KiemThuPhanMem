package com.supermarket.DAL;

import com.supermarket.DTO.Customer;
import com.supermarket.utils.Date;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAL extends Manager{
    public CustomerDAL() {
        super("customer",
            List.of("id",
                "name",
                "gender",
                "birthdate",
                "phone",
                "membership",
                "signed_up_date",
                "point",
                "deleted"));
    }

    public List<Customer> convertToCustomers(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Customer(
                    Integer.parseInt(row.get(0)), // id
                    row.get(1), // name
                    Boolean.parseBoolean(row.get(2)), // gender
                    Date.parseDate(row.get(3)), // birthdate
                    row.get(4), // phone
                    Boolean.parseBoolean(row.get(5)), // membership
                    Date.parseDate(row.get(6)), // signed_up_date
                    Integer.parseInt(row.get(7)), // point
                    Boolean.parseBoolean(row.get(8))    // deleted
                );
            } catch (Exception e) {
                System.out.println("Error occurred in CustomerDAL.convertToCustomers(): " + e.getMessage());
            }
            return new Customer();
        });
    }

    public int addCustomer(Customer customer) {
        try {
            return create(customer.getId(),
                customer.getName(),
                customer.isGender(),
                customer.getBirthday(),
                customer.getPhone(),
                customer.isMembership(),
                customer.getSigned_up_date(),
                customer.getPoint(),
                false
            ); // customer khi tạo mặc định deleted = 0
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in CustomerDAL.addCustomer(): " + e.getMessage());
        }
        return 0;
    }

    public int updateCustomer(Customer customer) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(customer.getId());
            updateValues.add(customer.getName());
            updateValues.add(customer.isGender());
            updateValues.add(customer.getBirthday());
            updateValues.add(customer.getPhone());
            updateValues.add(customer.isMembership());
            updateValues.add(customer.getSigned_up_date());
            updateValues.add(customer.getPoint());
            updateValues.add(customer.isDeleted());
            return update(updateValues, "id = " + customer.getId());
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in CustomerDAL.updateCustomer(): " + e.getMessage());
        }
        return 0;
    }

    public int deleteCustomer(String... conditions) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(true);
            return update(updateValues, conditions);
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in CustomerDAL.deleteCustomer(): " + e.getMessage());
        }
        return 0;
    }

    public List<Customer> searchCustomers(String... conditions) {
        try {
            return convertToCustomers(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in CustomerDAL.searchCustomers(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
