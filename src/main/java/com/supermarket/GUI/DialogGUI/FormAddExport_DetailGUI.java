package com.supermarket.GUI.DialogGUI;

import com.supermarket.BLL.ExportBLL;
import com.supermarket.BLL.Export_detailBLL;
import com.supermarket.BLL.ProductBLL;
import com.supermarket.BLL.ShipmentBLL;
import com.supermarket.DTO.Export;
import com.supermarket.DTO.Export_detail;
import com.supermarket.DTO.Product;
import com.supermarket.DTO.Shipment;
import com.supermarket.GUI.ExportGUI;
import com.supermarket.GUI.components.DataTable;
import com.supermarket.GUI.components.RoundedPanel;
import com.supermarket.GUI.components.RoundedScrollPane;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.*;

public class FormAddExport_DetailGUI extends DialogForm{
    private List<Integer> shipmentIDInExport;
    private List<Export_detail> exportDetails;
    private Export newExport;
    private final ExportBLL exportBLL = new ExportBLL();
    private final Export_detailBLL exportDetailBLL = new Export_detailBLL();
    private final ProductBLL productBLL = new ProductBLL();
    private DataTable dataTable;
    private RoundedPanel formDetail;
    private RoundedScrollPane scrollPaneDatatable;
    private RoundedScrollPane scrollPaneExportDetail;
    private List<JTextField> jTextFieldExport;
    private JButton buttonCancel;
    private JButton buttonAdd;
    private JButton buttonConfirm;
    public FormAddExport_DetailGUI(Export newExport, List<Integer> shipmentIDInExport) {
        super();
        super.setTitle("Quản lý phiếu xuất");
        this.newExport = newExport;
        this.shipmentIDInExport = shipmentIDInExport;
        init();
        containerButton.setBackground(new Color(0xFFFFFF));
        setVisible(true);
    }

    private void init() {
        exportDetails = new ArrayList<>();
        dataTable = new DataTable(new Object[][] {}, new String[] {}, e -> {});
        formDetail = new RoundedPanel();
        jTextFieldExport = new ArrayList<>();
        scrollPaneDatatable = new RoundedScrollPane(containerTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneExportDetail = new RoundedScrollPane(formDetail, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        buttonCancel = new JButton("Huỷ");
        buttonAdd = new JButton("Thêm");
        buttonConfirm = new JButton("Xác nhận");

        scrollPaneDatatable.setPreferredSize(new Dimension(600, 700));
        leftContent.add(scrollPaneDatatable, BorderLayout.CENTER);
        containerTable.add(dataTable);

        scrollPaneExportDetail.setPreferredSize(new Dimension(670, 610));
        rightContent.add(scrollPaneExportDetail, BorderLayout.NORTH);

        formDetail.setBackground(new Color(0xFFBDD2DB));
        formDetail.setLayout(new MigLayout("", "50[]20[]10", "20[]20[]"));

        for (String string : new String[]{"Mã lô hàng:", "Số lượng xuất:", "Tổng giá trị:"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("FlatLaf.style", Font.PLAIN, 16)));
            formDetail.add(label);

            JTextField textField = new JTextField();
            if (string.equals("Mã lô hàng:")) {
                textField.setEnabled(false);
            }
            if (string.equals("Số lượng xuất:")) {
                textField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        char c = e.getKeyChar();
                        if (!Character.isDigit(c) && c != KeyEvent.VK_PERIOD) {
                            e.consume();
                        }
                    }
                });
            }
            if (string.equals("Tổng giá trị:")) {
                textField.setEnabled(false);
            }
            textField.setPreferredSize(new Dimension(400, 50));
            textField.setFont((new Font("FlatLaf.style", Font.PLAIN, 14)));
            jTextFieldExport.add(textField);
            formDetail.add(textField, "wrap");
        }

        for (Integer i : shipmentIDInExport) {
            Export_detail exportDetail = new Export_detail();
            exportDetail.setExport_id(newExport.getId());
            exportDetail.setShipment_id(i);
            exportDetails.add(exportDetail);
        }

        buttonConfirm.setPreferredSize(new Dimension(100,40));
        buttonConfirm.setFont(new Font("FlatLaf.style", Font.BOLD, 15));
        buttonConfirm.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonConfirm.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                comfirmExport_detail();
            }
        });
        formDetail.add(buttonConfirm);

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

        buttonAdd.setPreferredSize(new Dimension(100,40));
        buttonAdd.setFont(new Font("FlatLaf.style", Font.BOLD, 15));
        buttonAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                addExport_detail();
            }
        });
        containerButton.add(buttonAdd);

        loadTableShipment();

    }

    private void comfirmExport_detail() {
        ShipmentBLL shipmentBLL = new ShipmentBLL();
        for (Export_detail exportDetail : exportDetails) {
            if (exportDetail.getShipment_id() == Integer.parseInt(jTextFieldExport.get(0).getText())) {
                try {
                    double quantity = Double.parseDouble(jTextFieldExport.get(1).getText());
                    if (quantity > shipmentBLL.findShipmentsBy(Map.of("id", exportDetail.getShipment_id())).get(0).getRemain()) {
                        JOptionPane.showMessageDialog(null, "Số lượng tồn kho không đủ.",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    exportDetail.setQuantity(quantity);
                    Shipment shipment = shipmentBLL.findShipmentsBy(Map.of("id", exportDetail.getShipment_id())).get(0);
                    double total = shipment.getUnit_price() * exportDetail.getQuantity();
                    exportDetail.setTotal(total);
                    jTextFieldExport.get(2).setText(String.valueOf(total));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private void loadTableShipment() {
        ShipmentBLL shipmentBLL = new ShipmentBLL();
        Object[][] shipmentList = new Object[0][0];
        for (Shipment shipment : shipmentBLL.getShipmentList()) {
            if (shipmentIDInExport.contains(shipment.getId())) {
                Object[] objects1 = new Object[3];
                objects1[0] = shipment.getId();
                Product product = productBLL.findProductsBy(Map.of("id", shipment.getProduct_id())).get(0);
                objects1[1] = product.getName();
                objects1[2] = shipment.getRemain();
                shipmentList = Arrays.copyOf(shipmentList, shipmentList.length + 1);
                shipmentList[shipmentList.length - 1] = objects1;
            }
        }
        dataTable = new DataTable(shipmentList, new String[] {"Mã lô hàng", "Tên sản phẩm", "Tồn kho"}, e -> selectRowTable());
        containerTable.removeAll();
        containerTable.add(dataTable.getTableHeader(), BorderLayout.NORTH);
        containerTable.add(dataTable, BorderLayout.CENTER);

        if (!jTextFieldExport.get(0).getText().isEmpty()) {
            Product product = productBLL.findProductsBy(Map.of("id", Integer.parseInt(jTextFieldExport.get(0).getText()))).get(0);
            int index = productBLL.getIndex(product, "id", productBLL.getProductList());
            dataTable.setRowSelectionInterval(index, index);
        }
        containerTable.repaint();
        containerTable.revalidate();
        scrollPaneDatatable.setViewportView(containerTable);
    }
    private void addExport_detail() {
        ShipmentBLL shipmentBLL = new ShipmentBLL();
        for (Export_detail exportDetail : exportDetails) {
            if (exportDetail.getQuantity() == 0.0) {
                JOptionPane.showMessageDialog(null, "Vui lòng xuất đầy đủ thông tin lô hàng.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        String[] options = new String[]{"Huỷ", "Xác nhận"};
        int choice = JOptionPane.showOptionDialog(null, "Xác nhận xuất các lô hàng?",
            "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (choice == 1) {
            double total = 0;
            for (Export_detail exportDetail : exportDetails) {
                total += exportDetail.getTotal();
            }
            newExport.setTotal(total);
            if (exportBLL.addExport(newExport)) {
                JOptionPane.showMessageDialog(null, "Thêm phiếu xuất thành công!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                for (Export_detail exportDetail : exportDetails) {
                    exportDetailBLL.addExport_detail(exportDetail);
                }
                for (Export_detail exportDetail : exportDetails) {
                    Shipment shipment = shipmentBLL.findShipmentsBy(Map.of("id", exportDetail.getShipment_id())).get(0);
                    shipment.setRemain(shipment.getRemain() - exportDetail.getQuantity());
                    shipmentBLL.updateShipment(shipment);
                }
                if (Objects.equals(newExport.getReason(), "Bán")) {
                    for (Export_detail exportDetail : exportDetails) {
                        Shipment shipment = shipmentBLL.findShipmentsBy(Map.of("id", exportDetail.getShipment_id())).get(0);
                        Product product = productBLL.findProductsBy(Map.of("id", shipment.getProduct_id())).get(0);
                        product.setQuantity(product.getQuantity() + exportDetail.getQuantity());
                        productBLL.updateProduct(product);
                    }
                }
                ExportGUI.loadDataTable(exportBLL.getExportList());
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Thêm phiếu xuất không thành công!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refresh() {
        jTextFieldExport.get(1).setText("");

    }

    public void selectRowTable() {
        String id;
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        int indexRow = dataTable.getSelectedRow();
        id = model.getDataVector().elementAt(indexRow).get(0).toString();
        jTextFieldExport.get(1).setText("");
        for (Export_detail exportDetail : exportDetails) {
            if (exportDetail.getShipment_id() == Integer.parseInt(id)) {
                jTextFieldExport.get(0).setText(String.valueOf(exportDetail.getShipment_id()));
                if (exportDetail.getQuantity() != 0.0) {
                    jTextFieldExport.get(1).setText(String.valueOf(exportDetail.getQuantity()));
                } else {
                    jTextFieldExport.get(1).setText("");
                }
                if (exportDetail.getTotal() != 0.0) {
                    jTextFieldExport.get(2).setText(String.valueOf(exportDetail.getTotal()));

                } else {
                    jTextFieldExport.get(2).setText("");
                }
            }
        }

    }
}
