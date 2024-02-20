package com.supermarket.GUI.DialogGUI;

import com.supermarket.BLL.ExportBLL;
import com.supermarket.BLL.ProductBLL;
import com.supermarket.BLL.ShipmentBLL;
import com.supermarket.DTO.Export;
import com.supermarket.DTO.Product;
import com.supermarket.DTO.Shipment;
import com.supermarket.GUI.HomeGUI;
import com.supermarket.GUI.components.DataTable;
import com.supermarket.GUI.components.RoundedPanel;
import com.supermarket.GUI.components.RoundedScrollPane;
import com.supermarket.utils.Date;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.*;

public class FormAddExportGUI extends DialogForm{
    private Export newExport;
    private final ExportBLL exportBLL = new ExportBLL();
    private final ProductBLL productBLL = new ProductBLL();
    private final ShipmentBLL shipmentBLL = new ShipmentBLL();
    private DataTable dataTable;
    private RoundedPanel formDetail;
    private RoundedScrollPane scrollPaneDatatable;
    private RoundedScrollPane scrollPaneExportDetail;
    private List<JLabel> attributeProduct;
    private List<JTextField> jTextFieldExport;
    private JComboBox cbbReason;
    private JButton buttonCancel;
    private JButton buttonAdd;
    private boolean flag;
    private static List<Integer> shipmentIDInExport;

    public FormAddExportGUI() {
        super();
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
        cbbReason = new JComboBox(new String[] {"Bán", "Huỷ"});
        scrollPaneDatatable = new RoundedScrollPane(containerTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneExportDetail = new RoundedScrollPane(formDetail, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        buttonCancel = new JButton("Huỷ");
        buttonAdd = new JButton("Thêm");
        shipmentIDInExport = new ArrayList<>();

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
                textField.setText(String.valueOf(exportBLL.getAutoID(exportBLL.searchExport())));
                textField.setEnabled(false);
                textField.setEditable(false);
            }
            if (string.equals("Mã nhân viên:")) {
                textField.setText(String.valueOf(HomeGUI.account.getStaffID()));
                textField.setEnabled(false);
            }
            if (string.equals("Ngày xuất:")) {
                textField.setText(Date.dateNow());
                textField.setEnabled(false);
            }
            if (string.equals("Tổng tiền:")) {
                textField.setEnabled(false);
            }
            if (string.equals("Lý do:")) {
                cbbReason.setPreferredSize(new Dimension(400, 50));
                formDetail.add(cbbReason, "wrap");
                continue;
            }
            cbbReason.setPreferredSize(new Dimension(400, 50));
            textField.setPreferredSize(new Dimension(400, 50));
            textField.setFont((new Font("FlatLaf.style", Font.PLAIN, 14)));
            jTextFieldExport.add(textField);
            formDetail.add(textField, "wrap");
        }

        buttonCancel.setPreferredSize(new Dimension(100,40));
        buttonCancel.setFont(new Font("FlatLaf.style", Font.BOLD, 15));
        buttonCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonCancel.setVisible(false);
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

        buttonAdd.setPreferredSize(new Dimension(100,40));
        buttonAdd.setFont(new Font("FlatLaf.style", Font.BOLD, 15));
        buttonAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                addExport();
            }
        });
        containerButton.add(buttonAdd);

    }

    private void refresh() {
        jTextFieldExport.get(0).setEnabled(true);
        jTextFieldExport.get(0).setText(String.valueOf(exportBLL.getAutoID(exportBLL.searchExport())));
        jTextFieldExport.get(0).setEnabled(false);
        jTextFieldExport.get(1).setEnabled(true);
        jTextFieldExport.get(1).setText("");
        jTextFieldExport.get(1).setEnabled(false);
        jTextFieldExport.get(2).setEnabled(true);
        jTextFieldExport.get(2).setText(Date.dateNow());
        jTextFieldExport.get(2).setEnabled(false);
        jTextFieldExport.get(3).setEnabled(true);
        jTextFieldExport.get(3).setText("");
        jTextFieldExport.get(3).setEnabled(false);
        jTextFieldExport.get(4).setEnabled(true);
        jTextFieldExport.get(4).setText("");
        jTextFieldExport.get(4).setEnabled(false);
        shipmentIDInExport.removeAll(shipmentIDInExport);
        containerTable.removeAll();
        containerTable.repaint();
        containerTable.revalidate();
    }

    private void addExport() {
        for (int i = 0; i < jTextFieldExport.size(); i++) {
            if (i != 3 && jTextFieldExport.get(i).getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng xuất đầy đủ thông tin phiếu xuất.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        if (shipmentIDInExport.isEmpty()) {
            int id = Integer.parseInt(jTextFieldExport.get(0).getText());
            int staffID = Integer.parseInt(jTextFieldExport.get(1).getText());
            Date receivedDate = null;
            try {
                receivedDate = Date.parseDate(jTextFieldExport.get(2).getText());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            String reason = Objects.requireNonNull(cbbReason.getSelectedItem()).toString();
            newExport = new Export(id, staffID, receivedDate, 0, reason, false);
            loadTableShipment(shipmentBLL.getShipmentList());
            JOptionPane.showMessageDialog(this, "Vui lòng chọn lô hàng xuất", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            String[] options = new String[]{"Huỷ", "Xác nhận"};
            int choice = JOptionPane.showOptionDialog(null, "Xác nhận xuất các lô hàng đã chọn?",
                "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            if (choice == 1) {
                new FormAddExport_DetailGUI(newExport ,shipmentIDInExport);
                dispose();
            }
        }

    }
    private void loadTableShipment(List<Shipment> shipments) {
        Object[][] shipmentList = new Object[0][0];
        dataTable = new DataTable(new Object[][]{}, new String[] {"Mã lô hàng", "Tên sản phẩm", "Tồn kho", ""}, e -> addToDetail(), 3);
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        model.setRowCount(0);
        for (Shipment shipment : shipments) {
            Object[] objects1 = new Object[4];
            objects1[0] = shipment.getId();
            Product product = productBLL.findProductsBy(Map.of("id", shipment.getProduct_id())).get(0);
            objects1[1] = product.getName();
            objects1[2] = shipment.getRemain();
            if (shipmentIDInExport.contains(shipment.getId())) {
                objects1[3] = true;
            } else {
                objects1[3] = null;
            }
            shipmentList = Arrays.copyOf(shipmentList, shipmentList.length + 1);
            shipmentList[shipmentList.length - 1] = objects1;
        }

        for (Object[] object : shipmentList) {
            model.addRow(object);
        }
        containerTable.removeAll();
//        dataTable.setPreferredSize(new Dimension(1300, 700));
        containerTable.add(dataTable.getTableHeader(), BorderLayout.NORTH);
        containerTable.add(dataTable, BorderLayout.CENTER);
        containerTable.repaint();
        containerTable.revalidate();
        scrollPaneDatatable.setViewportView(containerTable);
    }

    private void addToDetail() {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        Object[] rowData = model.getDataVector().elementAt(dataTable.getSelectedRow()).toArray();
        String[] data = new String[rowData.length];
        for (int i = 0; i < rowData.length; i++) {
            if (rowData[i] != null)
                data[i] = rowData[i].toString();
        }
        String[] shipment = String.join(" | ", data).split(" \\| ");
        Shipment shipment1 = shipmentBLL.findShipmentsBy(Map.of("id", Integer.parseInt(shipment[0]))).get(0);
        if (data[3] == null) {
            shipmentIDInExport.add(shipment1.getId());
            model.setValueAt(true, dataTable.getSelectedRow(), 3);
        } else {
            shipmentIDInExport.remove((Object)shipment1.getId());
            model.setValueAt(null, dataTable.getSelectedRow(), 3);
        }
    }
}
