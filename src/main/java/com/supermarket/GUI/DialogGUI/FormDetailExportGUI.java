package com.supermarket.GUI.DialogGUI;

import com.supermarket.BLL.Export_detailBLL;
import com.supermarket.BLL.ProductBLL;
import com.supermarket.BLL.ShipmentBLL;
import com.supermarket.BLL.StaffBLL;
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

public class FormDetailExportGUI extends DialogForm{
    private Export exportNote;
    private DataTable dataTable;
    private RoundedPanel formDetail;
    private RoundedScrollPane scrollPaneDatatable;
    private RoundedScrollPane scrollPaneExportDetail;
    private List<JLabel> attributeProduct;
    private List<JTextField> jTextFieldExport;
    public FormDetailExportGUI(Export exportNote) {
        super();
        this.exportNote = exportNote;
        super.setTitle("Quản lý phiếu xuất");
        init();
        containerButton.setBackground(new Color(0xFFFFFF));
        setVisible(true);
    }

    private void init() {
        dataTable = new DataTable(new Object[][] {}, new String[] {}, e -> {});
        formDetail = new RoundedPanel();
        attributeProduct = new ArrayList<>();
        jTextFieldExport = new ArrayList<>();
        scrollPaneDatatable = new RoundedScrollPane(containerTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneExportDetail = new RoundedScrollPane(formDetail, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scrollPaneDatatable.setPreferredSize(new Dimension(600, 700));
        leftContent.add(scrollPaneDatatable, BorderLayout.CENTER);
        containerTable.add(dataTable);

        scrollPaneExportDetail.setPreferredSize(new Dimension(670, 610));
        rightContent.add(scrollPaneExportDetail, BorderLayout.NORTH);

        formDetail.setBackground(new Color(0xFFBDD2DB));
        formDetail.setLayout(new MigLayout("", "50[]20[]10", "20[]20[]"));

        for (String string : new String[] {"Mã phiếu xuất:", "Mã nhân viên:", "Ngày xuất:", "Tổng tiền:", "Lý do:"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("FlatLaf.style", Font.PLAIN, 16)));
            attributeProduct.add(label);
            formDetail.add(label);

            JTextField textField = new JTextField();
            if (string.equals("Mã phiếu xuất:")) {
                textField.setText(String.valueOf(exportNote.getId()));
                textField.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        loadTableExport_Detail();
                    }
                });
            }
            if (string.equals("Mã nhân viên:")) {
                textField.setText(String.valueOf(exportNote.getStaff_id()));
                textField.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        loadTableStaff();
                    }
                });
            }
            if (string.equals("Ngày xuất:")) {
                textField.setText(String.valueOf(exportNote.getInvoice_date()));
                textField.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        loadTableExport_Detail();
                    }
                });
            }
            if (string.equals("Tổng tiền:")) {
                textField.setText(String.valueOf(exportNote.getTotal()));
                textField.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        loadTableExport_Detail();
                    }
                });
            }
            if (string.equals("Lý do:")) {
                textField.setText(String.valueOf(exportNote.getReason()));
                textField.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        loadTableExport_Detail();
                    }
                });
            }
            textField.setEditable(false);
            textField.setPreferredSize(new Dimension(400, 50));
            textField.setFont((new Font("FlatLaf.style", Font.PLAIN, 14)));
            jTextFieldExport.add(textField);
            formDetail.add(textField, "wrap");
        }
        loadTableExport_Detail();
    }

    private void loadTableExport_Detail() {
        Object[][] exportNoteDetailList = new Object[0][0];
        Export_detailBLL export_detailBLL = new Export_detailBLL();
        ShipmentBLL shipmentBLL = new ShipmentBLL();
        ProductBLL productBLL = new ProductBLL();
        for (Export_detail exportDetail : export_detailBLL.getExportList()) {
            if (exportDetail.getExport_id() == exportNote.getId()) {
                Object[] object = new Object[3];
                Shipment shipment = shipmentBLL.findShipmentsBy(Map.of("id", exportDetail.getShipment_id())).get(0);
                Product product = productBLL.findProductsBy(Map.of("id", shipment.getProduct_id())).get(0);
                object[0] = product.getName();
                object[1] = exportDetail.getQuantity();
                object[2] = exportDetail.getTotal();
                exportNoteDetailList = Arrays.copyOf(exportNoteDetailList, exportNoteDetailList.length + 1);
                exportNoteDetailList[exportNoteDetailList.length - 1] = object;
            }

        }

        dataTable = new DataTable(exportNoteDetailList, new String[] {"Tên sản phẩm", "Số lượng", "Tổng giá trị"}, e -> {});
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

        Staff staff = staffBLL.findStaffsBy(Map.of("id", exportNote.getStaff_id())).get(0);
        int index = staffBLL.getIndex(staff, "id", staffBLL.getStaffList());
        dataTable.setRowSelectionInterval(index, index);

        containerTable.repaint();
        containerTable.revalidate();
        scrollPaneDatatable.setViewportView(containerTable);
    }

}
