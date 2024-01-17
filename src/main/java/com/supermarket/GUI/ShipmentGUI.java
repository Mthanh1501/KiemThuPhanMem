package com.supermarket.GUI;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.supermarket.BLL.ImportBLL;
import com.supermarket.BLL.ProductBLL;
import com.supermarket.BLL.ShipmentBLL;
import com.supermarket.DTO.Function;
import com.supermarket.DTO.Product;
import com.supermarket.DTO.Shipment;
import com.supermarket.GUI.DialogGUI.FormDetailShipmentGUI;
import com.supermarket.GUI.components.DataTable;
import com.supermarket.GUI.components.Layout1;
import com.supermarket.GUI.components.RoundedScrollPane;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ShipmentGUI extends Layout1 {
    private ProductBLL productBLL = new ProductBLL();
    private ImportBLL importBLL = new ImportBLL();
    private ShipmentBLL shipmentBLL = new ShipmentBLL();
    private JLabel iconDetail;
    private static DataTable dataTable;
    private RoundedScrollPane scrollPane;
    private JComboBox cbbAttributeShipment;
    private JComboBox cbbProduct;
    private JComboBox cbbDate;
    private Object[][] shipmentList;
    public ShipmentGUI (List<Function> functions) {
        super();
        init(functions);
    }

    private void init(List<Function> functions) {
        shipmentList = new Object[0][0];
        dataTable = new DataTable(new Object[][] {},
            new String[] {"Mã lô hàng", "Tên sản phẩm", "Đơn giá", "Số lượng nhập", "Tồn kho", "Ngày sản xuất", "Ngày hết hạn", "Ngày nhập hàng"}, e -> {});
        scrollPane = new RoundedScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        iconDetail = new JLabel();
        cbbAttributeShipment = new JComboBox(new String[] {"Tên sản phẩm", "Ngày hết hạn", "Sắp hết hàng"});
        cbbProduct = new JComboBox<>();
        cbbDate = new JComboBox<>(new String[] {"Hết hạn", "Sắp hết hạn"});

        if (functions.stream().anyMatch(f -> f.getName().equals("Chi tiết"))) {
            iconDetail.setIcon(new FlatSVGIcon("icon/detail.svg"));
            iconDetail.setCursor(new Cursor(Cursor.HAND_CURSOR));
            iconDetail.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    showDetailShipment();
                }
            });
            leftMenu.add(iconDetail);
        }

        cbbAttributeShipment.setPreferredSize(new Dimension(130, 30));
        cbbAttributeShipment.addActionListener(e -> selectSearchFilter());
        rightMenu.add(cbbAttributeShipment);

        for (Product product : productBLL.getProductList()) {
            cbbProduct.addItem(product.getName());
        }
        rightMenu.add(cbbProduct);
        cbbProduct.addActionListener(e -> searchByProduct());

        rightMenu.add(cbbDate);
        cbbDate.setVisible(false);
        cbbDate.addActionListener(e -> searchByDate());

        bottom.add(scrollPane, BorderLayout.CENTER);

        loadDataTable(shipmentBLL.getShipmentList());
    }

    private void loadDataTable(List<Shipment> shipments) {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        ProductBLL productBLL = new ProductBLL();
        ImportBLL importBLL = new ImportBLL();
        shipmentList = new Object[0][0];
        for (Shipment shipment : shipments) {
            Object[] object = new Object[8];
            object[0] = shipment.getId();
            object[1] = productBLL.findProductsBy(Map.of("id", shipment.getProduct_id())).get(0).getName();
            object[2] = shipment.getUnit_price();
            object[3] = shipment.getQuantity();
            object[4] = shipment.getRemain();
            object[5] = shipment.getMfg();
            object[6] = shipment.getExp();
            object[7] = importBLL.findImportBy(Map.of("id", shipment.getImport_id())).get(0).getReceived_date();
            shipmentList = Arrays.copyOf(shipmentList, shipmentList.length + 1);
            shipmentList[shipmentList.length - 1] = object;
        }

        model.setRowCount(0);
        for (Object[] object : shipmentList) {
            model.addRow(object);
        }
    }

    private void searchByProduct() {
        for (Product product : productBLL.getProductList()) {
            if (product.getName().equals(cbbProduct.getSelectedItem())) {
                loadDataTable(shipmentBLL.findShipmentsBy(Map.of("product_id", product.getId())));
                return;
            }
        }
    }

    private void searchByDate() {
        if (Objects.equals(cbbDate.getSelectedItem(), "Hết hạn")) {
            loadDataTable(shipmentBLL.searchShipments("deleted = 0", "`shipment`.exp < CURRENT_DATE()"));
        }
        if (Objects.equals(cbbDate.getSelectedItem(), "Sắp hết hạn")) {
            loadDataTable(shipmentBLL.searchShipments("deleted = 0", "MONTH(`shipment`.exp) - MONTH(CURRENT_DATE()) = 1 AND YEAR(`shipment`.exp) = YEAR(CURRENT_DATE())"));
        }
    }

    private void selectSearchFilter() {
        if (Objects.requireNonNull(cbbAttributeShipment.getSelectedItem()).toString().contains("Tên sản phẩm")) {
            cbbDate.setVisible(false);
            cbbProduct.setSelectedIndex(0);
            cbbProduct.setVisible(true);
            searchByProduct();
        } else if (Objects.requireNonNull(cbbAttributeShipment.getSelectedItem()).toString().contains("Ngày hết hạn")) {
            cbbProduct.setVisible(false);
            cbbDate.setSelectedIndex(0);
            cbbDate.setVisible(true);
            searchByDate();
        } else {
            cbbDate.setVisible(false);
            cbbProduct.setVisible(false);
            searchByQuantity();
        }
    }

    private void searchByQuantity() {
        loadDataTable(shipmentBLL.searchShipments("deleted = 0", "`shipment`.remain > 0 AND `shipment`.remain <= 20"));
    }

    private void showDetailShipment() {
        shipmentBLL = new ShipmentBLL();
        if (dataTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn lô hàng cần xem chi tiết.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        new FormDetailShipmentGUI(shipmentBLL.findShipmentsBy(Map.of("id", Integer.parseInt(model.getValueAt(dataTable.getSelectedRow(), 0).toString()))).get(0));
    }
}
