package com.supermarket.GUI;

import com.supermarket.BLL.*;
import com.supermarket.DTO.*;
import com.supermarket.GUI.DialogGUI.SmallDialog;
import com.supermarket.GUI.components.DataTable;
import com.supermarket.GUI.components.Layout3;
import com.supermarket.GUI.components.RoundedPanel;
import com.supermarket.GUI.components.RoundedScrollPane;
import com.supermarket.utils.Date;
import com.toedter.calendar.JDateChooser;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.*;

public class DiscountGUI extends Layout3 {
    private RoleBLL roleBLL = new RoleBLL();
    private AccountBLL accountBLL = new AccountBLL();
    private DiscountBLL discountBLL = new DiscountBLL();
    private Discount_detailBLL discount_detailBLL = new Discount_detailBLL();
    private ProductBLL productBLL = new ProductBLL();
    private final CategoryBLL categoryBLL = new CategoryBLL();
    private final BrandBLL brandBLL = new BrandBLL();
    private static DataTable dataTableDiscount;
    private static DataTable dataTableDiscountDetail;
    private RoundedPanel searchPanel;
    private RoundedPanel containerDiscount;
    private RoundedScrollPane scrollPaneDiscount;
    private RoundedScrollPane scrollPaneDiscountDetail;
    private JTextField jTextFieldSearch;
    private List<JTextField> jTextFieldDiscount;
    private JDateChooser[] jDateChooser;
    private JTextField[] dateTextField;
    private JTextField[] jTextFields;
    private JComboBox cbbAttributeProduct;
    private JComboBox cbbCategory;
    private JComboBox cbbBrand;
    private JComboBox cbbStatus;
    private static Discount discountSelected;
    private static Discount newDiscount = null;
    private JButton buttonAdd;
    private JButton buttonCancel;
    private static List<Integer> productIDInDiscount;
    private boolean currentUserHasRole(int roleId) {
        int currentRoleId = getCurrentUserRoleId(); // Sử dụng phương thức getRoleIdByLoginCredentials để lấy role_id của tài khoản đăng nhập
        return currentRoleId == roleId;
    }

    private int getCurrentUserRoleId() {
        // Gọi phương thức getRoleIdByLoginCredentials từ AccountBLL để lấy role_id của tài khoản đăng nhập
        String username = ""; // Thay bằng tên người dùng của tài khoản đăng nhập
        String password = ""; // Thay bằng mật khẩu của tài khoản đăng nhập
        return new AccountBLL().getRoleIdByLoginCredentials(username, password);
    }
    public DiscountGUI(List<Function> functions) {
        super();
        init(functions);

    }

    public void init(List<Function> functions) {
        containerDiscount = new RoundedPanel();
        jTextFieldSearch = new JTextField();
        jTextFieldDiscount = new ArrayList<>();
        cbbAttributeProduct = new JComboBox(new String[] {"Tên sản phẩm", "Thương hiệu", "Thể loại"});
        cbbCategory = new JComboBox();
        cbbBrand = new JComboBox();
        cbbStatus = new JComboBox(new String[] {"Đang áp dụng", "Ngừng áp dụng"});
        searchPanel = new RoundedPanel();
        buttonAdd = new JButton("Thêm");
        buttonCancel = new JButton("Huỷ");
        productIDInDiscount = new ArrayList<>();
        dataTableDiscount = new DataTable(new Object[][] {},
            new String[] {"Mã đợt giảm giá", "Phần trăm giảm giá", "Ngày bắt đầu", "Ngày kết thúc", "Trạng thái"},
            e -> {
                List<Product> products = new ArrayList<>();
                DefaultTableModel modelDiscount = (DefaultTableModel) dataTableDiscount.getModel();
                int indexRow = dataTableDiscount.getSelectedRow();
                if (indexRow != -1) {
                    Object[] rowData = modelDiscount.getDataVector().elementAt(indexRow).toArray();
                    String[] data = new String[rowData.length];
                    for (int i = 0; i < rowData.length; i++) {
                        data[i] = rowData[i].toString();
                    }
                    String[] discount = String.join(" | ", data).split(" \\| ");
                    discountSelected = discountBLL.findDiscountsBy(Map.of("id", Integer.parseInt(discount[0]))).get(0);
                    for (Discount_detail discountDetail : discount_detailBLL.findDiscount_detailsBy(Map.of("discount_id", discountSelected.getId()))) {
                        products.add(productBLL.findProductsBy(Map.of("id", discountDetail.getProduct_id())).get(0));
                    }
                }
                loadDataTableDiscountDetail(products);
            });
        scrollPaneDiscount = new RoundedScrollPane(dataTableDiscount, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        dataTableDiscountDetail = new DataTable(new Object[][] {},
            new String[] {"Mã sản phẩm", "Tên sản phẩm", "Giá cũ", "Giá mới", ""}, e -> fillForm(), 4);
        scrollPaneDiscountDetail = new RoundedScrollPane(dataTableDiscountDetail, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        cbbAttributeProduct.setPreferredSize(new Dimension(130, 30));
        cbbAttributeProduct.addActionListener(e -> selectSearchFilter());
        searchPanel.add(cbbAttributeProduct);

        for (Category category : categoryBLL.getCategoryList()) {
            cbbCategory.addItem(category.getName());
        }
        searchPanel.add(cbbCategory);
        cbbCategory.setVisible(false);
        cbbCategory.addActionListener(e -> searchByCategory());

        for (Brand brand : brandBLL.getBrandList()) {
            cbbBrand.addItem(brand.getName());
        }
        searchPanel.add(cbbBrand);
        cbbBrand.setVisible(false);
        cbbBrand.addActionListener(e -> searchByBrand());
        jTextFieldSearch.setPreferredSize(new Dimension(200, 30));
        jTextFieldSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchProducts();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchProducts();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchProducts();
            }
        });
        searchPanel.add(jTextFieldSearch);

        searchPanel.setPreferredSize(new Dimension(800, 40));
        bottomContent.add(searchPanel, BorderLayout.NORTH);

        topContent.add(scrollPaneDiscount);
        loadDataTableDiscount(discountBLL.searchDiscounts());

        bottomContent.add(scrollPaneDiscountDetail, BorderLayout.CENTER);
        loadDataTableDiscountDetail(productBLL.getProductList());

        containerDiscount.setLayout(new MigLayout("", "10[]15[]10", "10[]20[]"));
        containerDiscount.setBackground(new Color(0xFFBDD2DB, true));
        rightContent.add(containerDiscount, BorderLayout.CENTER);

        jTextFields = new JTextField[2];
        jDateChooser = new JDateChooser[2];
        dateTextField = new JTextField[2];
        for (int i = 0; i < 2; i++) {
            jTextFields[i] = new JTextField();
            jTextFields[i].setFont(new Font("Times New Roman", Font.BOLD, 15));
            jTextFields[i].setPreferredSize(new Dimension(200, 50));
            jTextFields[i].setAutoscrolls(true);
            jDateChooser[i] = new JDateChooser();
            jDateChooser[i].setDateFormatString("dd/MM/yyyy");
            jDateChooser[i].setPreferredSize(new Dimension(200, 50));
            jDateChooser[i].setMinSelectableDate(new Date(1, 1, 1000).toJDateSafe());
            dateTextField[i] = (JTextField) jDateChooser[i].getDateEditor().getUiComponent();
            dateTextField[i].setFont(new Font("Tahoma", Font.BOLD, 14));
//            int index = i;
//            dateTextField[i].addKeyListener(new KeyAdapter() {
//                @Override
//                public void keyTyped(KeyEvent e) {
//                    try {
//                        Date day = Date.parseDate(dateTextField[index].getText());
//                        jDateChooser[index].setDate(day.toJDate());
//                    } catch (Exception ex) {
//                        JOptionPane.showMessageDialog(null, "Ngày không hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
//                        e.consume();
//                    }
//                }
//            });
        }

        for (String string : new String[]{"Mã đợt giảm giá:", "Phần trăm giảm giá:", "Ngày BĐ:", "Ngày KT:", "Trạng thái:"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("FlatLaf.style", Font.PLAIN, 16)));
            containerDiscount.add(label);

            JTextField textField = new JTextField();
            if (string.equals("Mã đợt giảm giá:")) {
                textField.setText(String.valueOf(discountBLL.getAutoID(discountBLL.searchDiscounts())));
                textField.setEditable(false);
                textField.setEnabled(false);
            }
            if (string.equals("Phần trăm giảm giá:")) {
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
            if (string.equals("Ngày BĐ:")) {
                containerDiscount.add(jDateChooser[0], "wrap");
                continue;
            }

            if (string.equals("Ngày KT:")) {
                containerDiscount.add(jDateChooser[1], "wrap");
                continue;
            }

            if (string.equals("Trạng thái:")) {
                cbbStatus.setPreferredSize(new Dimension(200, 50));
                containerDiscount.add(cbbStatus, "wrap");
                cbbStatus.setEnabled(false);
                continue;
            }
            textField.setPreferredSize(new Dimension(200, 50));
            textField.setFont((new Font("FlatLaf.style", Font.PLAIN, 14)));
            jTextFieldDiscount.add(textField);
            containerDiscount.add(textField, "wrap");
        }

        buttonCancel.setPreferredSize(new Dimension(40,40));
        buttonCancel.setFont(new Font("FlatLaf.style", Font.BOLD, 15));
        buttonCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonCancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                refresh();
            }
        });
        containerDiscount.add(buttonCancel);

        buttonAdd.setPreferredSize(new Dimension(40,40));
        buttonAdd.setFont(new Font("FlatLaf.style", Font.BOLD, 15));
        buttonAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                addDiscount();
            }
        });
        containerDiscount.add(buttonAdd, "wrap");
        if (currentUserHasRole(3)) { // Nếu người dùng không có vai trò có role_id = 3
            buttonAdd.setVisible(false);
            buttonCancel.setVisible(false);
        }

    }

    private void addDiscount() {
        for (int i = 0; i < jDateChooser.length; i++) {
            if (jDateChooser[i].getDate() == null) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin đợt khuyến mãi.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        if (jTextFieldDiscount.get(1).getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin đợt khuyến mãi.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Date startDate = null;
        try {
            startDate = new Date(jDateChooser[0].getDate());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi định dạng ngày.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Date endDate = null;
        try {
            endDate = new Date(jDateChooser[1].getDate());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi định dạng ngày.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (startDate.isAfter(endDate)) {
            JOptionPane.showMessageDialog(this, "Ngày bắt đầu phải trước ngày kết thúc", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (productIDInDiscount.isEmpty()) {
            int id = Integer.parseInt(jTextFieldDiscount.get(0).getText());
            double percent = Double.parseDouble(jTextFieldDiscount.get(1).getText());
            loadDataTableDiscount(discountBLL.searchDiscounts());
            loadDataTableDiscountDetail(productBLL.getProductList());
            newDiscount = new Discount(id, percent, startDate, endDate, false);
            JOptionPane.showMessageDialog(this, "Vui lòng thêm sản phẩm vào đợt giảm giá", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            String[] options = new String[]{"Huỷ", "Xác nhận"};
            int choice = JOptionPane.showOptionDialog(null, "Xác nhận thêm đợt giảm giá?",
                "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            if (choice == 1) {
                if (discountBLL.addDiscount(newDiscount).getKey()) {
                    JOptionPane.showMessageDialog(null, "Thêm đợt giảm giá thành công!",
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);

                    for (Discount discount : discountBLL.getDiscountList()) {
                        if (discount.getId() != newDiscount.getId())
                            discount.setStatus(true);
                        discountBLL.updateDiscount(discount);
                    }

                    for (Integer i : productIDInDiscount) {
                        Discount_detail discountDetail = new Discount_detail();
                        discountDetail.setDiscount_id(newDiscount.getId());
                        discountDetail.setProduct_id(i);
                        discountDetail.setStatus(false);
                        discount_detailBLL.addDiscount_detail(discountDetail);
                    }
                    productIDInDiscount.removeAll(productIDInDiscount);
                    refresh();
                } else {
                    SmallDialog.showResult(discountBLL.addDiscount(newDiscount).getValue());
                }
            }
        }

    }

    private void refresh() {
        jTextFieldDiscount.get(0).setEditable(true);
        jTextFieldDiscount.get(0).setText(String.valueOf(discountBLL.getAutoID(discountBLL.searchDiscounts())));
        jTextFieldDiscount.get(0).setEditable(false);
        jTextFieldDiscount.get(0).setEnabled(false);
        jTextFieldDiscount.get(1).setText("");
        jDateChooser[0].setDate(null);
        jDateChooser[1].setDate(null);
        loadDataTableDiscount(discountBLL.searchDiscounts());
        loadDataTableDiscountDetail(productBLL.getProductList());
        newDiscount = null;
    }

    private void fillForm() {
        if (newDiscount != null && dataTableDiscount.getSelectedRow() == -1) {
            DefaultTableModel model = (DefaultTableModel) dataTableDiscountDetail.getModel();
            Object[] rowData = model.getDataVector().elementAt(dataTableDiscountDetail.getSelectedRow()).toArray();
            String[] data = new String[rowData.length];
            for (int i = 0; i < rowData.length; i++) {
                if (rowData[i] != null)
                    data[i] = rowData[i].toString();
            }
            String[] product = String.join(" | ", data).split(" \\| ");
            Product product1 = productBLL.findProductsBy(Map.of("id", Integer.parseInt(product[0]))).get(0);
            if (data[4] == null) {
                productIDInDiscount.add(product1.getId());
                double newPrice = product1.getCost() - (product1.getCost() * newDiscount.getPercent() / 100);
                model.setValueAt(newPrice, dataTableDiscountDetail.getSelectedRow(), 3);
                model.setValueAt(true, dataTableDiscountDetail.getSelectedRow(), 4);
            } else {
                productIDInDiscount.remove((Object)product1.getId());
                model.setValueAt(null, dataTableDiscountDetail.getSelectedRow(), 3);
                model.setValueAt(null, dataTableDiscountDetail.getSelectedRow(), 4);
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
        } else if (Objects.requireNonNull(cbbAttributeProduct.getSelectedItem()).toString().contains("Thể loại")) {
            jTextFieldSearch.setVisible(false);
            cbbBrand.setVisible(false);
            cbbCategory.setSelectedIndex(0);
            cbbCategory.setVisible(true);
            searchByCategory();
        } else {
            cbbBrand.setVisible(false);
            cbbCategory.setVisible(false);
            jTextFieldSearch.setVisible(true);
            searchProducts();
        }
    }

    private void searchByCategory() {
        for (Category category : categoryBLL.getCategoryList()) {
            if (category.getName().equals(cbbCategory.getSelectedItem())) {
                loadDataTableDiscountDetail(productBLL.findProductsBy(Map.of("category_id", category.getId())));
                return;
            }
        }
    }

    private void searchByBrand() {
        for (Brand brand : brandBLL.getBrandList()) {
            if (brand.getName().equals(Objects.requireNonNull(cbbBrand.getSelectedItem()).toString())) {
                loadDataTableDiscountDetail(productBLL.findProductsBy(Map.of("brand_id", brand.getId())));
                return;
            }
        }
    }

    private void searchProducts() {
        if (jTextFieldSearch.getText().isEmpty()) {
            loadDataTableDiscountDetail(productBLL.getProductList());
        } else {
            loadDataTableDiscountDetail(productBLL.findProducts("name", jTextFieldSearch.getText()));
        }
    }

    public static void loadDataTableDiscount(List<Discount> discountList) {
        Object[][] discounts = new Object[0][0];
        DefaultTableModel model = (DefaultTableModel) dataTableDiscount.getModel();
        model.setRowCount(0);
        for (Discount discount : discountList) {
            discounts = Arrays.copyOf(discounts, discounts.length + 1);
            discounts[discounts.length - 1] = discount.toString().split(" \\| ");
        }
        for (Object[] object : discounts) {
            model.addRow(object);
        }
    }

    public static void loadDataTableDiscountDetail(List<Product> productList) {
        List<Integer> listProID = new ArrayList<>();
        Discount_detailBLL discountDetailBLL = new Discount_detailBLL();
        DiscountBLL discountBLL = new DiscountBLL();
        Object[][] products = new Object[0][0];
        DefaultTableModel model = (DefaultTableModel) dataTableDiscountDetail.getModel();
        DefaultTableModel modelDiscount = (DefaultTableModel) dataTableDiscount.getModel();
        int indexRow = dataTableDiscount.getSelectedRow();
        model.setRowCount(0);

        if (indexRow != -1) {
            Object[] rowData = modelDiscount.getDataVector().elementAt(indexRow).toArray();
            String[] data = new String[rowData.length];
            for (int i = 0; i < rowData.length; i++) {
                data[i] = rowData[i].toString();
            }
            String[] discount = String.join(" | ", data).split(" \\| ");
            discountSelected = discountBLL.findDiscountsBy(Map.of("id", Integer.parseInt(discount[0]))).get(0);
            for (Discount_detail discountDetail : discountDetailBLL.findDiscount_detailsBy(Map.of("discount_id", Integer.parseInt(discount[0])))) {
                listProID.add(discountDetail.getProduct_id());
            }
            for (Product product : productList) {
                Object[] objects1 = new Object[5];
                objects1[0] = product.getId();
                objects1[1] = product.getName();
                objects1[2] = product.getCost();
                if (listProID.contains(product.getId())) {
                    objects1[3] = product.getCost() - (product.getCost() * discountSelected.getPercent() / 100);
                    objects1[4] = true;
                } else {
                    objects1[3] = null;
                    objects1[4] = null;
                }

                products = Arrays.copyOf(products, products.length + 1);
                products[products.length - 1] = objects1;
            }

            for (Object[] object : products) {
                model.addRow(object);
            }
        }
        else {
            for (Product product : productList) {
                Object[] objects1 = new Object[5];
                objects1[0] = product.getId();
                objects1[1] = product.getName();
                objects1[2] = product.getCost();
                if (productIDInDiscount.contains(product.getId())) {
                    objects1[3] = product.getCost() - (product.getCost() * newDiscount.getPercent() / 100);
                    objects1[4] = true;
                } else {
                    objects1[3] = null;
                    objects1[4] = null;
                }

                products = Arrays.copyOf(products, products.length + 1);
                products[products.length - 1] = objects1;
            }

            for (Object[] object : products) {
                model.addRow(object);
            }
        }

    }
}
