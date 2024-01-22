package com.supermarket.GUI;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.supermarket.BLL.*;
import com.supermarket.DTO.*;
import com.supermarket.GUI.components.DataTable;
import com.supermarket.GUI.components.RoundedPanel;
import com.supermarket.GUI.components.RoundedScrollPane;
import com.supermarket.GUI.components.SalePanel;
import com.supermarket.utils.Date;
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

public class SaleGUI extends SalePanel {
    private final ProductBLL productBLL = new ProductBLL();
    private final CategoryBLL categoryBLL = new CategoryBLL();
    private final BrandBLL brandBLL = new BrandBLL();
    private final StaffBLL staffBLL = new StaffBLL();
//    private final CustomerBLL customerBLL = new CustomerBLL();
    private final DiscountBLL discountBLL = new DiscountBLL();
    private final Discount_detailBLL discountDetailBLL = new Discount_detailBLL();
    private final PromotionBLL promotionBLL = new PromotionBLL();
    private final Promotion_giftBLL promotionGiftBLL = new Promotion_giftBLL();
    private final Promotion_itemBLL promotionItemBLL = new Promotion_itemBLL();
    private RoundedPanel containerProduct;
    private RoundedPanel containerProductDetail;
    private RoundedPanel containerBillDetail;
    private RoundedPanel containerBill;
    private RoundedPanel containerProductsBuy;
    private RoundedScrollPane scrollPane;
    private List<RoundedPanel> listProduct;
    private List<RoundedPanel> productsBuy;
    private JLabel seachCustomer;
    private List<JLabel> productDetail;
    private List<JLabel> productImage;
    private List<JLabel> productName;
    private JComboBox cbbAttributeProduct;
    private JComboBox cbbCategory;
    private JComboBox cbbBrand;
    private JTextField jTextFieldSearch;
    private JTextField jTextFieldSearchCustomer;
    private List<JTextField> jTextFieldProductDetail;
    private List<JTextField> jTextFieldBill;
    private JButton buttonAddProduct;
    private JButton buttonCancelBill_detail;
    private JButton buttonCancelAllBill_detail;
    private JButton buttonCancelBill;
    private JButton buttonConfirm;
    private JButton buttonPay;
    private DataTable cartShopping;
    private Object[][] products;
    private Object[][] gifts;
    private List<Integer> productsInCart;
    private List<Integer> giftsInCart;
    private List<Integer> productID;
    private List<Integer> productIDBillDetail;
    private List<Double> totalBillDetail;
    private List<Double> quantityBillDetail;
    private int index = -1;
    private final Account account;
    public SaleGUI(Account account) {
        super();
        this.account = account;
        initComponents();
    }

    public void initComponents() {
        containerProduct = new RoundedPanel();
        containerProductDetail = new RoundedPanel();
        containerBillDetail = new RoundedPanel();
        containerBill = new RoundedPanel();
        containerProductsBuy = new RoundedPanel();
        listProduct = new ArrayList<>();
        seachCustomer = new JLabel("SĐT khách hàng");
        productDetail = new ArrayList<>();
        productImage = new ArrayList<>();
        productName = new ArrayList<>();
        productsBuy = new ArrayList<>();
        cbbAttributeProduct = new JComboBox(new String[] {"Tên sản phẩm", "Thương hiệu", "Thể loại"});
        cbbCategory = new JComboBox();
        cbbBrand = new JComboBox();
        jTextFieldSearch = new JTextField();
        jTextFieldSearchCustomer = new JTextField();
        jTextFieldProductDetail = new ArrayList<>();
        jTextFieldBill = new ArrayList<>();
        buttonAddProduct = new JButton("Thêm");
        buttonCancelBill_detail = new JButton("Huỷ");
        buttonCancelAllBill_detail = new JButton("Huỷ tất cả");
        buttonCancelBill = new JButton("Huỷ");
        buttonConfirm = new JButton("Xác nhận");
        buttonPay = new JButton("Thanh toán");
        products = new Object[0][0];
        gifts =  new Object[0][0];
        productsInCart = new ArrayList<>();
        giftsInCart = new ArrayList<>();
        productID = new ArrayList<>();
        totalBillDetail = new ArrayList<>();
        quantityBillDetail = new ArrayList<>();
        productIDBillDetail = new ArrayList<>();

        Thread thread = new Thread( () -> loadProduct(productBLL.getProductList()));
        thread.start();

        cbbAttributeProduct.setPreferredSize(new Dimension(130, 30));
        cbbAttributeProduct.addActionListener(e -> selectSearchFilter());
        SearchPanel.add(cbbAttributeProduct);

        for (Category category : categoryBLL.getCategoryList()) {
            cbbCategory.addItem(category.getName());
        }
        SearchPanel.add(cbbCategory);
        cbbCategory.setVisible(false);
        cbbCategory.addActionListener(e -> searchByCategory());

        for (Brand brand : brandBLL.getBrandList()) {
            cbbBrand.addItem(brand.getName());
        }
        SearchPanel.add(cbbBrand);
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
        SearchPanel.add(jTextFieldSearch);

        containerProduct.setLayout(new MigLayout("", "10[]20", "[]10[]"));
        containerProduct.setBackground(new Color(0xFFFFFF));
        containerProduct.setAutoscrolls(true);
        ProductPanel.add(containerProduct, BorderLayout.CENTER);


        containerProductDetail.setLayout(new MigLayout("", "10[][]10", "10[]20[]"));
        containerProductDetail.setBackground(new Color(0xFFFFFF));
        containerProductDetail.setAutoscrolls(true);
        Product_detailPanel.add(containerProductDetail);
        for (String string : new String[]{"Tên sản phẩm:", "Thương hiệu:", "Thể loại:", "Đơn giá:", "Mã vạch:", "Số lượng:"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("FlatLaf.style", Font.PLAIN, 16)));
            productDetail.add(label);
            containerProductDetail.add(label);

            JTextField textField = new JTextField();
            if (string.equals("Số lượng:")) {
                textField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        char c = e.getKeyChar();
                        if (!Character.isDigit(c) && c != KeyEvent.VK_PERIOD) {
                            e.consume();
                        }
                    }
                });
            } else {
                textField.setEditable(false);
            }
            textField.setPreferredSize(new Dimension(200, 50));
            textField.setFont((new Font("FlatLaf.style", Font.PLAIN, 15)));
            jTextFieldProductDetail.add(textField);
            containerProductDetail.add(textField, "wrap");
        }

        containerProductDetail.add(new JLabel());
        buttonAddProduct.setPreferredSize(new Dimension(40,40));
        buttonAddProduct.setFont(new Font("FlatLaf.style", Font.BOLD, 15));
        buttonAddProduct.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonAddProduct.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                addProductToCart();
            }
        });
        containerProductDetail.add(buttonAddProduct);

        containerBillDetail.setLayout(new BorderLayout());
        containerBillDetail.setBackground(new Color(0x93D3D3D3, true));
        Bill_detailPanel.add(containerBillDetail, BorderLayout.CENTER);

        cartShopping = new DataTable(new Object[][] {},
            new String[]{"Tên sản phẩm", "Thương hiệu", "Thể loại", "Số lượng", "Giá tiền"},
            e -> fillProductDetail());
        containerBillDetail.add(cartShopping.getTableHeader(),BorderLayout.NORTH);
        containerBillDetail.add(cartShopping);

        buttonCancelBill_detail.setFont(new Font("FlatLaf.style", Font.BOLD, 15));
        buttonCancelBill_detail.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonCancelBill_detail.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                buttonCancelBill_detail.setBackground(new Color(0xD54218));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buttonCancelBill_detail.setBackground(null);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (products.length == 0) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm.",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int indexRow = cartShopping.getSelectedRow();
                if (indexRow != -1) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm cần xoá.",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String[] options = new String[]{"Huỷ", "Xoá"};
                int choice = JOptionPane.showOptionDialog(null, "Bạn chắc chắn xoá sản phẩm đã chọn khỏi giỏ hàng?",
                    "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                if (choice == 1)
                    cancelCartShopping();
            }
        });
        ContainerButtonsBill_detail.add(buttonCancelBill_detail);

        buttonCancelAllBill_detail.setPreferredSize(new Dimension(130,30));
        buttonCancelAllBill_detail.setFont(new Font("FlatLaf.style", Font.BOLD, 15));
        buttonCancelAllBill_detail.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonCancelAllBill_detail.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                buttonCancelAllBill_detail.setBackground(new Color(0xD54218));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buttonCancelAllBill_detail.setBackground(null);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (products.length == 0) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm.",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String[] options = new String[]{"Huỷ", "Xoá"};
                int choice = JOptionPane.showOptionDialog(null, "Bạn chắc chắn xoá các sản phẩm đã chọn khỏi giỏ hàng?",
                    "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                if (choice == 1)
                    cancelAllCartShopping();
            }
        });
        ContainerButtonsBill_detail.add(buttonCancelAllBill_detail);

        buttonConfirm.setFont(new Font("FlatLaf.style", Font.BOLD, 15));
        buttonConfirm.setPreferredSize(new Dimension(130,30));
        buttonConfirm.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonConfirm.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                confirmCartShopping();
            }
        });
        ContainerButtonsBill_detail.add(buttonConfirm);

        containerBill.setLayout(new MigLayout("", "10[]15[]10", "10[]20[]"));
        containerBill.setBackground(new Color(0x93D3D3D3, true));
        BillPanel.add(containerBill, BorderLayout.CENTER);

//        seachCustomer.setFont((new Font("FlatLaf.style", Font.PLAIN, 14)));
//        containerBill.add(seachCustomer);

//        jTextFieldSearchCustomer.setPreferredSize(new Dimension(230, 30));
//        jTextFieldSearchCustomer.addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyTyped(KeyEvent e) {
//                if (!Character.isDigit(e.getKeyChar())) {
//                    e.consume();
//                }
//            }
//        });
//        jTextFieldSearchCustomer.getDocument().addDocumentListener(new DocumentListener() {
//            @Override
//            public void insertUpdate(DocumentEvent e) {
//                searchCustomerByPhone();
//            }
//
//            @Override
//            public void removeUpdate(DocumentEvent e) {
//                searchCustomerByPhone();
//            }
//
//            @Override
//            public void changedUpdate(DocumentEvent e) {
//                searchCustomerByPhone();
//            }
//        });
//        containerBill.add(jTextFieldSearchCustomer, "wrap");

        for (String string : new String[]{"Tên nhân viên:", "Ngày mua:"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("FlatLaf.style", Font.PLAIN, 16)));
            containerBill.add(label);

            JTextField textField = new JTextField();
            if (string.equals("Tên nhân viên:")) {
                textField.setText(staffBLL.findStaffsBy(Map.of("id", account.getStaffID())).get(0).getName());
                textField.setEditable(false);
                textField.setEnabled(false);
            }
            if (string.equals("Ngày mua:")) {
                textField.setText(Date.dateNow());
                textField.setEditable(false);
                textField.setEnabled(false);
            }
            textField.setPreferredSize(new Dimension(200, 50));
            textField.setFont((new Font("FlatLaf.style", Font.PLAIN, 14)));
            jTextFieldBill.add(textField);
            containerBill.add(textField, "wrap");
        }
        containerProductsBuy.setLayout(new MigLayout("", "[]", "[]10[]"));
        containerProductsBuy.setBackground(new Color(0xFFFFFF));

        scrollPane = new RoundedScrollPane(containerProductsBuy, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(350, 300));
        scrollPane.getViewport().setBackground(new Color(0x0A9CEEA, true));
        containerBill.add(scrollPane, "span 2, wrap");

        for (String string : new String[]{"Thành tiền:", "Tiền nhận:", "Tiền thừa:"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("FlatLaf.style", Font.PLAIN, 16)));
            containerBill.add(label);

            JTextField textField = new JTextField();
            if (string.equals("Thành tiền:")) {
                textField.setEditable(false);
                textField.setEnabled(false);
            }
            if (string.equals("Tiền thừa:")) {
                textField.setEditable(false);
                textField.setEnabled(false);
            } else {
                textField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        if (!Character.isDigit(e.getKeyChar())) {
                            e.consume();
                        }
                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER)
                            calculateExcess();
                    }
                });

            }

            textField.setPreferredSize(new Dimension(230, 30));
            textField.setFont((new Font("FlatLaf.style", Font.PLAIN, 15)));
            jTextFieldBill.add(textField);
            containerBill.add(textField, "wrap");
        }

        buttonCancelBill.setPreferredSize(new Dimension(40,40));
        buttonCancelBill.setFont(new Font("FlatLaf.style", Font.BOLD, 15));
        buttonCancelBill.setBackground(new Color(0xFFFFFF));
        buttonCancelBill.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonCancelBill.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                buttonCancelBill.setBackground(new Color(0xD54218));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buttonCancelBill.setBackground(new Color(0xFFFFFF));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (productsBuy.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm.",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String[] options = new String[]{"Huỷ", "Xác nhận"};
                int choice = JOptionPane.showOptionDialog(null, "Bạn muốn huỷ hoá đơn?",
                    "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                if (choice == 1)
                    cancelBill();
            }
        });
        containerBill.add(buttonCancelBill);

        buttonPay.setPreferredSize(new Dimension(40,40));
        buttonPay.setFont(new Font("FlatLaf.style", Font.BOLD, 15));
        buttonPay.setBackground(new Color(0xB58DDEAF, true));
        buttonPay.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonPay.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                String[] options = new String[]{"Huỷ", "Xác nhận"};
                int choice = JOptionPane.showOptionDialog(null, "Xác nhận lập hoá đơn?",
                    "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                if (choice == 1)
                    payBill();
            }
        });
        containerBill.add(buttonPay);

    }

    private void calculateExcess() {
        if (Objects.equals(jTextFieldBill.get(2).getText(), ""))
            return;
        double received = Double.parseDouble(jTextFieldBill.get(3).getText());
        double total = Double.parseDouble(jTextFieldBill.get(2).getText().replace(" vnđ", ""));
        double excess = received - total;
        jTextFieldBill.get(4).setText(excess + " vnđ");
    }

    private void payBill() {
        if (Objects.equals(jTextFieldBill.get(3).getText(), ""))  {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập số tiền nhận và nhấn Enter.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (jTextFieldBill.get(3).getText().contains("-"))  {
            JOptionPane.showMessageDialog(null, "Chưa đủ tiền thanh toán.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (productIDBillDetail.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Receipt receipt = new Receipt();
        ReceiptBLL receiptBLL = new ReceiptBLL();
        receipt.setId(receiptBLL.getAutoID(receiptBLL.getReceiptList()));
        receipt.setStaff_id(account.getStaffID());
//        if (Objects.equals(jTextFieldBill.get(1).getText(), ""))
//            receipt.setCustomer_id(1);
//        else {
//            List<Customer> customers = customerBLL.findCustomersBy(Map.of("phone", jTextFieldSearchCustomer.getText()));
//            if (customers.isEmpty()) {
//                if (Objects.equals(jTextFieldBill.get(3).getText(), "")) {
//                    JOptionPane.showMessageDialog(null, "Không tìm thấy khách hàng!.",
//                        "Lỗi", JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//                receipt.setCustomer_id((customers.get(0).getId()));
//            }
//        }

        try {
            receipt.setInvoice_date(Date.parseDate(jTextFieldBill.get(1).getText()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        receipt.setTotal(Double.parseDouble(jTextFieldBill.get(2).getText().replace(" vnđ", "")));
        receipt.setReceived(Double.parseDouble(jTextFieldBill.get(3).getText()));
        receipt.setExcess(Double.parseDouble(jTextFieldBill.get(4).getText().replace(" vnđ", "")));
        receiptBLL.addReceipt(receipt);

        Receipt_detailBLL receiptDetailBLL = new Receipt_detailBLL();
        for (int i = 0; i < productIDBillDetail.size(); i++) {
            Receipt_detail receiptDetail = new Receipt_detail();
            receiptDetail.setReceipt_id(receipt.getId());
            receiptDetail.setProduct_id(productIDBillDetail.get(i));
            receiptDetail.setQuantity(quantityBillDetail.get(i));
            receiptDetail.setTotal(totalBillDetail.get(i));
            receiptDetailBLL.addReceipt_detail(receiptDetail);

            Product product = productBLL.findProductsBy(Map.of("id", productIDBillDetail.get(i))).get(0);
            product.setQuantity(product.getQuantity() - quantityBillDetail.get(i));
            productBLL.updateProduct(product);
        }

        JOptionPane.showMessageDialog(null, "Tạo hoá dơn thành công.",
            "Thông báo", JOptionPane.INFORMATION_MESSAGE);

        loadProduct(productBLL.getProductList());
        cancelBill();
    }

    private void cancelBill() {
        productIDBillDetail.removeAll(productIDBillDetail);
        totalBillDetail.removeAll(totalBillDetail);
        quantityBillDetail.removeAll((quantityBillDetail));
        containerProductsBuy.removeAll();
        productsBuy.removeAll(productsBuy);
//        jTextFieldBill.get(1).setText("");
        jTextFieldBill.get(2).setText("");
        jTextFieldBill.get(3).setEditable(true);
        jTextFieldBill.get(3).setText("");
        jTextFieldBill.get(4).setEditable(true);
        jTextFieldBill.get(4).setText("");
        containerProductsBuy.repaint();
        containerProductsBuy.revalidate();
    }

    private void confirmCartShopping() {
        if (!productsBuy.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không thể thay đổi giỏ hàng!\nVui lòng thanh toán hoặc huỷ hoá đơn.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        double total = 0;
        if (productsInCart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] options = new String[]{"Huỷ", "Xác nhận"};
        int choice = JOptionPane.showOptionDialog(null, "Xác nhận giỏ hàng?\nBạn sẽ không thể thay đổi giỏ hàng.",
            "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (choice == 1) {
            for (int i = 0; i < productsInCart.size(); i++) {

                RoundedPanel panel = new RoundedPanel();
                panel.setPreferredSize(new Dimension(310, 110));
                panel.setLayout(new MigLayout("","10[]10","[][]"));
                panel.setBackground(new Color(0x93D3D3D3, true));

                JLabel label = new JLabel();
                label.setFont((new Font("FlatLaf.style", Font.BOLD, 15)));
                label.setText("<html>" + (productsBuy.size()+1) + ". " + productBLL.findProductsBy(Map.of("id", productsInCart.get(i))).get(0).getName() + "<html>");
                JLabel label1 = new JLabel();
                label1.setFont((new Font("FlatLaf.style", Font.PLAIN, 15)));
                label1.setText("Số lượng: " + products[i][3].toString());
                quantityBillDetail.add(Double.parseDouble(products[i][3].toString()));
                JLabel label2 = new JLabel();
                label2.setFont((new Font("FlatLaf.style", Font.PLAIN, 15)));
                label2.setText("Tổng cộng: " + products[i][4].toString() + " vnđ");
                totalBillDetail.add(Double.parseDouble(products[i][4].toString()));
                panel.add(label,"wrap");
                panel.add(label1, "wrap");
                panel.add(label2, "wrap");
                productIDBillDetail.add(productsInCart.get(i));

                productsBuy.add(panel);

                containerProductsBuy.add(panel, "wrap");

                total += Double.parseDouble(products[i][4].toString());
            }

            for (int i = 0; i < giftsInCart.size(); i++) {

                RoundedPanel panel = new RoundedPanel();
                panel.setPreferredSize(new Dimension(310, 110));
                panel.setLayout(new MigLayout("","10[]10","[][]"));
                panel.setBackground(new Color(0x93D3D3D3, true));

                JLabel label = new JLabel();
                label.setFont((new Font("FlatLaf.style", Font.BOLD, 15)));
                label.setText("<html>" + (productsBuy.size()+1) + ". Khuyến mãi: " + productBLL.findProductsBy(Map.of("id", giftsInCart.get(i))).get(0).getName() + "<html>");
                JLabel label1 = new JLabel();
                label1.setFont((new Font("FlatLaf.style", Font.PLAIN, 15)));
                label1.setText("Số lượng: " + gifts[i][3].toString());
                JLabel label2 = new JLabel();
                label2.setFont((new Font("FlatLaf.style", Font.PLAIN, 15)));
                label2.setText("Tổng cộng: " + gifts[i][4].toString() + " vnđ");
                panel.add(label,"wrap");
                panel.add(label1, "wrap");
                panel.add(label2, "wrap");

                if (productIDBillDetail.contains(giftsInCart.get(i))) {
                    int indexProID = productIDBillDetail.indexOf(giftsInCart.get(i));
                    double quantity = quantityBillDetail.get(indexProID) + Double.parseDouble(gifts[i][3].toString());
                    quantityBillDetail.set(indexProID, quantity);
                } else {
                    productIDBillDetail.add(giftsInCart.get(i));
                    quantityBillDetail.add(Double.parseDouble(gifts[i][3].toString()));
                    totalBillDetail.add(Double.parseDouble(gifts[i][4].toString()));
                }
                productsBuy.add(panel);

                containerProductsBuy.add(panel, "wrap");
            }

            jTextFieldBill.get(2).setText(total + " vnđ");
        }
        cancelAllCartShopping();
    }

//    private void searchCustomerByPhone() {
//        if (jTextFieldSearchCustomer.getText().isEmpty())
//            return;
//        List<Customer> customers = customerBLL.findCustomersBy(Map.of("phone", jTextFieldSearchCustomer.getText()));
//        if (!customers.isEmpty())
//            jTextFieldBill.get(1).setText(customers.get(0).getName());
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
                loadProduct(productBLL.findProductsBy(Map.of("category_id", category.getId())));
                return;
            }
        }
    }

    private void searchByBrand() {
        for (Brand brand : brandBLL.getBrandList()) {
            if (brand.getName().equals(Objects.requireNonNull(cbbBrand.getSelectedItem()).toString())) {
                loadProduct(productBLL.findProductsBy(Map.of("brand_id", brand.getId())));
                return;
            }
        }
    }

    private void searchProducts() {
        if (jTextFieldSearch.getText().isEmpty()) {
            loadProduct(productBLL.getProductList());
        } else {
            loadProduct(productBLL.findProducts("name", jTextFieldSearch.getText()));
        }
    }

    private void fillProductDetail() {
        DefaultTableModel model = (DefaultTableModel) cartShopping.getModel();
        int indexRow = cartShopping.getSelectedRow();
        if (indexRow < products.length) {
            Object[] rowData = model.getDataVector().elementAt(indexRow).toArray();
            String[] data = new String[rowData.length];
            for (int i = 0; i < rowData.length; i++) {
                data[i] = rowData[i].toString();
            }
            String[] product = String.join(" | ", data).split(" \\| ");
            Product selectedProduct = productBLL.findProductsBy(Map.of("id", productsInCart.get(indexRow))).get(0);
            loadProductDetail(selectedProduct);

            jTextFieldProductDetail.get(5).setText(product[3]);
            index = productsInCart.get(indexRow);
        }

    }

    private void cancelAllCartShopping() {
        productsInCart.removeAll(productsInCart);
        giftsInCart.removeAll(giftsInCart);
        products = Arrays.copyOf(new Object[0][0], 0);
        gifts = Arrays.copyOf(new Object[0][0], 0);
        index = -1;
        jTextFieldProductDetail.get(0).setText("");
        jTextFieldProductDetail.get(1).setText("");
        jTextFieldProductDetail.get(2).setText("");
        jTextFieldProductDetail.get(3).setText("");
        jTextFieldProductDetail.get(4).setText("");
        jTextFieldProductDetail.get(5).setText("");
        loadCartShopping(products);
    }
    private void cancelCartShopping() {
        int indexRow = cartShopping.getSelectedRow();
        productsInCart.remove(indexRow);
        for (int i = indexRow; i < products.length - 1; i++) {
            products[i] = products[i+1];
        }
        products = Arrays.copyOf(products, products.length-1);
        index = -1;
        jTextFieldProductDetail.get(0).setText("");
        jTextFieldProductDetail.get(1).setText("");
        jTextFieldProductDetail.get(2).setText("");
        jTextFieldProductDetail.get(3).setText("");
        jTextFieldProductDetail.get(4).setText("");
        jTextFieldProductDetail.get(5).setText("");
        loadCartShopping(products);
    }

    private void addProductToCart() {
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Double.parseDouble(jTextFieldProductDetail.get(5).getText());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập lại số lượng sản phẩm.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (productBLL.findProductsBy(Map.of("id", index)).get(0).getQuantity() < Double.parseDouble(jTextFieldProductDetail.get(5).getText())) {
            JOptionPane.showMessageDialog(this, "Số lượng sản phẩm trên kệ không đủ.\nVui lòng nhập thêm sản phẩm.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (productsInCart.contains(index)) {
            int indexRow = productsInCart.indexOf(index);
            for (int i = 0; i < 3; i++) {
                products[indexRow][i] = jTextFieldProductDetail.get(i).getText();
            }
            products[indexRow][3] = Double.parseDouble(jTextFieldProductDetail.get(5).getText());
            products[indexRow][4] = Double.parseDouble(jTextFieldProductDetail.get(5).getText()) * Double.parseDouble(jTextFieldProductDetail.get(3).getText());
        }
        else {
            Object[] objects = new Object[5];
            for (int i = 0; i < 3; i++) {
                objects[i] = jTextFieldProductDetail.get(i).getText();
            }
            try {
                objects[3] = Double.parseDouble(jTextFieldProductDetail.get(5).getText());

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập lại số lượng sản phẩm.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            objects[4] = Double.parseDouble(jTextFieldProductDetail.get(5).getText()) * Double.parseDouble(jTextFieldProductDetail.get(3).getText());
            products = Arrays.copyOf(products, products.length + 1);
            products[products.length - 1] = objects;
            productsInCart.add(index);
        }
        Object[] objects;
        gifts = new Object[0][0];
//        for (Promotion promotion : promotionBLL.getPromotionList()) {
//            int min = 1000;
//            List<Integer> listItemInCart = new ArrayList<>();
//            List<Promotion_item> promotionItems = promotionItemBLL.findPromotion_itemsBy(Map.of("promotion_id", promotion.getId()));
//            List<Promotion_gift> promotionGifts = promotionGiftBLL.findPromotion_giftsBy(Map.of("promotion_id", promotion.getId()));
//            for (Promotion_item promotionItem : promotionItems) {
//                if (productsInCart.contains(promotionItem.getProduct_id())) {
//                    int indexRow = productsInCart.indexOf(promotionItem.getProduct_id());
//                    double quantity = Double.parseDouble(products[indexRow][3].toString());
//                    if (quantity >= promotionItem.getQuantity()) {
//                        listItemInCart.add(promotionItem.getProduct_id());
//                        int quantityGift = (int) quantity / (int) promotionItem.getQuantity();
//                        if (quantityGift < min)
//                            min = quantityGift;
//                    }
//                }
//            }
//
//            int i = 0;
//            if (listItemInCart.size() == promotionItems.size() ) {
//                for (Promotion_gift promotionGift : promotionGifts) {
//                    Product product = productBLL.findProductsBy(Map.of("id", promotionGift.getProduct_id())).get(0);
//                    if (product.getQuantity() < promotionGift.getQuantity() * min) {
//                        JOptionPane.showMessageDialog(this, "Sản phẩm " + product.getName() + " không đủ để khuyến mãi.",
//                            "Lỗi", JOptionPane.ERROR_MESSAGE);
//                        loadCartShopping(products);
//                        jTextFieldProductDetail.get(0).setText("");
//                        jTextFieldProductDetail.get(1).setText("");
//                        jTextFieldProductDetail.get(2).setText("");
//                        jTextFieldProductDetail.get(3).setText("");
//                        jTextFieldProductDetail.get(4).setText("");
//                        jTextFieldProductDetail.get(5).setText("");
//                        index = -1;
//                        return;
//                    }
//                    objects = new Object[5];
//                    objects[0] = "Khuyến mãi: " + product.getName();
//                    objects[1] = brandBLL.findBrandsBy(Map.of("id", product.getBrand_id())).get(0).getName();
//                    objects[2] = categoryBLL.findCategoriesBy(Map.of("id", product.getCategory_id())).get(0).getName();
//                    objects[3] = promotionGift.getQuantity() * min;
//                    objects[4] = 0.0;
//                    gifts = Arrays.copyOf(gifts, gifts.length + 1);
//                    gifts[gifts.length - 1] = objects;
//                    giftsInCart.add(promotionGift.getProduct_id());
//                    i += 1;
//                }
//            }
//
//        }

        loadCartShopping(products);
        jTextFieldProductDetail.get(0).setText("");
        jTextFieldProductDetail.get(1).setText("");
        jTextFieldProductDetail.get(2).setText("");
        jTextFieldProductDetail.get(3).setText("");
        jTextFieldProductDetail.get(4).setText("");
        jTextFieldProductDetail.get(5).setText("");
        index = -1;
    }

    public void loadProduct (List<Product> products) {
        ProductPanel.removeAll();
        containerProduct.removeAll();
        listProduct.removeAll(listProduct);
        productImage.removeAll(productImage);
        productName.removeAll(productName);
        productID.removeAll(productID);
        for (Product product : products) {
            RoundedPanel panel = new RoundedPanel();
            panel.setLayout(new MigLayout("", "10[]30[]10", "[][]"));
            panel.setBackground(new Color(0xFFBDD2DB, true));
            panel.setPreferredSize(new Dimension(360, 130));
            panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            listProduct.add(panel);

            JLabel label = new JLabel();
            label.setIcon(new FlatSVGIcon(product.getImage()));
            label.scrollRectToVisible(new Rectangle());
            productImage.add(label);

            JLabel label1 = new JLabel();
            label1.setFont((new Font("FlatLaf.style", Font.PLAIN, 15)));
            label1.setText("<html>" + product.getName() + "<html><br>SL: " + product.getQuantity());
            productName.add(label1);

            panel.add(label);
            panel.add(label1, "wrap");

            productID.add(product.getId());
        }

        for (RoundedPanel panel : listProduct) {
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    for (int i = 0;  i < listProduct.size(); i++) {
                        if (listProduct.get(i) == e.getComponent()) {
                            index = productID.get(i);
                            loadProductDetail(productBLL.findProductsBy(Map.of("id", index)).get(0));
                        }
                    }
                }
            });
        }
        for (RoundedPanel panel : listProduct) {
            containerProduct.add(panel, "wrap");
        }
        containerProduct.repaint();
        containerProduct.revalidate();
        ProductPanel.add(containerProduct);
        ProductPanel.repaint();
        ProductPanel.revalidate();
    }

    public void loadProductDetail(Product product) {
        List<Discount_detail> discountDetails = new ArrayList<>();

        for (Discount discount : discountBLL.getDiscountList()){
            if (!discount.isStatus()) {
                for (Discount_detail discountDetail : discountDetailBLL.findDiscount_detailsBy(Map.of("discount_id", discount.getId()))) {
                    if (discountDetail.getProduct_id() == product.getId())
                        discountDetails.add(discountDetail);
                }
            }
        }

        jTextFieldProductDetail.get(0).setText(product.getName());
        jTextFieldProductDetail.get(0).setCaretPosition(0);
        jTextFieldProductDetail.get(1).setText(brandBLL.findBrandsBy(Map.of("id", product.getBrand_id())).get(0).getName());
        if (!discountDetails.isEmpty()) {
            Discount discount = discountBLL.findDiscountsBy(Map.of("id", discountDetails.get(0).getDiscount_id())).get(0);
            jTextFieldProductDetail.get(3).setText(String.valueOf(product.getCost() - (product.getCost() * discount.getPercent() / 100)));
        } else {
            jTextFieldProductDetail.get(3).setText(String.valueOf(product.getCost()));
        }
        jTextFieldProductDetail.get(2).setText(categoryBLL.findCategoriesBy(Map.of("id", product.getCategory_id())).get(0).getName());
        jTextFieldProductDetail.get(4).setText(product.getBarcode());
        jTextFieldProductDetail.get(5).setText("0.0");
    }

    public void loadCartShopping(Object[][] objects) {
        DefaultTableModel model = (DefaultTableModel) cartShopping.getModel();
        model.setRowCount(0);
        for (Object[] object : objects) {
            model.addRow(object);
        }
        if (gifts.length > 0) {
            for (Object[] object : gifts) {
                model.addRow(object);
            }
        }
    }

}
