package com.supermarket.GUI.DialogGUI;

import com.supermarket.BLL.SupplierBLL;
import com.supermarket.DTO.Supplier;
import com.supermarket.GUI.SupplierGUI;
import com.supermarket.GUI.components.DataTable;
import com.supermarket.GUI.components.RoundedPanel;
import com.supermarket.GUI.components.RoundedScrollPane;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class FormUpdateSupplierGUI extends DialogForm{
    private final SupplierBLL supplierBLL = new SupplierBLL();
    private DataTable dataTable;
    private RoundedPanel formDetail;
    private RoundedScrollPane scrollPaneFormDetail;
    private RoundedScrollPane scrollPaneDatatable;
    private java.util.List<JLabel> attributesupplier;
    private List<JTextField> jTextFieldSupplier;
    private JButton buttonCancel;
    private JButton buttonUpdate;
    private Supplier supplier;

    public FormUpdateSupplierGUI(Supplier supplier) {
        super();
        this.supplier = supplier;
        super.setTitle("Quản lý nhà cung cấp");
        init();
        containerButton.setBackground(new Color(0xFFFFFF));
        setVisible(true);
    }

    public void init() {
        dataTable = new DataTable(new Object[][] {}, new String[] {}, e -> {});
        formDetail = new RoundedPanel();
        attributesupplier = new ArrayList<>();
        jTextFieldSupplier = new ArrayList<>();
        buttonCancel = new JButton("Huỷ");
        buttonUpdate = new JButton("Cập nhật");
        scrollPaneDatatable = new RoundedScrollPane(containerTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneFormDetail = new RoundedScrollPane(formDetail, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);


        scrollPaneDatatable.setPreferredSize(new Dimension(600, 700));
        leftContent.add(scrollPaneDatatable, BorderLayout.CENTER);
        containerTable.add(dataTable);

        scrollPaneFormDetail.setPreferredSize(new Dimension(670, 610));
        rightContent.add(scrollPaneFormDetail, BorderLayout.NORTH);

        formDetail.setBackground(new Color(0xFFBDD2DB));
        formDetail.setLayout(new MigLayout("", "50[]20[]10", "20[]20[]"));

        for (String string : new String[]{"Mã nhà cung cấp:", "Tên nhà cung cấp:", "SĐT:", "Địa chỉ:", "Email:"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("FlatLaf.style", Font.PLAIN, 16)));
            attributesupplier.add(label);
            formDetail.add(label);

            JTextField textField = new JTextField();
            if (string.equals("Mã nhà cung cấp:")) {
                textField.setText(String.valueOf(supplier.getId()));
                textField.setEnabled(false);
            }
            if (string.equals("Tên nhà cung cấp:")) {
                textField.setText(supplier.getName());
            }
            if (string.equals("SĐT:")) {
                textField.setText(supplier.getPhone());
            }
            if (string.equals("Địa chỉ:")) {
                textField.setText(supplier.getAddress());
            }
            if (string.equals("Email:")) {
                textField.setText(supplier.getEmail());
            }

            textField.setPreferredSize(new Dimension(400, 50));
            textField.setFont((new Font("FlatLaf.style", Font.BOLD, 14)));
            jTextFieldSupplier.add(textField);
            formDetail.add(textField, "wrap");
        }

        buttonCancel.setPreferredSize(new Dimension(100,40));
        buttonCancel.setFont(new Font("FlatLaf.style", Font.BOLD, 15));
        buttonCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonCancel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                buttonCancel.setBackground(new Color(0xD54218));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buttonCancel.setBackground(null);
            }
            @Override
            public void mousePressed(MouseEvent e) {
                refresh();
            }
        });
        containerButton.add(buttonCancel);

        buttonUpdate.setPreferredSize(new Dimension(100,40));
        buttonUpdate.setFont(new Font("FlatLaf.style", Font.BOLD, 15));
        buttonUpdate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonUpdate.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                updatesupplier();
            }
        });
        containerButton.add(buttonUpdate);

    }

    private void updatesupplier() {
        for (int i = 0; i < jTextFieldSupplier.size(); i++) {
            if (i != 4 && jTextFieldSupplier.get(i).getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin nhà cung cấp.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        int id = Integer.parseInt(jTextFieldSupplier.get(0).getText());
        String name = jTextFieldSupplier.get(1).getText();
        String phone = jTextFieldSupplier.get(2).getText();
        String address = jTextFieldSupplier.get(3).getText();
        String email = jTextFieldSupplier.get(4).getText();

        Supplier supplier = new Supplier(id, name, phone, address, email, false);

        String[] options = new String[]{"Huỷ", "Xác nhận"};
        int choice = JOptionPane.showOptionDialog(null, "Xác nhận cập nhật nhà cung cấp?",
            "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (choice == 1) {
            if (supplierBLL.updateSupplier(supplier).getKey()) {
                JOptionPane.showMessageDialog(null, "Cập nhật nhà cung cấp thành công!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                SupplierGUI.loadDataTable(supplierBLL.getData());
                dispose();
            } else {
                SmallDialog.showResult(supplierBLL.updateSupplier(supplier).getValue());
            }
        }

    }

    private void refresh() {
        jTextFieldSupplier.get(0).setEnabled(true);
        jTextFieldSupplier.get(0).setText(String.valueOf(supplierBLL.getAutoID(supplierBLL.searchSuppliers())));
        jTextFieldSupplier.get(0).setEnabled(false);
        jTextFieldSupplier.get(1).setText("");
        jTextFieldSupplier.get(2).setText("");
        jTextFieldSupplier.get(3).setEnabled(true);
        jTextFieldSupplier.get(3).setText("");
        jTextFieldSupplier.get(3).setEnabled(false);
        jTextFieldSupplier.get(5).setEnabled(true);
        jTextFieldSupplier.get(5).setText("");
        jTextFieldSupplier.get(5).setEnabled(false);
    }

    public void selectRowTable() {
        String id = "";
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        int indexRow = dataTable.getSelectedRow();
        id = model.getDataVector().elementAt(indexRow).get(0).toString();
        jTextFieldSupplier.get(3).setEnabled(true);
        jTextFieldSupplier.get(3).setText(id);
        jTextFieldSupplier.get(3).setEnabled(false);
    }
}
