package com.supermarket.GUI.DialogGUI;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.supermarket.BLL.RoleBLL;
import com.supermarket.BLL.StaffBLL;
import com.supermarket.DTO.Account;
import com.supermarket.DTO.Customer;
import com.supermarket.DTO.Role;
import com.supermarket.DTO.Staff;
import com.supermarket.GUI.components.DataTable;
import com.supermarket.GUI.components.RoundedPanel;
import com.supermarket.GUI.components.RoundedScrollPane;
import com.toedter.calendar.JDateChooser;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FormDetailCustomerGUI extends DialogForm{
    private final StaffBLL staffBLL = new StaffBLL();
    private final RoleBLL roleBLL = new RoleBLL();
    private DataTable dataTable;
    private RoundedPanel formDetail;
    private RoundedScrollPane scrollPaneFormDetail;
    private RoundedScrollPane scrollPaneDatatable;
    private List<JLabel> attributeAccount;
    private List<JTextField> jTextFieldAccount;
    private Account account;
    private Customer customer;
    private JDateChooser lastSignedIn;
    private JTextField dateTextField;

    public FormDetailCustomerGUI(Customer customer) {
        super();
        this.customer = customer;
        super.setTitle("Quản lý khách hàng");
        init();
        containerButton.setBackground(new Color(0xFFFFFF));
        setVisible(true);
    }

    public void init() {
        dataTable = new DataTable(new Object[][] {}, new String[] {}, e -> {});
        formDetail = new RoundedPanel();
        attributeAccount = new ArrayList<>();
        jTextFieldAccount = new ArrayList<>();
        scrollPaneDatatable = new RoundedScrollPane(containerTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneFormDetail = new RoundedScrollPane(formDetail, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);


        scrollPaneDatatable.setPreferredSize(new Dimension(600, 700));
        leftContent.add(scrollPaneDatatable, BorderLayout.CENTER);
        containerTable.setLayout(new GridBagLayout());
        containerTable.add(new FlatSVGIcon("icon/customerr.svg"));

        scrollPaneFormDetail.setPreferredSize(new Dimension(670, 610));
        rightContent.add(scrollPaneFormDetail, BorderLayout.NORTH);

        formDetail.setBackground(new Color(0xFFBDD2DB));
        formDetail.setLayout(new MigLayout("", "50[]20[]10", "20[]20[]"));

        for (String string : new String[]{"Mã khách hàng:", "Tên khách hàng:", "Giới tính:", "Ngày sinh:", "Số điện thoại:", "Thành viên:","Ngày đăng ký thành viên:","Điểm thưởng:"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("FlatLaf.style", Font.PLAIN, 16)));
            attributeAccount.add(label);
            formDetail.add(label);

            JTextField textField = new JTextField();
            if (string.equals("Mã khách hàng:")) {
                textField.setText(String.valueOf(customer.getId()));
            }
            if (string.equals("Tên khách hàng:")) {
                textField.setText(customer.getName());
            }
            if (string.equals("Giới tính:")) {
                String gender1 = customer.isGender()?  "Nam": "Nữ";
                textField.setText(gender1);
            }
            if (string.equals("Ngày sinh:")) {
                textField.setText(String.valueOf(customer.getBirthday()));
            }
            if (string.equals("Số điện thoại:")) {
                textField.setText(customer.getPhone());
            }
            if (string.equals("Thành viên:")) {
                String membership = customer.isMembership()? "Có":"Không";
                textField.setText(membership);
            }
            if (string.equals("Ngày đăng ký thành viên:")) {
                textField.setText(customer.getSigned_up_date().toString());
            }
            if (string.equals("Điểm thưởng:")) {
                textField.setText(String.valueOf(customer.getPoint()));
            }
            textField.setEditable(false);
            textField.setPreferredSize(new Dimension(400, 50));
            textField.setFont((new Font("FlatLaf.style", Font.PLAIN, 14)));
            jTextFieldAccount.add(textField);
            formDetail.add(textField, "wrap");
        }
    }

    private void loadTableStaff() {
        dataTable = new DataTable(staffBLL.getData(), new String[] {"Mã nhân viên", "Họ tên", "Giới tính", "Ngày sinh", "Điện thoại", "Địa chỉ", "Email", "Ngày vào làm"}, e -> {});
        dataTable.setPreferredSize(new Dimension(1500, 700));
        containerTable.removeAll();
        containerTable.add(dataTable.getTableHeader(), BorderLayout.NORTH);
        containerTable.add(dataTable, BorderLayout.CENTER);

        Staff staff = staffBLL.findStaffsBy(Map.of("id", account.getStaffID())).get(0);
        int index = staffBLL.getIndex(staff, "id", staffBLL.getStaffList());
        dataTable.setRowSelectionInterval(index, index);

        containerTable.repaint();
        containerTable.revalidate();
        scrollPaneDatatable.setViewportView(containerTable);
    }

    private void loadTableRole() {
        dataTable = new DataTable(roleBLL.getData(), new String[] {"Mã chức vụ", "Tên chức vụ"}, e -> {});
        containerTable.removeAll();
        containerTable.add(dataTable.getTableHeader(), BorderLayout.NORTH);
        containerTable.add(dataTable, BorderLayout.CENTER);

        Role role = roleBLL.findRolesBy(Map.of("id", account.getRoleID())).get(0);
        int index = roleBLL.getIndex(role, "id", roleBLL.getRoleList());
        dataTable.setRowSelectionInterval(index, index);

        containerTable.repaint();
        containerTable.revalidate();
        scrollPaneDatatable.setViewportView(containerTable);
    }
}
