package com.supermarket.BLL;

import com.supermarket.DAL.CustomerDAL;
import com.supermarket.DTO.Customer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomerBLL extends Manager<Customer>{
    private CustomerDAL customerDAL;
    private List<Customer> customerList;

    public CustomerBLL() {
        customerDAL = new CustomerDAL();
        customerList = searchCustomers("deleted = 0");
    }

    public CustomerDAL getCustomerDAL() {
        return customerDAL;
    }

    public void setCustomerDAL(CustomerDAL customerDAL) {
        this.customerDAL = customerDAL;
    }

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }

    public Object[][] getData() {
        return getData(customerList);
    }

    public boolean addCustomer(Customer customer) {

        if (!validateName(customer.getName())) {
            JOptionPane.showMessageDialog(null,"Tên không hợp lệ." ,"Thông báo",JOptionPane.ERROR_MESSAGE);
            return false;
        }


        if(!validatePhone(customer.getPhone())){
            JOptionPane.showMessageDialog(null,"Số điện thoại không hợp lệ." ,"Thông báo",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(exists(customer.getPhone())){
            return false;
        }
        customerList.add(customer);
        return customerDAL.addCustomer(customer) != 0;
    }

    public boolean updateCustomer(Customer customer) {
        if (!validateName(customer.getName())) {
            JOptionPane.showMessageDialog(null,"Tên không hợp lệ." ,"Thông báo",JOptionPane.ERROR_MESSAGE);
            return false;
        }


        if(!validatePhone(customer.getPhone())){
            JOptionPane.showMessageDialog(null,"Số điện thoại không hợp lệ." ,"Thông báo",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(exists(customer.getPhone())){
            return false;
        }
        customerList.set(getIndex(customer, "id", customerList), customer);
        return customerDAL.updateCustomer(customer) != 0;
    }

    public boolean deleteCustomer(Customer customer) {
        customerList.remove(getIndex(customer, "id", customerList));
        return customerDAL.deleteCustomer("id = " + customer.getId()) != 0;
    }

    public List<Customer> searchCustomers(String... conditions) {
        return customerDAL.searchCustomers(conditions);
    }

    public List<Customer> findCustomers(String key, String value) {
        List<Customer> list = new ArrayList<>();
        for (Customer customer : customerList) {
            if (getValueByKey(customer, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(customer);
            }
        }
        return list;
    }

    public List<Customer> findCustomersBy(Map<String, Object> conditions) {
        List<Customer> customers = customerList;
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            customers = findObjectsBy(entry.getKey(), entry.getValue(), customers);
        return customers;
    }

//    public boolean exists(Customer customer) {
//        return !findCustomersBy(Map.of(
//            "name", customer.getName(),
//            "gender", customer.isGender(),
//            "birthday", customer.getBirthday(),
//            "phone", customer.getPhone(),
//            "membership", customer.isMembership(),
//            "signed_up_date", customer.getSigned_up_date(),
//            "point", customer.getPoint()
//        )).isEmpty();
//    }
//    public boolean exists(Map<String, Object> conditions) {
//        return !findCustomersBy(conditions).isEmpty();
//    }


    public boolean exists(String phone) {
        for (Customer customer : customerList) {
            if (customer.getPhone().equals(phone)) {
                JOptionPane.showMessageDialog(null, "Số điện thoại khách hàng đã tồn tại.", "Thông báo", JOptionPane.ERROR_MESSAGE);

                return true;
            }
        }
        return false;
    }


    public boolean validateName(String name) {
        // Kiểm tra tên có tồn tại và có đúng định dạng tiếng Việt có dấu câu, có thể viết hoa, viết thường, không được để trống
        return name != null && !name.isEmpty() && name.matches("[\\p{L}\\p{P}\\s]+");
    }

    public  boolean validatePhone(String phone){
        return phone.matches("^(\\+?84|0)[35789]\\d{8}$");
    }



    @Override
    public Object getValueByKey(Customer customer, String key) {
        return switch (key) {
            case "id" -> customer.getId();
            case "name" -> customer.getName();
            case "gender" -> customer.isGender();
            case "birthday" -> customer.getBirthday();
            case "phone" -> customer.getPhone();
            case "membership" -> customer.isMembership();
            case "signed_up_date" -> customer.getSigned_up_date();
            case "point" -> customer.getPoint();
            default -> null;
        };
    }
}
