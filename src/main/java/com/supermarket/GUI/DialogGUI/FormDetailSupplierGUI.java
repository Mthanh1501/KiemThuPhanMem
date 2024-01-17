package com.supermarket.GUI.DialogGUI;

import com.supermarket.BLL.BrandBLL;
import com.supermarket.DTO.Brand;
import com.supermarket.DTO.Supplier;
import com.supermarket.GUI.components.DataTable;
import com.supermarket.GUI.components.RoundedPanel;
import com.supermarket.GUI.components.RoundedScrollPane;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FormDetailSupplierGUI extends DialogForm{
    private final BrandBLL brandBLL = new BrandBLL();
    private DataTable dataTable;
    private RoundedPanel formDetail;
    private RoundedScrollPane scrollPaneFormDetail;
    private RoundedScrollPane scrollPaneDatatable;
    private List<JLabel> attributesupplier;
    private List<JTextField> jTextFieldsupplier;
    private Supplier supplier;

    public FormDetailSupplierGUI(Supplier supplier) {
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
        jTextFieldsupplier = new ArrayList<>();
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

            textField.setEditable(false);
            textField.setPreferredSize(new Dimension(400, 50));
            textField.setFont((new Font("FlatLaf.style", Font.PLAIN, 14)));
            jTextFieldsupplier.add(textField);
            formDetail.add(textField, "wrap");
            loadTableBrand();
        }
    }

    private void loadTableBrand() {
        Object[][] objects = new Object[0][0];
        for (Brand brand : brandBLL.findBrandsBy(Map.of("supplier_id", supplier.getId()))) {
            Object[] newObject = new Object[2];
            newObject[0] = brand.getId();
            newObject[1] = brand.getName();
            objects = Arrays.copyOf(objects, objects.length + 1);
            objects[objects.length-1] = newObject;
        }

        dataTable = new DataTable(objects, new String[] {"Mã thương hiệu", "Tên thương hiệu"}, e -> {});
        dataTable.setPreferredSize(new Dimension(500, 1500));
        containerTable.removeAll();
        containerTable.add(dataTable.getTableHeader(), BorderLayout.NORTH);
        containerTable.add(dataTable, BorderLayout.CENTER);

        containerTable.repaint();
        containerTable.revalidate();
        scrollPaneDatatable.setViewportView(containerTable);
    }
}
