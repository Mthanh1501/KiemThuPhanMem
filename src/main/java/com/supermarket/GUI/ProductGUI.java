package com.supermarket.GUI;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.supermarket.BLL.BrandBLL;
import com.supermarket.BLL.CategoryBLL;
import com.supermarket.BLL.ProductBLL;
import com.supermarket.BLL.SupplierBLL;
import com.supermarket.DTO.*;
import com.supermarket.GUI.DialogGUI.FormAddProductGUI;
import com.supermarket.GUI.DialogGUI.FormDetailProductGUI;
import com.supermarket.GUI.DialogGUI.FormUpdateProductGUI;
import com.supermarket.GUI.components.DataTable;
import com.supermarket.GUI.components.Layout4;
import com.supermarket.GUI.components.RoundedPanel;
import com.supermarket.GUI.components.RoundedScrollPane;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.*;

public class ProductGUI extends Layout4 {
    private ProductBLL productBLL = new ProductBLL();
    private static DataTable dataTable;
    private RoundedScrollPane scrollPane;
    private JLabel iconDetail;
    private JLabel iconAdd;
    private JLabel iconEdit;
    private JLabel iconDelete;
    private RoundedPanel formDetail;
    private Product product;
    private JComboBox cbbAttributeProduct;
    private JTextField jTextFieldSearch;
    private List<JTextField> jTextFieldBrand;
    private List<JTextField> jTextFieldCategory;
    private JComboBox cbbBrand;
    private JComboBox cbbCategory;
    private JComboBox cbbSupplier;
    private BrandBLL brandBLL = new BrandBLL();
    private CategoryBLL categoryBLL = new CategoryBLL();
    private static Object[][] productlist;
    private  DefaultComboBoxModel<String> comboBoxBrand;
    private  DefaultComboBoxModel<String> comboBoxCategory;

    public ProductGUI(List<Function> functions) {
        super();
        initComponent(functions);
    }

    public void initComponent(List<Function> functions) {
        productlist = new Object[0][0];

        formDetail = new RoundedPanel();
        dataTable = new DataTable(new Object[][]{}, new String[]{"Mã sản phẩm","Tên sản phẩm","Thương hiệu", "Thể loại", "Giá bán", "Số lượng"}, e -> {});
        scrollPane = new RoundedScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        leftContent.add(scrollPane, BorderLayout.CENTER);

        iconDetail = new JLabel();
        iconAdd = new JLabel();
        iconEdit = new JLabel();
        iconDelete = new JLabel();
        jTextFieldCategory = new ArrayList<>();
        jTextFieldBrand = new ArrayList<>();

        formDetail.setBackground(new Color(0xFFBDD2DB));
        formDetail.setLayout(new FlowLayout(FlowLayout.LEFT, 20,20));

        cbbAttributeProduct= new JComboBox(new String[] {"Tên sản phẩm","Thương hiệu","Thể loại", "Sắp hết hàng"});

        cbbBrand= new JComboBox<>();
        cbbCategory = new JComboBox<>();
        cbbSupplier = new JComboBox<>();

        if (functions.stream().anyMatch(f -> f.getName().equals("Chi tiết"))) {
            iconDetail.setIcon(new FlatSVGIcon("icon/detail.svg"));
            iconDetail.setCursor(new Cursor(Cursor.HAND_CURSOR));
            iconDetail.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    showDetailAccount();
                }
            });
            leftMenu.add(iconDetail);
        }

        if (functions.stream().anyMatch(f -> f.getName().equals("Thêm"))) {
            iconAdd.setIcon(new FlatSVGIcon("icon/add.svg"));
            iconAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
            iconAdd.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    addProduct();
                }
            });
            leftMenu.add(iconAdd);
        }

        if (functions.stream().anyMatch(f -> f.getName().equals("Sửa"))) {
            iconEdit.setIcon(new FlatSVGIcon("icon/edit.svg"));
            iconEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
            iconEdit.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    updateProduct();
                }
            });
            leftMenu.add(iconEdit);
        }

        if (functions.stream().anyMatch(f -> f.getName().equals("Xóa"))) {
            iconDelete.setIcon(new FlatSVGIcon("icon/remove.svg"));
            iconDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
            iconDelete.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    deleteProduct();
                }
            });
            leftMenu.add(iconDelete);
        }

        cbbAttributeProduct.setPreferredSize(new Dimension(130, 30));
        cbbAttributeProduct.addActionListener(e -> selectSearchFilter());
        rightMenu.add(cbbAttributeProduct);

        for (Brand brand : brandBLL.getBrandList()) {
            cbbBrand.addItem(brand.getName());
        }
        rightMenu.add(cbbBrand);
        cbbBrand.setVisible(false);
        cbbBrand.addActionListener(e -> searchByBrand());

        for (Category category : categoryBLL.getCategoryList()) {
            cbbCategory.addItem(category.getName());
        }
        rightMenu.add(cbbCategory);
        cbbCategory.setVisible(false);
        cbbCategory.addActionListener(e -> searchByCategory());

        SupplierBLL supplierBLL = new SupplierBLL();
        for (Supplier supplier : supplierBLL.getSupplierList()) {
            cbbSupplier.addItem(supplier.getName());
        }

        jTextFieldSearch = new JTextField();
        jTextFieldSearch.setPreferredSize(new Dimension(200, 30));
        jTextFieldSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchProduct();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchProduct();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchProduct();
            }
        });
        rightMenu.add(jTextFieldSearch);
        loadDataTable(productBLL.getProductList());
    }

    private void showDetailAccount() {
        productBLL = new ProductBLL();
        if (dataTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm cần xem chi tiết.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        new FormDetailProductGUI(productBLL.findProductsBy(Map.of("id", Integer.parseInt(model.getValueAt(dataTable.getSelectedRow(), 0).toString()))).get(0));
    }

    private void searchProduct() {
        if (jTextFieldSearch.getText().isEmpty()) {
            loadDataTable(productBLL.getProductList());
        } else {
            loadDataTable(productBLL.findProducts("name", jTextFieldSearch.getText()));
        }
    }

    private RoundedPanel panelBtn;
    private JButton btnAdd,btnRemove,btnEdit;

    private void searchByBrand() {
        if (cbbBrand.getSelectedIndex() == -1)
            return;
        bottom.removeAll();
        formDetail.removeAll();
        jTextFieldBrand = new ArrayList<>();
        JLabel title = new JLabel("THƯƠNG HIỆU");
        title.setPreferredSize(new Dimension(300,50));
        title.setFont((new Font("FlatLaf.style", Font.BOLD, 23)));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        formDetail.add(title);

        SupplierBLL supplierBLL = new SupplierBLL();
        Brand brand1 = brandBLL.getBrandList().get(cbbBrand.getSelectedIndex());
        Supplier supplier = supplierBLL.findSuppliersBy(Map.of("id", brand1.getSupplier_id())).get(0);
        for (String string : new String[] {"Tên thương hiệu:", "Nhà cung cấp:"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("FlatLaf.style", Font.PLAIN, 14)));
            formDetail.add(label);

            JTextField textField = new JTextField();
            if (string.equals("Tên thương hiệu:")) {
                textField.setText(brand1.getName());
            }
            if (string.equals("Nhà cung cấp:")) {
                cbbSupplier.setSelectedItem(supplier.getName());
                cbbSupplier.setPreferredSize(new Dimension(300, 50));
                formDetail.add(cbbSupplier);
                continue;
            }
            textField.setPreferredSize(new Dimension(300, 50));
            textField.setFont((new Font("FlatLaf.style", Font.PLAIN, 14)));
            jTextFieldBrand.add(textField);
            formDetail.add(textField);

        }
        btnAdd = new JButton();
        btnAdd.setText("Thêm");
        btnAdd.setBackground(new Color(0x8EBCDA));
        btnAdd.setFont((new Font("FlatLaf.style", Font.PLAIN, 18)));
        btnEdit = new JButton();
        btnEdit.setBackground(new Color(0x8EBCDA));
        btnEdit.setText("Sửa");
        btnEdit.setFont((new Font("FlatLaf.style", Font.PLAIN, 18)));
        btnRemove = new JButton();
        btnRemove.setBackground(new Color(0xC50101));
        btnRemove.setText("Xoá");
        btnRemove.setFont((new Font("FlatLaf.style", Font.PLAIN, 18)));
        panelBtn = new RoundedPanel();

        panelBtn.setPreferredSize(new Dimension(300,80));
        panelBtn.setLayout(new GridLayout(2,2));
        panelBtn.add(btnAdd);
        panelBtn.add(btnEdit);
        panelBtn.add(btnRemove);
        panelBtn.setBackground(null);
        formDetail.add(panelBtn);

        rightContent.add(formDetail);
        rightContent.repaint();
        rightContent.revalidate();
        bottom.add(leftContent, BorderLayout.WEST);
        bottom.add(rightContent, BorderLayout.EAST);
        bottom.repaint();
        bottom.revalidate();

        btnAdd.addActionListener(e -> {
            String[] options = new String[]{"Huỷ", "Xác nhận"};
            int choice = JOptionPane.showOptionDialog(null, "Xác nhận thêm thương hiệu?",
                "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            if (choice == 1) {
                addBrand();
            }
        });
        btnRemove.addActionListener(e -> {
            if (products.size() != 0){
                JOptionPane.showMessageDialog(null, "Tồn tại sản phẩm thuộc thương hiệu này!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                String[] options = new String[]{"Huỷ", "Xác nhận"};
                int choice = JOptionPane.showOptionDialog(null, "Xác nhận xoá thương hiêu?",
                    "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                if (choice == 1) {
                    deleteBrand();
                }
            }
        });
        btnEdit.addActionListener(e -> {
            String[] options = new String[]{"Huỷ", "Xác nhận"};
            int choice = JOptionPane.showOptionDialog(null, "Xác nhận sửa thương hiệu?",
                "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            if (choice == 1) {
                editBrand();
            }
        });

        for (Brand brand : brandBLL.getBrandList()) {
            if (brand.getName().equals(cbbBrand.getSelectedItem())) {
                products = productBLL.findProductsBy(Map.of("brand_id", brand.getId()));
                loadDataTable(products);
                return;
            }
        }

    }
    private List<Product> products;

    private void searchByCategory() {
        if (cbbCategory.getSelectedIndex() == -1)
            return;
        bottom.removeAll();
        formDetail.removeAll();
        products = new ArrayList<>();
        jTextFieldCategory = new ArrayList<>();
        JLabel title = new JLabel("THỂ LOẠI");
        title.setFont((new Font("FlatLaf.style", Font.BOLD, 23)));
        title.setPreferredSize(new Dimension(300,50));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        formDetail.add(title);

        Category category1 = categoryBLL.getCategoryList().get(cbbCategory.getSelectedIndex());
        for (String string : new String[] {" Tên thể loại:"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("FlatLaf.style", Font.PLAIN, 14)));
            formDetail.add(label);

            JTextField textField = new JTextField();
            textField.setText(category1.getName());

            textField.setPreferredSize(new Dimension(300, 50));
            textField.setFont((new Font("FlatLaf.style", Font.PLAIN, 14)));
            jTextFieldCategory.add(textField);
            formDetail.add(textField);
        }

        btnAdd = new JButton();
        btnAdd.setText("Thêm");
        btnAdd.setBackground(new Color(0x8EBCDA));
        btnAdd.setFont((new Font("FlatLaf.style", Font.PLAIN, 18)));
        btnEdit = new JButton();
        btnEdit.setBackground(new Color(0x8EBCDA));
        btnEdit.setText("Sửa");
        btnEdit.setFont((new Font("FlatLaf.style", Font.PLAIN, 18)));
        btnRemove = new JButton();
        btnRemove.setBackground(new Color(0xC50101));
        btnRemove.setText("Xoá");
        btnRemove.setFont((new Font("FlatLaf.style", Font.PLAIN, 18)));
        panelBtn = new RoundedPanel();

        panelBtn.setPreferredSize(new Dimension(300,80));
        panelBtn.setLayout(new GridLayout(2,2));
        panelBtn.add(btnAdd);
        panelBtn.setBackground(null);
        panelBtn.add(btnEdit);
        panelBtn.add(btnRemove);
        formDetail.add(panelBtn);

        btnAdd.addActionListener(e -> {
            String[] options = new String[]{"Huỷ", "Xác nhận"};
            int choice = JOptionPane.showOptionDialog(null, "Xác nhận thêm thể loại?",
                "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            if (choice == 1) {
                addCategory();
            }
        });
        btnRemove.addActionListener(e -> {
            if (products.size() != 0){
                JOptionPane.showMessageDialog(null, "Tồn tại sản phẩm thuộc thể loại này!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                String[] options = new String[]{"Huỷ", "Xác nhận"};
                int choice = JOptionPane.showOptionDialog(null, "Xác nhận xoá thể loại?",
                    "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                if (choice == 1) {
                    deleteCategory();
                }
            }
        });
        btnEdit.addActionListener(e -> {
            String[] options = new String[]{"Huỷ", "Xác nhận"};
            int choice = JOptionPane.showOptionDialog(null, "Xác nhận sửa thể loại?",
                "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            if (choice == 1) {
                editCategory();
            }
        });

        rightContent.add(formDetail);
        rightContent.repaint();
        rightContent.revalidate();
        bottom.add(leftContent, BorderLayout.WEST);
        bottom.add(rightContent, BorderLayout.EAST);
        bottom.repaint();
        bottom.revalidate();
        for (Category category : categoryBLL.getCategoryList()) {
            if (category.getName().equals(cbbCategory.getSelectedItem())) {
                products = productBLL.findProductsBy(Map.of("category_id", category.getId()));
                loadDataTable(products);
                return;
            }
        }
    }
    private void selectSearchFilter() {
        if (Objects.requireNonNull(cbbAttributeProduct.getSelectedItem()).toString().contains("Thương hiệu")) {
            jTextFieldSearch.setVisible(false);
            cbbCategory.setVisible(false);
            cbbBrand.setSelectedIndex(0);
            cbbBrand.setVisible(true);
            searchByBrand();
            return;
        } else if (Objects.requireNonNull(cbbAttributeProduct.getSelectedItem()).toString().contains("Thể loại")) {
            jTextFieldSearch.setVisible(false);
            cbbBrand.setVisible(false);
            cbbCategory.setSelectedIndex(0);
            cbbCategory.setVisible(true);
            searchByCategory();
            return;
        } else if (Objects.requireNonNull(cbbAttributeProduct.getSelectedItem()).toString().contains("Sắp hết hàng")) {
            jTextFieldSearch.setVisible(false);
            cbbBrand.setVisible(false);
            cbbCategory.setVisible(false);
            searchByQuantity();
        } else {
            cbbCategory.setVisible(false);
            cbbBrand.setVisible(false);
            jTextFieldSearch.setVisible(true);
            searchProduct();
        }
        bottom.removeAll();
        bottom.add(leftContent, BorderLayout.CENTER);
        bottom.repaint();
        bottom.revalidate();
    }

    private void searchByQuantity() {
        loadDataTable(productBLL.searchProducts("deleted = 0", "`product`.quantity > 0 AND `product`.quantity <= 20"));
    }

    private void deleteCategory() {
        List<Category> categories = categoryBLL.findCategoriesBy(Map.of("name",jTextFieldCategory.get(0).getText()));
        if(categories.size() != 0) {
            categoryBLL.deleteCategory(categories.get(0));
            cbbCategory.removeAllItems();
            categoryBLL = new CategoryBLL();
            for (Category category : categoryBLL.getCategoryList()) {
                cbbCategory.addItem(category.getName());
            }
            cbbCategory.setSelectedIndex(0);
            searchByCategory();
            JOptionPane.showMessageDialog(null, "Xóa thể loại thành công!",
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
        else  {
            JOptionPane.showMessageDialog(null, "Xóa thể loại không thành công!",
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deleteBrand() {
        List<Brand> brands = brandBLL.findBrandsBy(Map.of("name",jTextFieldBrand.get(0).getText()));
        String j = jTextFieldBrand.get(0).getText();
        if(brands.size() != 0) {
            brandBLL.deleteBrand(brands.get(0));
            cbbBrand.removeAllItems();
            brandBLL = new BrandBLL();
            for (Brand brand : brandBLL.getBrandList()) {
                cbbBrand.addItem(brand.getName());
            }
            cbbBrand.setSelectedIndex(0);
            searchByBrand();
            JOptionPane.showMessageDialog(null, "Xóa thương hiệu thành công!",
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
        else {
            JOptionPane.showMessageDialog(null, "Không tồn tại thương hiệu trong cơ sở dữ liệu!",
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void addBrand() {
        List<Brand> brands = brandBLL.findBrandsBy(Map.of("name",jTextFieldBrand.get(0).getText()));
        SupplierBLL supplierBLL = new SupplierBLL();
        List<Supplier> suppliers = supplierBLL.findSuppliersBy(Map.of("name", Objects.requireNonNull(cbbSupplier.getSelectedItem()).toString()));
        if(!suppliers.isEmpty()) {
            if (!brands.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Thương hiệu đã tồn tại!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                Brand brand = new Brand();
                brand.setId(brandBLL.getAutoID(brandBLL.searchBrands()));
                brand.setName(jTextFieldBrand.get(0).getText());
                brand.setSupplier_id(suppliers.get(0).getId());
                brand.setDeleted(false);
                brandBLL.addBrand(brand);
                cbbBrand.removeAllItems();
                brandBLL = new BrandBLL();
                for (Brand brand1 : brandBLL.getBrandList()) {
                    cbbBrand.addItem(brand1.getName());
                }
                cbbBrand.setSelectedIndex(0);
                searchByBrand();
                JOptionPane.showMessageDialog(null, "Thêm thể loại thành công!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Không tồn tại nhà cung cấp!",
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void addCategory() {
        List<Category> categories = categoryBLL.findCategoriesBy(Map.of("name",jTextFieldCategory.get(0).getText()));
        if(categories.size() != 0) {
            JOptionPane.showMessageDialog(null, "Thể loại đã tồn tại!",
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
        else {
            Category category = new Category();
            category.setId(categoryBLL.getAutoID(categoryBLL.searchCategorys()));
            category.setName(jTextFieldCategory.get(0).getText());
            category.setQuantity(0);
            category.setDeleted(false);
            categoryBLL.addCategory(category);
            cbbCategory.removeAllItems();
            categoryBLL = new CategoryBLL();
            for (Category category1 : categoryBLL.getCategoryList()) {
                cbbCategory.addItem(category1.getName());
            }
            cbbCategory.setSelectedIndex(0);
            searchByCategory();
            JOptionPane.showMessageDialog(null, "Thêm thể loại thành công!",
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);

        }
    }

    private void editBrand() {
        Brand brand1 = brandBLL.getBrandList().get(cbbBrand.getSelectedIndex());
        brand1.setName(jTextFieldBrand.get(0).getText());
        brandBLL.updateBrand(brand1);
        cbbBrand.removeAllItems();
        brandBLL = new BrandBLL();
        for (Brand brand : brandBLL.getBrandList()) {
            cbbBrand.addItem(brand.getName());
        }
        cbbBrand.setSelectedIndex(0);
        searchByBrand();
        JOptionPane.showMessageDialog(null, "Chỉnh sửa thương hiệu thành công!",
            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void editCategory() {
        Category category = categoryBLL.getCategoryList().get(cbbCategory.getSelectedIndex());
        category.setName(jTextFieldCategory.get(0).getText());
        categoryBLL.updateCategory(category);
        cbbCategory.removeAllItems();
        categoryBLL = new CategoryBLL();
        for (Category category1 : categoryBLL.getCategoryList()) {
            cbbCategory.addItem(category1.getName());
        }
        cbbCategory.setSelectedIndex(0);
        searchByCategory();
        JOptionPane.showMessageDialog(null, "Chỉnh sửa thể loại thành công!",
            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteProduct() {
        if (dataTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm cần xoá.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();

        String[] options = new String[]{"Huỷ", "Xác nhận"};
        int choice = JOptionPane.showOptionDialog(null, "Xác nhận xoá sản phẩm?",
            "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (choice == 1) {
            if (productBLL.deleteProduct(productBLL.findProductsBy(Map.of("id", Integer.parseInt(model.getValueAt(dataTable.getSelectedRow(), 0).toString()))).get(0))) {
                JOptionPane.showMessageDialog(null, "Xoá sản phẩm thành công!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);

                loadDataTable(productBLL.getProductList());
            } else {
                JOptionPane.showMessageDialog(null, "Xoá sản phẩm không thành công!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addProduct() {
        new FormAddProductGUI();
    }

    private void updateProduct() {
        productBLL = new ProductBLL();
        if (dataTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm cần cập nhật.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        new FormUpdateProductGUI(productBLL.findProductsBy(Map.of("id", Integer.parseInt(model.getValueAt(dataTable.getSelectedRow(), 0).toString()))).get(0));
    }

    public static  void loadDataTable(List<Product> products) {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        productlist = new Object[0][0];
        BrandBLL brandBLL = new BrandBLL();
        CategoryBLL categoryBLL = new CategoryBLL();
        for (Product product : products) {
            Object[] object = new Object[6];
            object[0] = product.getId();
            object[1] = product.getName();
            object[2] = brandBLL.findBrandsBy(Map.of("id", product.getBrand_id())).get(0).getName();
            object[3] = categoryBLL.findCategoriesBy(Map.of("id", product.getCategory_id())).get(0).getName();
            object[4] = product.getCost();
            object[5] = product.getQuantity();
            productlist = Arrays.copyOf(productlist, productlist.length + 1);
            productlist[productlist.length - 1] = object;
        }

        model.setRowCount(0);
        for (Object[] object : productlist) {
            model.addRow(object);
        }
    }
}
