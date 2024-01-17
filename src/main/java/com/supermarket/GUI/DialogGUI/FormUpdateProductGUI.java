package com.supermarket.GUI.DialogGUI;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.supermarket.BLL.BrandBLL;
import com.supermarket.BLL.CategoryBLL;
import com.supermarket.BLL.ProductBLL;
import com.supermarket.BLL.SupplierBLL;
import com.supermarket.DTO.Brand;
import com.supermarket.DTO.Category;
import com.supermarket.DTO.Product;
import com.supermarket.GUI.ProductGUI;
import com.supermarket.GUI.components.DataTable;
import com.supermarket.GUI.components.RoundedPanel;
import com.supermarket.GUI.components.RoundedScrollPane;
import com.supermarket.utils.Resource;
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

public class FormUpdateProductGUI extends DialogForm {
    private Product product;

    private final ProductBLL productBLL = new ProductBLL();
    private final BrandBLL brandBLL = new BrandBLL();
    private final CategoryBLL categoryBLL = new CategoryBLL();
    private DataTable dataTable;
    private RoundedPanel formDetail;
    private RoundedScrollPane scrollPaneFormDetail;
    private RoundedScrollPane scrollPaneDatatable;
    private List<JLabel> attributeProduct;
    private List<JTextField> jTextFieldProduct;
    private JComboBox cbbUnit;
    private JButton buttonCancel;
    private JButton buttonAdd;
    private boolean flag;
    private JLabel labelImageProduct;
    private JLabel labelAddImage;
    private JFileChooser jFileChooser;

    public FormUpdateProductGUI(Product product) {
        super();
        this.product = product;
        super.setTitle("Quản lý sản phẩm");
        init();
        containerButton.setBackground(new Color(0xFFFFFF));
        setVisible(true);
    }

    public void init() {
        dataTable = new DataTable(new Object[][] {}, new String[] {}, e -> {});
        formDetail = new RoundedPanel();
        attributeProduct = new ArrayList<>();
        jTextFieldProduct = new ArrayList<>();
        cbbUnit = new JComboBox(new String[] {"Lốc", "Chai", "Hộp", "Ly", "Lon"});
        buttonCancel = new JButton("Huỷ");
        buttonAdd = new JButton("Cập nhật");
        scrollPaneDatatable = new RoundedScrollPane(containerTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneFormDetail = new RoundedScrollPane(formDetail, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        labelImageProduct = new JLabel();
        labelAddImage = new JLabel();
        jFileChooser = new JFileChooser();

        scrollPaneDatatable.setPreferredSize(new Dimension(600, 700));
        leftContent.add(scrollPaneDatatable, BorderLayout.CENTER);
        containerTable.add(dataTable);

        scrollPaneFormDetail.setPreferredSize(new Dimension(670, 610));
        rightContent.add(scrollPaneFormDetail, BorderLayout.NORTH);

        formDetail.setBackground(new Color(0xFFBDD2DB));
        formDetail.setLayout(new MigLayout("", "50[]20[]10", "20[]20[]"));

        labelAddImage.setCursor(new Cursor(Cursor.HAND_CURSOR));
        labelAddImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                jFileChooser = new JFileChooser(Resource.getAbsolutePath("img"));

                int value = jFileChooser.showSaveDialog(null);
                if (value == JFileChooser.APPROVE_OPTION)
                {
                    labelAddImage.setVisible(false);
                    labelImageProduct.setIcon(new FlatSVGIcon(jFileChooser.getSelectedFile()));
                    labelImageProduct.setVisible(true);
                    formDetail.repaint();
                    formDetail.revalidate();
                }
            }
        });

        labelImageProduct.setIcon(new FlatSVGIcon("img/Pro1.svg"));
        labelImageProduct.setCursor(new Cursor(Cursor.HAND_CURSOR));
        labelImageProduct.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                String[] options = new String[]{"Huỷ", "Xác nhận"};
                int choice = JOptionPane.showOptionDialog(null, "Xác nhận xoá hình sản phẩm?",
                    "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                if (choice == 1) {
                    labelImageProduct.setVisible(false);
                    labelAddImage.setVisible(true);
                    formDetail.repaint();
                    formDetail.revalidate();
                }
            }
        });

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
                formDetail.add(labelImageProduct, "split 2");
                labelAddImage.setIcon(new FlatSVGIcon("icon/image.svg"));
                formDetail.add(labelAddImage, "wrap");
                labelAddImage.setVisible(false);
                continue;
            }
            if (string.equals("Mã sản phẩm:")) {
                textField.setText(String.valueOf(product.getId()));
                textField.setEnabled(false);
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
                textField.setEnabled(false);
            }
            if (string.equals("Mã thể loại:")) {
                textField.setText(String.valueOf(product.getCategory_id()));
                textField.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        loadTableCategory();
                    }
                });
                textField.setEnabled(false);
            }
            if (string.equals("Đơn vị:")) {
                cbbUnit.setPreferredSize(new Dimension(400, 50));
                cbbUnit.setSelectedItem(product.getUnit());
                formDetail.add(cbbUnit, "wrap");
                continue;
            }
            if (string.equals("Giá bán:") || string.equals("Số lượng:")) {
                if (string.equals("Giá bán:")) {
                    textField.setText(String.valueOf(product.getCost()));
                }
                if (string.equals("Số lượng:")) {
                    textField.setText(String.valueOf(product.getQuantity()));
                }
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
            if (string.equals("Barcode:")) {
                textField.setText(String.valueOf(product.getBarcode()));
            }
            textField.setPreferredSize(new Dimension(400, 50));
            textField.setFont((new Font("FlatLaf.style", Font.BOLD, 14)));
            jTextFieldProduct.add(textField);
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

        buttonAdd.setPreferredSize(new Dimension(100,40));
        buttonAdd.setFont(new Font("FlatLaf.style", Font.BOLD, 15));
        buttonAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                updateProduct();
            }
        });
        containerButton.add(buttonAdd);

    }

    private void updateProduct() {
        for (int i = 0; i < jTextFieldProduct.size(); i++) {
            if (jTextFieldProduct.get(i).getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin sản phẩm.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        int id = Integer.parseInt(jTextFieldProduct.get(0).getText());
        String name = jTextFieldProduct.get(1).getText();
        int brandID = Integer.parseInt(jTextFieldProduct.get(2).getText());
        int categoryID = Integer.parseInt(jTextFieldProduct.get(3).getText());
        String unit = Objects.requireNonNull(cbbUnit.getSelectedItem()).toString();
        double cost = Double.parseDouble((jTextFieldProduct.get(4).getText()));
        double quantity = Double.parseDouble((jTextFieldProduct.get(5).getText()));
        String barcode = jTextFieldProduct.get(6).getText();
        String image = "";
        if (labelAddImage.isVisible()) {
            image = "img/Pro1.svg";
        } else {
            if (jFileChooser.getSelectedFile() == null)
                image = product.getImage();
            else
                image = "img/" + jFileChooser.getSelectedFile().getName();
        }

        Product product = new Product(id, name,brandID,categoryID,unit,cost,quantity,image, barcode,false);

        String[] options = new String[]{"Huỷ", "Xác nhận"};
        int choice = JOptionPane.showOptionDialog(null, "Xác nhận cập nhật sản phẩm?",
            "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (choice == 1) {
            if (productBLL.updateProduct(product).getKey()) {
                JOptionPane.showMessageDialog(null, "Cập nhật sản phẩm thành công!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);

                ProductGUI.loadDataTable(productBLL.getProductList());
                Category category = categoryBLL.findCategoriesBy(Map.of("id", categoryID)).get(0);
                category.setQuantity(category.getQuantity() + 1);
                categoryBLL.updateCategory(category);
                dispose();
            } else {
                SmallDialog.showResult(productBLL.updateProduct(product).getValue());
            }
        }

    }

    private void refresh() {
        jTextFieldProduct.get(0).setEnabled(true);
        jTextFieldProduct.get(0).setText(String.valueOf(productBLL.getAutoID(productBLL.searchProducts())));
        jTextFieldProduct.get(0).setEnabled(false);
        jTextFieldProduct.get(1).setText("");
        jTextFieldProduct.get(2).setEnabled(true);
        jTextFieldProduct.get(2).setText("");
        jTextFieldProduct.get(2).setEnabled(false);
        jTextFieldProduct.get(3).setEnabled(true);
        jTextFieldProduct.get(3).setText("");
        jTextFieldProduct.get(3).setEnabled(false);
        jTextFieldProduct.get(4).setText("");
        jTextFieldProduct.get(5).setText("");
        jTextFieldProduct.get(6).setText("");
    }

    private void loadTableCategory() {
        flag = false;
        Object[][] categoryList = new Object[0][0];
        for (Category category : categoryBLL.getCategoryList()) {
            Object[] object = new Object[3];
            object[0] = category.getId();
            object[1] = category.getName();
            object[2] = category.getQuantity();
            categoryList = Arrays.copyOf(categoryList, categoryList.length + 1);
            categoryList[categoryList.length - 1] = object;
        }

        dataTable = new DataTable(categoryList, new String[] {"Mã thể loại", "Tên thể loại", "Số lượng"}, e -> selectRowTable());
        containerTable.removeAll();
        containerTable.add(dataTable.getTableHeader(), BorderLayout.NORTH);
        containerTable.add(dataTable, BorderLayout.CENTER);

        Category category = categoryBLL.findCategoriesBy(Map.of("id", Integer.parseInt(jTextFieldProduct.get(3).getText()))).get(0);
        int index = categoryBLL.getIndex(category, "id", categoryBLL.getCategoryList());
        dataTable.setRowSelectionInterval(index, index);

        containerTable.repaint();
        containerTable.revalidate();
        scrollPaneDatatable.setViewportView(containerTable);
    }

    private void loadTableBrand() {
        flag = true;
        Object[][] brandList = new Object[0][0];
        SupplierBLL supplierBLL = new SupplierBLL();
        for (Brand brand : brandBLL.getBrandList()) {
            Object[] object = new Object[3];
            object[0] = brand.getId();
            object[1] = brand.getName();
            object[2] = supplierBLL.findSuppliersBy(Map.of("id", brand.getSupplier_id())).get(0).getName();
            brandList = Arrays.copyOf(brandList, brandList.length + 1);
            brandList[brandList.length - 1] = object;
        }

        dataTable = new DataTable(brandList, new String[] {"Mã thương hiệu", "Tên thương hiệu", "Tên nhà cung cấp"}, e -> selectRowTable());
        containerTable.removeAll();
        containerTable.add(dataTable.getTableHeader(), BorderLayout.NORTH);
        containerTable.add(dataTable, BorderLayout.CENTER);

        Brand brand = brandBLL.findBrandsBy(Map.of("id", Integer.parseInt(jTextFieldProduct.get(2).getText()))).get(0);
        int index = brandBLL.getIndex(brand, "id", brandBLL.getBrandList());
        dataTable.setRowSelectionInterval(index, index);

        containerTable.repaint();
        containerTable.revalidate();
        scrollPaneDatatable.setViewportView(containerTable);
    }

    public void selectRowTable() {
        String id = "";
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        int indexRow = dataTable.getSelectedRow();
        id = model.getDataVector().elementAt(indexRow).get(0).toString();
        if (flag) {
            jTextFieldProduct.get(2).setEnabled(true);
            jTextFieldProduct.get(2).setText(id);
            jTextFieldProduct.get(2).setEnabled(false);
        } else {
            jTextFieldProduct.get(3).setEnabled(true);
            jTextFieldProduct.get(3).setText(id);
            jTextFieldProduct.get(3).setEnabled(false);
        }
    }
}

