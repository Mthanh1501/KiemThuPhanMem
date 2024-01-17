package com.supermarket.GUI.DialogGUI;

import com.supermarket.BLL.ProductBLL;
import com.supermarket.BLL.ShipmentBLL;
import com.supermarket.BLL.StaffBLL;
import com.supermarket.BLL.SupplierBLL;
import com.supermarket.DTO.*;
import com.supermarket.GUI.components.DataTable;
import com.supermarket.GUI.components.RoundedPanel;
import com.supermarket.GUI.components.RoundedScrollPane;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FormDetailImportGUI extends DialogForm{
    private Import importNote;
    private DataTable dataTable;
    private RoundedPanel formDetail;
    private RoundedScrollPane scrollPaneDatatable;
    private RoundedScrollPane scrollPaneImportDetail;
    private java.util.List<JLabel> attributeProduct;
    private List<JTextField> jTextFieldImport;
    public FormDetailImportGUI(Import importNote) {
        super();
        this.importNote = importNote;
        super.setTitle("Quản lý phiếu nhập");
        init();
        containerButton.setBackground(new Color(0xFFFFFF));
        setVisible(true);
    }

    private void init() {
        dataTable = new DataTable(new Object[][] {}, new String[] {}, e -> {});
        formDetail = new RoundedPanel();
        attributeProduct = new ArrayList<>();
        jTextFieldImport = new ArrayList<>();
        scrollPaneDatatable = new RoundedScrollPane(containerTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneImportDetail = new RoundedScrollPane(formDetail, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scrollPaneDatatable.setPreferredSize(new Dimension(600, 700));
        leftContent.add(scrollPaneDatatable, BorderLayout.CENTER);
        containerTable.add(dataTable);

        scrollPaneImportDetail.setPreferredSize(new Dimension(670, 610));
        rightContent.add(scrollPaneImportDetail, BorderLayout.NORTH);

        formDetail.setBackground(new Color(0xFFBDD2DB));
        formDetail.setLayout(new MigLayout("", "50[]20[]10", "20[]20[]"));

        for (String string : new String[] {"Mã phiếu nhập:", "Mã nhân viên:", "Ngày nhập:", "Tổng tiền:", "Mã nhà cung cấp:"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("FlatLaf.style", Font.PLAIN, 16)));
            attributeProduct.add(label);
            formDetail.add(label);

            JTextField textField = new JTextField();
            if (string.equals("Mã phiếu nhập:")) {
                textField.setText(String.valueOf(importNote.getId()));
                textField.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        loadTableImport_Detail();
                    }
                });
            }
            if (string.equals("Mã nhân viên:")) {
                textField.setText(String.valueOf(importNote.getStaff_id()));
                textField.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        loadTableStaff();
                    }
                });
            }
            if (string.equals("Ngày nhập:")) {
                textField.setText(String.valueOf(importNote.getReceived_date()));
                textField.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        loadTableImport_Detail();
                    }
                });
            }
            if (string.equals("Tổng tiền:")) {
                textField.setText(String.valueOf(importNote.getTotal()));
                textField.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        loadTableImport_Detail();
                    }
                });
            }
            if (string.equals("Mã nhà cung cấp:")) {
                textField.setText(String.valueOf(importNote.getSupplier_id()));
                textField.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        loadTableSupplier();
                    }
                });
            }
            textField.setEditable(false);
            textField.setPreferredSize(new Dimension(400, 50));
            textField.setFont((new Font("FlatLaf.style", Font.PLAIN, 14)));
            jTextFieldImport.add(textField);
            formDetail.add(textField, "wrap");
        }
        loadTableImport_Detail();
    }

    private void loadTableImport_Detail() {
        Object[][] importNoteDetailList = new Object[0][0];
        ShipmentBLL shipmentBLL = new ShipmentBLL();
        ProductBLL productBLL = new ProductBLL();
        for (Shipment shipment : shipmentBLL.getShipmentList()) {
            if (shipment.getImport_id() == importNote.getId()) {
                Object[] object = new Object[3];
                Product product = productBLL.findProductsBy(Map.of("id", shipment.getProduct_id())).get(0);
                object[0] = product.getName();
                object[1] = shipment.getUnit_price();
                object[2] = shipment.getQuantity();
                importNoteDetailList = Arrays.copyOf(importNoteDetailList, importNoteDetailList.length + 1);
                importNoteDetailList[importNoteDetailList.length - 1] = object;
            }

        }

        dataTable = new DataTable(importNoteDetailList, new String[] {"Tên sản phẩm", "Đơn giá", "Số lượng"}, e -> {});
        containerTable.removeAll();
        containerTable.add(dataTable.getTableHeader(), BorderLayout.NORTH);
        containerTable.add(dataTable, BorderLayout.CENTER);

        containerTable.repaint();
        containerTable.revalidate();
        scrollPaneDatatable.setViewportView(containerTable);
    }

    private void loadTableStaff() {
        StaffBLL staffBLL = new StaffBLL();
        dataTable = new DataTable(staffBLL.getData(), new String[] {"Mã nhân viên", "Họ tên", "Giới tính", "Ngày sinh", "Điện thoại", "Địa chỉ", "Email", "Ngày vào làm"}, e -> {});
        dataTable.setPreferredSize(new Dimension(1500, 700));
        containerTable.removeAll();
        containerTable.add(dataTable.getTableHeader(), BorderLayout.NORTH);
        containerTable.add(dataTable, BorderLayout.CENTER);

        Staff staff = staffBLL.findStaffsBy(Map.of("id", importNote.getStaff_id())).get(0);
        int index = staffBLL.getIndex(staff, "id", staffBLL.getStaffList());
        dataTable.setRowSelectionInterval(index, index);

        containerTable.repaint();
        containerTable.revalidate();
        scrollPaneDatatable.setViewportView(containerTable);
    }

    private void loadTableSupplier() {
        SupplierBLL staffBLL = new SupplierBLL();
        dataTable = new DataTable(staffBLL.getData(), new String[] {"Mã nhà cung cấp", "Tên nhà cung cấp", "Số điện thoại", "Địa chỉ", "Email"}, e -> {});
        dataTable.setPreferredSize(new Dimension(1500, 700));
        containerTable.removeAll();
        containerTable.add(dataTable.getTableHeader(), BorderLayout.NORTH);
        containerTable.add(dataTable, BorderLayout.CENTER);

        Supplier staff = staffBLL.findSuppliersBy(Map.of("id", importNote.getSupplier_id())).get(0);
        int index = staffBLL.getIndex(staff, "id", staffBLL.getSupplierList());
        dataTable.setRowSelectionInterval(index, index);

        containerTable.repaint();
        containerTable.revalidate();
        scrollPaneDatatable.setViewportView(containerTable);
    }
}
