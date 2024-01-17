package com.supermarket.GUI.DialogGUI;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.supermarket.BLL.RoleBLL;
import com.supermarket.BLL.StaffBLL;
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

public class FormDetailStaffGUI extends DialogForm{
    private final StaffBLL staffBLL = new StaffBLL();
    private final RoleBLL roleBLL = new RoleBLL();
    private DataTable dataTable;
    private RoundedPanel formDetail;
    private RoundedScrollPane scrollPaneFormDetail;
    private RoundedScrollPane scrollPaneDatatable;
    private List<JLabel> attributeStaff;
    private List<JTextField> jTextFieldStaff;
    private Staff staff;
    private JDateChooser lastSignedIn;
    private JTextField dateTextField;

    public FormDetailStaffGUI(Staff staff) {
        super();
        this.staff = staff;
        super.setTitle("Xem thông tin nhân viên");
        init();
        containerButton.setBackground(new Color(0xFFFFFF));
        setVisible(true);
    }

    public void init() {
        dataTable = new DataTable(new Object[][] {}, new String[] {}, e -> {});
        formDetail = new RoundedPanel();
        attributeStaff = new ArrayList<>();
        jTextFieldStaff = new ArrayList<>();
        scrollPaneDatatable = new RoundedScrollPane(containerTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneFormDetail = new RoundedScrollPane(formDetail, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);


        scrollPaneDatatable.setPreferredSize(new Dimension(600, 700));
        leftContent.add(scrollPaneDatatable, BorderLayout.CENTER);
        containerTable.setLayout(new GridBagLayout());
        containerTable.add(new FlatSVGIcon("icon/stafff.svg"));

        scrollPaneFormDetail.setPreferredSize(new Dimension(670, 610));
        rightContent.add(scrollPaneFormDetail, BorderLayout.NORTH);

        formDetail.setBackground(new Color(0xFFBDD2DB));
        formDetail.setLayout(new MigLayout("", "50[]20[]10", "20[]20[]"));

        for (String string : new String[]{"Mã nhân viên:", "Tên nhân viên:", "Giới tính:", "Ngày sinh:", "Số điện thoại:", "Địa chỉ:","Email:","Ngày vào làm:"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("FlatLaf.style", Font.PLAIN, 16)));
            attributeStaff.add(label);
            formDetail.add(label);

            JTextField textField = new JTextField();
            if (string.equals("Mã nhân viên:")) {
                textField.setText(String.valueOf(staff.getId()));
            }
            if (string.equals("Tên nhân viên:")) {
                textField.setText(staff.getName());
            }
            if (string.equals("Giới tính:")) {
                String gender = staff.getGender() ? "Nam":"Nữ";
                textField.setText(gender);
            }
            if (string.equals("Ngày sinh:")) {
                textField.setText(String.valueOf(staff.getBirthday()));
            }
            if (string.equals("Số điện thoại:")) {
                textField.setText(staff.getPhone());
            }
            if (string.equals("Địa chỉ:")) {
                textField.setText(staff.getAddress());
            }
            if (string.equals("Email:")) {
                textField.setText(staff.getEmail());
            }
            if (string.equals("Ngày vào làm:")) {
                textField.setText(String.valueOf(staff.getEntry_date()));
            }
            textField.setEditable(false);
            textField.setPreferredSize(new Dimension(400, 50));
            textField.setFont((new Font("FlatLaf.style", Font.PLAIN, 14)));
            jTextFieldStaff.add(textField);
            formDetail.add(textField, "wrap");
        }
    }

//    private void loadTableStaff() {
//        dataTable = new DataTable(staffBLL.getData(), new String[] {"Mã nhân viên", "Họ tên", "Giới tính", "Ngày sinh", "Điện thoại", "Địa chỉ", "Email", "Ngày vào làm"}, e -> {});
//        dataTable.setPreferredSize(new Dimension(1500, 700));
//        containerTable.removeAll();
//        containerTable.add(dataTable.getTableHeader(), BorderLayout.NORTH);
//        containerTable.add(dataTable, BorderLayout.CENTER);
//
//        Staff staff = staffBLL.findStaffsBy(Map.of("id", staff.getStaffID())).get(0);
//        int index = staffBLL.getIndex(staff, "id", staffBLL.getStaffList());
//        dataTable.setRowSelectionInterval(index, index);
//
//        containerTable.repaint();
//        containerTable.revalidate();
//        scrollPaneDatatable.setViewportView(containerTable);
//    }

//    private void loadTableRole() {
//        dataTable = new DataTable(roleBLL.getData(), new String[] {"Mã chức vụ", "Tên chức vụ"}, e -> {});
//        containerTable.removeAll();
//        containerTable.add(dataTable.getTableHeader(), BorderLayout.NORTH);
//        containerTable.add(dataTable, BorderLayout.CENTER);
//
//        Role role = roleBLL.findRolesBy(Map.of("id", staff.getRoleID())).get(0);
//        int index = roleBLL.getIndex(role, "id", roleBLL.getRoleList());
//        dataTable.setRowSelectionInterval(index, index);
//
//        containerTable.repaint();
//        containerTable.revalidate();
//        scrollPaneDatatable.setViewportView(containerTable);
//    }
}
