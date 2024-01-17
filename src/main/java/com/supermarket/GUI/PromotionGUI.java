package com.supermarket.GUI;

import com.supermarket.BLL.BrandBLL;
import com.supermarket.BLL.CategoryBLL;
import com.supermarket.BLL.ProductBLL;
import com.supermarket.BLL.PromotionBLL;
import com.supermarket.DTO.*;
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

public class PromotionGUI extends Layout3 {
    private PromotionBLL promotionBLL = new PromotionBLL();
//    private Promotion_detailBLL promotion_detailBLL = new Promotion_detailBLL();
    private ProductBLL productBLL = new ProductBLL();
    private final CategoryBLL categoryBLL = new CategoryBLL();
    private final BrandBLL brandBLL = new BrandBLL();
    private static DataTable dataTablePromotion;
    private static DataTable dataTablePromotionDetail;
    private RoundedPanel searchPanel;
    private RoundedPanel containerPromotion;
    private RoundedScrollPane scrollPanePromotion;
    private RoundedScrollPane scrollPanePromotionDetail;
    private JTextField jTextFieldSearch;
    private List<JTextField> jTextFieldPromotion;
    private JDateChooser[] jDateChooser;
    private JTextField[] dateTextField;
    private JTextField[] jTextFields;
    private JComboBox cbbAttributeProduct;
    private JComboBox cbbCategory;
    private JComboBox cbbBrand;
    private JComboBox cbbStatus;
    private static Promotion promotionSelected;
    private static Promotion newPromotion = null;
    private JButton buttonAdd;
    private JButton buttonCancel;
    private static List<Integer> productIDInPromotion;
    public PromotionGUI(List<Function> functions) {
        super();
        init(functions);

    }

    public void init(List<Function> functions) {
        containerPromotion = new RoundedPanel();
        jTextFieldSearch = new JTextField();
        jTextFieldPromotion = new ArrayList<>();
        cbbAttributeProduct = new JComboBox(new String[] {"Tên sản phẩm", "Thương hiệu", "Thể loại"});
        cbbCategory = new JComboBox();
        cbbBrand = new JComboBox();
        cbbStatus = new JComboBox(new String[] {"Đang áp dụng", "Ngừng áp dụng"});
        searchPanel = new RoundedPanel();
        buttonAdd = new JButton("Thêm");
        buttonCancel = new JButton("Huỷ");
        productIDInPromotion = new ArrayList<>();
        dataTablePromotion = new DataTable(new Object[][] {},
            new String[] {"Mã đợt khuyến mãi", "Ngày bắt đầu", "Ngày kết thúc", "Trạng thái"},
            e -> {
//                List<Product> products = new ArrayList<>();
//                DefaultTableModel modelPromotion = (DefaultTableModel) dataTablePromotion.getModel();
//                int indexRow = dataTablePromotion.getSelectedRow();
//                if (indexRow != -1) {
//                    Object[] rowData = modelPromotion.getDataVector().elementAt(indexRow).toArray();
//                    String[] data = new String[rowData.length];
//                    for (int i = 0; i < rowData.length; i++) {
//                        data[i] = rowData[i].toString();
//                    }
//                    String[] promotion = String.join(" | ", data).split(" \\| ");
//                    promotionSelected = promotionBLL.findPromotionsBy(Map.of("id", Integer.parseInt(promotion[0]))).get(0);
//                    for (Promotion_detail promotionDetail : promotion_detailBLL.findPromotion_detailsBy(Map.of("promotion_id", promotionSelected.getId()))) {
//                        products.add(productBLL.findProductsBy(Map.of("id", promotionDetail.getProduct_id())).get(0));
//                    }
//                }
//                loadDataTablePromotionDetail(products);
            });
        scrollPanePromotion = new RoundedScrollPane(dataTablePromotion, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        dataTablePromotionDetail = new DataTable(new Object[][] {},
            new String[] {"Mã sản phẩm", "Tên sản phẩm", "Giá cũ", "Giá mới", ""}, e -> {}, 4);
        scrollPanePromotionDetail = new RoundedScrollPane(dataTablePromotionDetail, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

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

        topContent.add(scrollPanePromotion);
        loadDataTablePromotion(promotionBLL.searchPromotions());

        bottomContent.add(scrollPanePromotionDetail, BorderLayout.CENTER);
        loadDataTablePromotionDetail(productBLL.getProductList());

        containerPromotion.setLayout(new MigLayout("", "10[]15[]10", "10[]20[]"));
        containerPromotion.setBackground(new Color(0xFFBDD2DB, true));
        rightContent.add(containerPromotion, BorderLayout.CENTER);

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
            int index = i;
            dateTextField[i].addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    try {
                        Date day = Date.parseDate(dateTextField[index].getText());
                        jDateChooser[index].setDate(day.toJDate());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Ngày không hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        e.consume();
                    }
                }
            });
        }

        for (String string : new String[]{"Mã đợt KM:", "Ngày BĐ:", "Ngày KT:", "Trạng thái:"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("FlatLaf.style", Font.PLAIN, 16)));
            containerPromotion.add(label);

            JTextField textField = new JTextField();
            if (string.equals("Mã đợt KM:")) {
                textField.setText(String.valueOf(promotionBLL.getAutoID(promotionBLL.searchPromotions())));
                textField.setEditable(false);
                textField.setEnabled(false);
            }
            if (string.equals("Ngày BĐ:")) {
                containerPromotion.add(jDateChooser[0], "wrap");
                continue;
            }

            if (string.equals("Ngày KT:")) {
                containerPromotion.add(jDateChooser[1], "wrap");
                continue;
            }

            if (string.equals("Trạng thái:")) {
                cbbStatus.setPreferredSize(new Dimension(200, 50));
                containerPromotion.add(cbbStatus, "wrap");
                cbbStatus.setEnabled(false);
                continue;
            }
            textField.setPreferredSize(new Dimension(200, 50));
            textField.setFont((new Font("FlatLaf.style", Font.PLAIN, 14)));
            jTextFieldPromotion.add(textField);
            containerPromotion.add(textField, "wrap");
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
        containerPromotion.add(buttonCancel);

        buttonAdd.setPreferredSize(new Dimension(40,40));
        buttonAdd.setFont(new Font("FlatLaf.style", Font.BOLD, 15));
        buttonAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                addPromotion();
            }
        });
        containerPromotion.add(buttonAdd, "wrap");
    }

    private void addPromotion() {
//        for (int i = 0; i < jDateChooser.length; i++) {
//            if (jDateChooser[i].getDate() == null) {
//                JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin đợt khuyến mãi.",
//                    "Lỗi", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//        }
//        if (jTextFieldPromotion.get(1).getText().isEmpty()) {
//            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin đợt khuyến mãi.",
//                "Lỗi", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//        Date startDate = null;
//        try {
//            startDate = new Date(jDateChooser[0].getDate());
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, "Lỗi định dạng ngày.",
//                "Lỗi", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//        Date endDate = null;
//        try {
//            endDate = new Date(jDateChooser[1].getDate());
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, "Lỗi định dạng ngày.",
//                "Lỗi", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//        if (startDate.isAfter(endDate)) {
//            JOptionPane.showMessageDialog(this, "Ngày bắt đầu phải trước ngày kết thúc", "Lỗi", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//        if (productIDInPromotion.isEmpty()) {
//            int id = Integer.parseInt(jTextFieldPromotion.get(0).getText());
//            double percent = Double.parseDouble(jTextFieldPromotion.get(1).getText());
//            loadDataTablePromotion(promotionBLL.searchPromotions());
//            loadDataTablePromotionDetail(productBLL.getProductList());
//            newPromotion = new Promotion(id, percent, startDate, endDate, false);
//            JOptionPane.showMessageDialog(this, "Vui lòng thêm sản phẩm vào đợt giảm giá", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//        } else {
//            String[] options = new String[]{"Huỷ", "Xác nhận"};
//            int choice = JOptionPane.showOptionDialog(null, "Xác nhận thêm đợt giảm giá?",
//                "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
//            if (choice == 1) {
//                if (promotionBLL.addPromotion(newPromotion)) {
//                    JOptionPane.showMessageDialog(null, "Thêm đợt giảm giá thành công!",
//                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//
//                    for (Promotion promotion : promotionBLL.getPromotionList()) {
//                        if (promotion.getId() != newPromotion.getId())
//                            promotion.setStatus(true);
//                        promotionBLL.updatePromotion(promotion);
//                    }
//
//                    for (Integer i : productIDInPromotion) {
//                        Promotion_detail promotionDetail = new Promotion_detail();
//                        promotionDetail.setPromotion_id(newPromotion.getId());
//                        promotionDetail.setProduct_id(i);
//                        promotionDetail.setStatus(false);
//                        promotion_detailBLL.addPromotion_detail(promotionDetail);
//                    }
//                    productIDInPromotion.removeAll(productIDInPromotion);
//                    refresh();
//                } else {
//                    JOptionPane.showMessageDialog(null, "Thêm đợt giảm giá không thành công!",
//                        "Lỗi", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        }

    }

    private void refresh() {
        jTextFieldPromotion.get(0).setEditable(true);
        jTextFieldPromotion.get(0).setText(String.valueOf(promotionBLL.getAutoID(promotionBLL.searchPromotions())));
        jTextFieldPromotion.get(0).setEditable(false);
        jTextFieldPromotion.get(0).setEnabled(false);
        jTextFieldPromotion.get(1).setText("");
        jDateChooser[0].setDate(null);
        jDateChooser[1].setDate(null);
        loadDataTablePromotion(promotionBLL.searchPromotions());
        loadDataTablePromotionDetail(productBLL.getProductList());
        newPromotion = null;
    }

//    private void fillForm() {
//        if (newPromotion != null && dataTablePromotion.getSelectedRow() == -1) {
//            DefaultTableModel model = (DefaultTableModel) dataTablePromotionDetail.getModel();
//            Object[] rowData = model.getDataVector().elementAt(dataTablePromotionDetail.getSelectedRow()).toArray();
//            String[] data = new String[rowData.length];
//            for (int i = 0; i < rowData.length; i++) {
//                if (rowData[i] != null)
//                    data[i] = rowData[i].toString();
//            }
//            String[] product = String.join(" | ", data).split(" \\| ");
//            Product product1 = productBLL.findProductsBy(Map.of("id", Integer.parseInt(product[0]))).get(0);
//            if (data[4] == null) {
//                productIDInPromotion.add(product1.getId());
//                double newPrice = product1.getCost() - (product1.getCost() * newPromotion.getPercent() / 100);
//                model.setValueAt(newPrice, dataTablePromotionDetail.getSelectedRow(), 3);
//                model.setValueAt(true, dataTablePromotionDetail.getSelectedRow(), 4);
//            } else {
//                productIDInPromotion.remove((Object)product1.getId());
//                model.setValueAt(null, dataTablePromotionDetail.getSelectedRow(), 3);
//                model.setValueAt(null, dataTablePromotionDetail.getSelectedRow(), 4);
//            }
//        }
//    }

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
                loadDataTablePromotionDetail(productBLL.findProductsBy(Map.of("category_id", category.getId())));
                return;
            }
        }
    }

    private void searchByBrand() {
        for (Brand brand : brandBLL.getBrandList()) {
            if (brand.getName().equals(Objects.requireNonNull(cbbBrand.getSelectedItem()).toString())) {
                loadDataTablePromotionDetail(productBLL.findProductsBy(Map.of("brand_id", brand.getId())));
                return;
            }
        }
    }

    private void searchProducts() {
        if (jTextFieldSearch.getText().isEmpty()) {
            loadDataTablePromotionDetail(productBLL.getProductList());
        } else {
            loadDataTablePromotionDetail(productBLL.findProducts("name", jTextFieldSearch.getText()));
        }
    }

    public static void loadDataTablePromotion(List<Promotion> promotionList) {
        Object[][] promotions = new Object[0][0];
        DefaultTableModel model = (DefaultTableModel) dataTablePromotion.getModel();
        model.setRowCount(0);
        for (Promotion promotion : promotionList) {
            promotions = Arrays.copyOf(promotions, promotions.length + 1);
            promotions[promotions.length - 1] = promotion.toString().split(" \\| ");
        }
        for (Object[] object : promotions) {
            model.addRow(object);
        }
    }

    public static void loadDataTablePromotionDetail(List<Product> productList) {
//        List<Integer> listProID = new ArrayList<>();
//        Promotion_detailBLL promotionDetailBLL = new Promotion_detailBLL();
//        PromotionBLL promotionBLL = new PromotionBLL();
//        Object[][] products = new Object[0][0];
//        DefaultTableModel model = (DefaultTableModel) dataTablePromotionDetail.getModel();
//        DefaultTableModel modelPromotion = (DefaultTableModel) dataTablePromotion.getModel();
//        int indexRow = dataTablePromotion.getSelectedRow();
//        model.setRowCount(0);
//
//        if (indexRow != -1) {
//            Object[] rowData = modelPromotion.getDataVector().elementAt(indexRow).toArray();
//            String[] data = new String[rowData.length];
//            for (int i = 0; i < rowData.length; i++) {
//                data[i] = rowData[i].toString();
//            }
//            String[] promotion = String.join(" | ", data).split(" \\| ");
//            promotionSelected = promotionBLL.findPromotionsBy(Map.of("id", Integer.parseInt(promotion[0]))).get(0);
//            for (Promotion_detail promotionDetail : promotionDetailBLL.findPromotion_detailsBy(Map.of("promotion_id", Integer.parseInt(promotion[0])))) {
//                listProID.add(promotionDetail.getProduct_id());
//            }
//            for (Product product : productList) {
//                Object[] objects1 = new Object[5];
//                objects1[0] = product.getId();
//                objects1[1] = product.getName();
//                objects1[2] = product.getCost();
//                if (listProID.contains(product.getId())) {
//                    objects1[3] = product.getCost() - (product.getCost() * promotionSelected.getPercent() / 100);
//                    objects1[4] = true;
//                } else {
//                    objects1[3] = null;
//                    objects1[4] = null;
//                }
//
//                products = Arrays.copyOf(products, products.length + 1);
//                products[products.length - 1] = objects1;
//            }
//
//            for (Object[] object : products) {
//                model.addRow(object);
//            }
//        }
//        else {
//            for (Product product : productList) {
//                Object[] objects1 = new Object[5];
//                objects1[0] = product.getId();
//                objects1[1] = product.getName();
//                objects1[2] = product.getCost();
//                if (productIDInPromotion.contains(product.getId())) {
//                    objects1[3] = product.getCost() - (product.getCost() * newPromotion.getPercent() / 100);
//                    objects1[4] = true;
//                } else {
//                    objects1[3] = null;
//                    objects1[4] = null;
//                }
//
//                products = Arrays.copyOf(products, products.length + 1);
//                products[products.length - 1] = objects1;
//            }
//
//            for (Object[] object : products) {
//                model.addRow(object);
//            }
//        }

    }
}
