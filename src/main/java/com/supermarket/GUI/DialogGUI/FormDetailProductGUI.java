package com.supermarket.GUI.DialogGUI;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.supermarket.BLL.BrandBLL;
import com.supermarket.BLL.CategoryBLL;
import com.supermarket.BLL.SupplierBLL;
import com.supermarket.DTO.Brand;
import com.supermarket.DTO.Category;
import com.supermarket.DTO.Product;
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

public class FormDetailProductGUI extends DialogForm {
    private Product product;
    private final BrandBLL brandBLL = new BrandBLL();
    private final CategoryBLL categoryBLL = new CategoryBLL();
    private DataTable dataTable;
    private RoundedPanel formDetail;
    private RoundedScrollPane scrollPaneFormDetail;
    private RoundedScrollPane scrollPaneDatatable;
    private java.util.List<JLabel> attributeProduct;
    private List<JTextField> jTextFieldProduct;
    private JLabel labelImageProduct;
    public FormDetailProductGUI(Product product) {
        super();
        this.product = product;
        super.setTitle("Quản lý sản phẩm");
        init();
        containerButton.setBackground(new Color(0xFFFFFF));
        setVisible(true);
    }

    private void init() {
        dataTable = new DataTable(new Object[][] {}, new String[] {}, e -> {});
        formDetail = new RoundedPanel();
        attributeProduct = new ArrayList<>();
        jTextFieldProduct = new ArrayList<>();
        scrollPaneDatatable = new RoundedScrollPane(containerTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneFormDetail = new RoundedScrollPane(formDetail, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        labelImageProduct = new JLabel();

        scrollPaneDatatable.setPreferredSize(new Dimension(600, 700));
        leftContent.add(scrollPaneDatatable, BorderLayout.CENTER);
        containerTable.add(dataTable);

        scrollPaneFormDetail.setPreferredSize(new Dimension(670, 610));
        rightContent.add(scrollPaneFormDetail, BorderLayout.NORTH);

        formDetail.setBackground(new Color(0xFFBDD2DB));
        formDetail.setLayout(new MigLayout("", "50[]20[]10", "20[]20[]"));

        for (String string : new String[] {"Hình sản phẩm:", "Mã sản phẩm:", "Tên sản phẩm:", "Mã thương hiệu:", "Mã thể loại:", "Đơn vị:", "Giá bán:", "Số lượng:", "Barcode:"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("FlatLaf.style", Font.PLAIN, 16)));
            attributeProduct.add(label);
            formDetail.add(label);

            JTextField textField = new JTextField();
            if (string.equals("Hình sản phẩm:")) {
                labelImageProduct.setIcon(new FlatSVGIcon(product.getImage()));
                formDetail.add(labelImageProduct, "wrap");
                continue;
            }
            if (string.equals("Mã sản phẩm:")) {
                textField.setText(String.valueOf(product.getId()));
            }
            if (string.equals("Tên sản phẩm:")) {
                textField.setText(String.valueOf(product.getName()));
            }
            if (string.equals("Mã thương hiệu:")) {
                textField.setText(String.valueOf(product.getBrand_id()));
                textField.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        loadTableBrand();
                    }
                });
            }
            if (string.equals("Mã thể loại:")) {
                textField.setText(String.valueOf(product.getCategory_id()));
                textField.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        loadTableCategory();
                    }
                });
            }
            if (string.equals("Đơn vị:")) {
                textField.setText(String.valueOf(product.getUnit()));
            }
            if (string.equals("Giá bán:")) {
                textField.setText(String.valueOf(product.getCost()));
            }
            if (string.equals("Số lượng:")) {
                textField.setText(String.valueOf(product.getQuantity()));
            }
            if (string.equals("Barcode:")) {
                textField.setText(String.valueOf(product.getBarcode()));
            }
            textField.setEditable(false);
            textField.setPreferredSize(new Dimension(400, 50));
            textField.setFont((new Font("FlatLaf.style", Font.PLAIN, 14)));
            jTextFieldProduct.add(textField);
            formDetail.add(textField, "wrap");
        }

    }

    private void loadTableCategory() {
        Object[][] categoryList = new Object[0][0];
        for (Category category : categoryBLL.getCategoryList()) {
            Object[] object = new Object[2];
            object[0] = category.getName();
            object[1] = category.getQuantity();
            categoryList = Arrays.copyOf(categoryList, categoryList.length + 1);
            categoryList[categoryList.length - 1] = object;
        }

        dataTable = new DataTable(categoryList, new String[] {"Tên thể loại", "Số lượng"}, e -> {});
        containerTable.removeAll();
        containerTable.add(dataTable.getTableHeader(), BorderLayout.NORTH);
        containerTable.add(dataTable, BorderLayout.CENTER);

        Category category = categoryBLL.findCategoriesBy(Map.of("id", product.getCategory_id())).get(0);
        int index = categoryBLL.getIndex(category, "id", categoryBLL.getCategoryList());
        dataTable.setRowSelectionInterval(index, index);

        containerTable.repaint();
        containerTable.revalidate();
        scrollPaneDatatable.setViewportView(containerTable);
    }

    private void loadTableBrand() {
        Object[][] brandList = new Object[0][0];
        SupplierBLL supplierBLL = new SupplierBLL();
        for (Brand brand : brandBLL.getBrandList()) {
            Object[] object = new Object[2];
            object[0] = brand.getName();
            object[1] = supplierBLL.findSuppliersBy(Map.of("id", brand.getSupplier_id())).get(0).getName();
            brandList = Arrays.copyOf(brandList, brandList.length + 1);
            brandList[brandList.length - 1] = object;
        }

        dataTable = new DataTable(brandList, new String[] {"Tên thương hiệu", "Tên nhà cung cấp"}, e -> {});
        containerTable.removeAll();
        containerTable.add(dataTable.getTableHeader(), BorderLayout.NORTH);
        containerTable.add(dataTable, BorderLayout.CENTER);

        Brand brand = brandBLL.findBrandsBy(Map.of("id", product.getBrand_id())).get(0);
        int index = brandBLL.getIndex(brand, "id", brandBLL.getBrandList());
        dataTable.setRowSelectionInterval(index, index);

        containerTable.repaint();
        containerTable.revalidate();
        scrollPaneDatatable.setViewportView(containerTable);
    }
}
