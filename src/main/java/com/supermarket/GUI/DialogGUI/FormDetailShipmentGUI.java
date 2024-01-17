package com.supermarket.GUI.DialogGUI;

import com.supermarket.BLL.ImportBLL;
import com.supermarket.BLL.ProductBLL;
import com.supermarket.DTO.Import;
import com.supermarket.DTO.Product;
import com.supermarket.DTO.Shipment;
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

public class FormDetailShipmentGUI extends DialogForm {
    private Shipment shipment;
    private final ProductBLL productBLL = new ProductBLL();
    private final ImportBLL importBLL = new ImportBLL();
    private DataTable dataTable;
    private RoundedPanel formDetail;
    private RoundedScrollPane scrollPaneFormDetail;
    private RoundedScrollPane scrollPaneDatatable;
    private java.util.List<JLabel> attributeShipment;
    private List<JTextField> jTextFieldShipment;
    public FormDetailShipmentGUI(Shipment shipment) {
        super();
        this.shipment = shipment;
        super.setTitle("Quản lý lô hàng");
        init();
        containerButton.setBackground(new Color(0xFFFFFF));
        setVisible(true);
    }

    private void init() {
        dataTable = new DataTable(new Object[][] {}, new String[] {}, e -> {});
        formDetail = new RoundedPanel();
        attributeShipment = new ArrayList<>();
        jTextFieldShipment = new ArrayList<>();
        scrollPaneDatatable = new RoundedScrollPane(containerTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneFormDetail = new RoundedScrollPane(formDetail, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);


        scrollPaneDatatable.setPreferredSize(new Dimension(600, 700));
        leftContent.add(scrollPaneDatatable, BorderLayout.CENTER);
        containerTable.add(dataTable);

        scrollPaneFormDetail.setPreferredSize(new Dimension(670, 610));
        rightContent.add(scrollPaneFormDetail, BorderLayout.NORTH);

        formDetail.setBackground(new Color(0xFFBDD2DB));
        formDetail.setLayout(new MigLayout("", "50[]20[]10", "20[]20[]"));

        for (String string : new String[]{"Mã lô hàng:", "Mã sản phẩm:", "Đơn giá:", "Số lượng nhập:", "Tồn kho:", "Ngày sản xuất:", "Ngày hết hạn:", "Mã SKU:", "Mã phiếu nhập:"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("FlatLaf.style", Font.PLAIN, 16)));
            attributeShipment.add(label);
            formDetail.add(label);

            JTextField textField = new JTextField();
            if (string.equals("Mã lô hàng:")) {
                textField.setText(String.valueOf(shipment.getId()));
            }
            if (string.equals("Mã sản phẩm:")) {
                textField.setText(String.valueOf(shipment.getProduct_id()));
                textField.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        loadTableProduct();
                    }
                });
            }
            if (string.equals("Đơn giá:")) {
                textField.setText(String.valueOf(shipment.getUnit_price()));
            }
            if (string.equals("Số lượng nhập:")) {
                textField.setText(String.valueOf(shipment.getQuantity()));
            }
            if (string.equals("Tồn kho:")) {
                textField.setText(String.valueOf(shipment.getRemain()));
            }
            if (string.equals("Ngày sản xuất:")) {
                textField.setText(String.valueOf(shipment.getMfg()));
            }
            if (string.equals("Ngày hết hạn:")) {
                textField.setText(String.valueOf(shipment.getExp()));
            }
            if (string.equals("Mã SKU:")) {
                textField.setText(String.valueOf(shipment.getSku()));
            }
            if (string.equals("Mã phiếu nhập:")) {
                textField.setText(String.valueOf(shipment.getImport_id()));
                textField.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        loadTableImport();
                    }
                });
            }
            textField.setEditable(false);
            textField.setPreferredSize(new Dimension(400, 50));
            textField.setFont((new Font("FlatLaf.style", Font.PLAIN, 14)));
            jTextFieldShipment.add(textField);
            formDetail.add(textField, "wrap");
        }
    }

    private void loadTableProduct() {
        Object[][] productList = new Object[0][0];
        for (Product product : productBLL.getProductList()) {
            Object[] object = new Object[1];
            object[0] = product.getName();
            productList = Arrays.copyOf(productList, productList.length + 1);
            productList[productList.length - 1] = object;
        }

        dataTable = new DataTable(productList, new String[] {"Tên sản phẩm"}, e -> {});
        containerTable.removeAll();
        containerTable.add(dataTable.getTableHeader(), BorderLayout.NORTH);
        containerTable.add(dataTable, BorderLayout.CENTER);

        Product product = productBLL.findProductsBy(Map.of("id", shipment.getProduct_id())).get(0);
        int index = productBLL.getIndex(product, "id", productBLL.getProductList());
        dataTable.setRowSelectionInterval(index, index);

        containerTable.repaint();
        containerTable.revalidate();
        scrollPaneDatatable.setViewportView(containerTable);
    }

    private void loadTableImport() {
        Object[][] importList = new Object[0][0];
        for (Import import_note : importBLL.getImportList()) {
            Object[] object = new Object[3];
            object[0] = import_note.getId();
            object[1] = import_note.getReceived_date();
            object[2] = import_note.getTotal();
            importList = Arrays.copyOf(importList, importList.length + 1);
            importList[importList.length - 1] = object;
        }

        dataTable = new DataTable(importList, new String[] {"Mã phiếu nhập", "Ngày nhập", "Tổng tiền"}, e -> {});
        containerTable.removeAll();
        containerTable.add(dataTable.getTableHeader(), BorderLayout.NORTH);
        containerTable.add(dataTable, BorderLayout.CENTER);

        Import import_note = importBLL.findImportBy(Map.of("id", shipment.getImport_id())).get(0);
        int index = importBLL.getIndex(import_note, "id", importBLL.getImportList());
        dataTable.setRowSelectionInterval(index, index);

        containerTable.repaint();
        containerTable.revalidate();
        scrollPaneDatatable.setViewportView(containerTable);
    }
}
