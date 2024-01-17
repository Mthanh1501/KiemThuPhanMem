package com.supermarket.GUI;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.supermarket.BLL.CustomerBLL;
import com.supermarket.BLL.RoleBLL;
import com.supermarket.BLL.StaffBLL;
import com.supermarket.DTO.Function;
import com.supermarket.GUI.DialogGUI.ExcelDialog;
import com.supermarket.GUI.DialogGUI.FormAddCustomerGUI;
import com.supermarket.GUI.DialogGUI.FormDetailCustomerGUI;
import com.supermarket.GUI.DialogGUI.FormUpdateCustomerGUI;
import com.supermarket.GUI.components.DataTable;
import com.supermarket.GUI.components.Layout1;
import com.supermarket.GUI.components.RoundedScrollPane;
import com.supermarket.main.Mini_supermarketManagement;
import com.supermarket.utils.Excel;
import javafx.util.Pair;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CustomerGUI extends Layout1 {
    private RoleBLL roleBLL = new RoleBLL();
    private StaffBLL staffBLL = new StaffBLL();
    private CustomerBLL customerBLL = new CustomerBLL();
    private JLabel iconDetail;
    private JLabel iconAdd;
    private JLabel iconEdit;
    private JLabel iconDelete;
    private JLabel iconPDF;
    private JLabel iconExcel;
    private ExcelDialog dialogExcel;
    private static DataTable dataTable;
    private RoundedScrollPane scrollPane;
    private JTextField jTextFieldSearch;
    private JComboBox cbbAttributeProduct;
    private JComboBox cbbGender;
    private JComboBox cbbMembership;
    private Object[][] customerList;

    public CustomerGUI(List<Function> functions) {
        super();
        init(functions);

    }

    public void init(List<Function> functions) {
        customerList = new Object[0][0];
        dataTable = new DataTable(new Object[][] {},
            new String[] {"Mã khách hàng", "Tên khách hàng", "Giới tính", "Ngày sinh", "Số điện thoại", "Thành viên","Ngày đăng ký","Điểm thưởng"}, e -> {});
        scrollPane = new RoundedScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        iconDetail = new JLabel();
        iconAdd = new JLabel();
        iconEdit = new JLabel();
        iconDelete = new JLabel();
        iconPDF = new JLabel();
        iconExcel = new JLabel();
        jTextFieldSearch = new JTextField();
        cbbAttributeProduct = new JComboBox(new String[] {"Tên khách hàng", "Giới tính", "KH thành viên"});
        cbbGender = new JComboBox<>();
        cbbMembership = new JComboBox<>();
//

        if (functions.stream().anyMatch(f -> f.getName().equals("Chi tiết"))) {
            iconDetail.setIcon(new FlatSVGIcon("icon/detail.svg"));
            iconDetail.setCursor(new Cursor(Cursor.HAND_CURSOR));
            iconDetail.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    showDetailCustomer();
                }
            });
            leftMenu.add(iconDetail);
        }

        if (functions.stream().anyMatch(f -> f.getName().equals("Thêm"))) {
            iconAdd.setIcon(new FlatSVGIcon("icon/add.svg"));
            iconAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
            iconAdd.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    addCustomer();
                }
            });
            leftMenu.add(iconAdd);
        }

        if (functions.stream().anyMatch(f -> f.getName().equals("Sửa"))) {
            iconEdit.setIcon(new FlatSVGIcon("icon/edit.svg"));
            iconEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
            iconEdit.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    updateCustomer();
                }
            });
            leftMenu.add(iconEdit);
        }

        if (functions.stream().anyMatch(f -> f.getName().equals("Xóa"))) {
            iconDelete.setIcon(new FlatSVGIcon("icon/remove.svg"));
            iconDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
            iconDelete.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    deleteCustomer();
                }
            });
            leftMenu.add(iconDelete);
        }

        if (functions.stream().anyMatch(f -> f.getName().equals("Xuất"))) {
            iconPDF.setIcon(new FlatSVGIcon("icon/pdf.svg"));
            iconPDF.setCursor(new Cursor(Cursor.HAND_CURSOR));
            iconPDF.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    exportCustomer();
                }
            });
            leftMenu.add(iconPDF);
        }

        if (functions.stream().anyMatch(f -> f.getName().equals("Nhập"))) {
            dialogExcel = new ExcelDialog(List.of(
                new Pair<>("Tên", Excel.Type.STRING),
                new Pair<>("Giới tính", Excel.Type.STRING),
                new Pair<>("Ngày sinh", Excel.Type.STRING),
                new Pair<>("SĐT", Excel.Type.STRING)
            ), row -> {
                return null;
            });
            iconExcel.setIcon(new FlatSVGIcon("icon/excel.svg"));
            iconExcel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            iconExcel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    importCustomer();
                }
            });
            leftMenu.add(iconExcel);
        }

        cbbAttributeProduct.setPreferredSize(new Dimension(130, 30));
        cbbAttributeProduct.addActionListener(e -> selectSearchFilter());
        rightMenu.add(cbbAttributeProduct);

        cbbGender.addItem("Nam");
        cbbGender.addItem("Nữ");
        rightMenu.add(cbbGender);
        cbbGender.setVisible(false);
        cbbGender.addActionListener(e -> searchByGender());
//
        cbbMembership.addItem("Là thành viên");
        cbbMembership.addItem("Không là thành viên");
        rightMenu.add(cbbMembership);
        cbbMembership.setVisible(false);
        cbbMembership.addActionListener(e -> searchByMembership());
        jTextFieldSearch.setPreferredSize(new Dimension(200, 30));
        jTextFieldSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchCustomers();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchCustomers();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchCustomers();
            }
        });
        rightMenu.add(jTextFieldSearch);

        bottom.add(scrollPane, BorderLayout.CENTER);

        loadDataTable(customerBLL.getData());
    }

    private void searchCustomers() {
        if (jTextFieldSearch.getText().isEmpty()) {
            loadDataTable(customerBLL.getData());
        } else {
            loadDataTable(customerBLL.getData(customerBLL.findCustomers("name", jTextFieldSearch.getText())));
        }
    }

    private void searchByGender() {
        String isMale = "Nam";
        String isFamele = "Nữ";
        if(isMale.equals(cbbGender.getSelectedItem())){
            loadDataTable(customerBLL.getData(customerBLL.findCustomersBy(Map.of("gender", true))));
        }else{
            loadDataTable(customerBLL.getData(customerBLL.findCustomersBy(Map.of("gender", false))));
        }
    }

    private void searchByMembership() {
        String isMembership = "Là thành viên";
        String isntMembership = "Không là thành viên";
        if(isMembership.equals(cbbMembership.getSelectedItem())){
            loadDataTable(customerBLL.getData(customerBLL.findCustomersBy(Map.of("membership", true))));
        }else{
            loadDataTable(customerBLL.getData(customerBLL.findCustomersBy(Map.of("membership", false))));
        }

    }

    private void selectSearchFilter() {
        if (Objects.requireNonNull(cbbAttributeProduct.getSelectedItem()).toString().contains("KH thành viên")) {
            jTextFieldSearch.setVisible(false);
            cbbGender.setVisible(false);
            cbbMembership.setSelectedIndex(0);
            cbbMembership.setVisible(true);
            searchByMembership();
        }else if (Objects.requireNonNull(cbbAttributeProduct.getSelectedItem()).toString().contains("Giới tính")) {
            jTextFieldSearch.setVisible(false);
            cbbMembership.setVisible(false);
            cbbGender.setSelectedIndex(0);
            cbbGender.setVisible(true);
            searchByGender();
        } else {
            cbbGender.setVisible(false);
            cbbMembership.setVisible(false);
            jTextFieldSearch.setVisible(true);
            searchCustomers();
        }

    }

    private void addCustomer() {
        new FormAddCustomerGUI();
    }
//    private void updateCustomer(){ new FormUpdateCustomerGUI();}

    private void showDetailCustomer() {
        customerBLL = new CustomerBLL();
        if (dataTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn khách hàng cần xem chi tiết.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        new FormDetailCustomerGUI(customerBLL.findCustomersBy(Map.of("id", Integer.parseInt(model.getValueAt(dataTable.getSelectedRow(), 0).toString()))).get(0));
    }

    private void updateCustomer() {
        customerBLL = new CustomerBLL();
        if (dataTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn tài khoản cần cập nhật.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        new FormUpdateCustomerGUI(customerBLL.findCustomersBy(Map.of("id", Integer.parseInt(model.getValueAt(dataTable.getSelectedRow(), 0).toString()))).get(0));

    }

    private void deleteCustomer() {
        if (dataTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn khách hàng cần xoá.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();

        String[] options = new String[]{"Huỷ", "Xác nhận"};
        int choice = JOptionPane.showOptionDialog(null, "Xác nhận xoá khách hàng?",
            "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (choice == 1) {
            if (customerBLL.deleteCustomer(customerBLL.findCustomersBy(Map.of("id", Integer.parseInt(model.getValueAt(dataTable.getSelectedRow(), 0).toString()))).get(0))) {
                JOptionPane.showMessageDialog(null, "Xoá khách hàng thành công!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);

                loadDataTable(customerBLL.getData());
            } else {
                JOptionPane.showMessageDialog(null, "Xoá khách hàng không thành công!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void importCustomer() {
        dialogExcel.setVisible(true);
        if (!dialogExcel.isCancel()) {
            JOptionPane.showMessageDialog(Mini_supermarketManagement.homeGUI,
                "Nhập dữ liệu thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            loadDataTable(customerBLL.getData());
        }
    }

    private void exportCustomer() {
        File file = Excel.saveFile();
        if (file == null)
            return;
        Pair<Boolean, String> result = Excel.exportExcel(file, dataTable.getModel());
        if (result.getKey())
            JOptionPane.showMessageDialog(Mini_supermarketManagement.homeGUI, result.getValue(), "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        else
            JOptionPane.showMessageDialog(Mini_supermarketManagement.homeGUI, result.getValue(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    public static void loadDataTable(Object[][] objects) {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
//        CustomerBLL customerBLL = new CustomerBLL();
//        for (Object[] object : objects) {
//            int roleId = Integer.parseInt(object[3].toString());
//            int staffId = Integer.parseInt(object[5].toString());
//            object[3] = roleBLL.findRolesBy(Map.of("id", roleId)).get(0).getName();
//            object[5] = staffBLL.findStaffsBy(Map.of("id", staffId)).get(0).getName();
//        }

        model.setRowCount(0);
        for (Object[] object : objects) {
            model.addRow(object);
        }
    }
}
