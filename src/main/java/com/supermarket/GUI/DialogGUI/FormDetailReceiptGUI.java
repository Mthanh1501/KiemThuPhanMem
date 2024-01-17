package com.supermarket.GUI.DialogGUI;

import com.supermarket.BLL.ProductBLL;
import com.supermarket.BLL.Receipt_detailBLL;
import com.supermarket.BLL.StaffBLL;
import com.supermarket.DTO.Product;
import com.supermarket.DTO.Receipt;
import com.supermarket.DTO.Receipt_detail;
import com.supermarket.DTO.Staff;
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

public class FormDetailReceiptGUI extends DialogForm{
    private Receipt receipt;
    private DataTable dataTable;
    private RoundedPanel formDetail;
    private RoundedScrollPane scrollPaneDatatable;
    private RoundedScrollPane scrollPaneReceiptDetail;
    private java.util.List<JLabel> attributeProduct;
    private List<JTextField> jTextFieldProduct;
    public FormDetailReceiptGUI(Receipt receipt) {
        super();
        this.receipt = receipt;
        super.setTitle("Quản lý hoá đơn");
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
        scrollPaneReceiptDetail = new RoundedScrollPane(formDetail, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scrollPaneDatatable.setPreferredSize(new Dimension(600, 700));
        leftContent.add(scrollPaneDatatable, BorderLayout.CENTER);
        containerTable.add(dataTable);

        scrollPaneReceiptDetail.setPreferredSize(new Dimension(670, 610));
        rightContent.add(scrollPaneReceiptDetail, BorderLayout.NORTH);

        formDetail.setBackground(new Color(0xFFBDD2DB));
        formDetail.setLayout(new MigLayout("", "50[]20[]10", "20[]20[]"));

        for (String string : new String[] {"Mã hoá đơn:", "Mã nhân viên:", "Ngày lập:", "Tổng tiền:", "Tiền nhận:", "Tiền thừa:"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("FlatLaf.style", Font.PLAIN, 16)));
            attributeProduct.add(label);
            formDetail.add(label);

            JTextField textField = new JTextField();
            if (string.equals("Mã hoá đơn:")) {
                textField.setText(String.valueOf(receipt.getId()));
                textField.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        loadTableReceipt_Detail();
                    }
                });
            }
            if (string.equals("Mã nhân viên:")) {
                textField.setText(String.valueOf(receipt.getStaff_id()));
                textField.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        loadTableStaff();
                    }
                });
            }
//            if (string.equals("Mã khách hàng:")) {
//                textField.setText(String.valueOf(receipt.getCustomer_id()));
//                textField.addMouseListener(new MouseAdapter() {
//                    @Override
//                    public void mousePressed(MouseEvent e) {
//                        loadTableCustomer();
//                    }
//                });
//            }
            if (string.equals("Ngày lập:")) {
                textField.setText(String.valueOf(receipt.getInvoice_date()));
                textField.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        loadTableReceipt_Detail();
                    }
                });
            }
            if (string.equals("Tổng tiền:")) {
                textField.setText(String.valueOf(receipt.getTotal()));
                textField.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        loadTableReceipt_Detail();
                    }
                });
            }
            if (string.equals("Tiền nhận:")) {
                textField.setText(String.valueOf(receipt.getReceived()));
                textField.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        loadTableReceipt_Detail();
                    }
                });
            }
            if (string.equals("Tiền thừa:")) {
                textField.setText(String.valueOf(receipt.getExcess()));
                textField.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        loadTableReceipt_Detail();
                    }
                });
            }
            textField.setEditable(false);
            textField.setPreferredSize(new Dimension(400, 50));
            textField.setFont((new Font("FlatLaf.style", Font.PLAIN, 14)));
            jTextFieldProduct.add(textField);
            formDetail.add(textField, "wrap");
        }
        loadTableReceipt_Detail();
    }

    private void loadTableReceipt_Detail() {
        Object[][] receiptDetailList = new Object[0][0];
        Receipt_detailBLL receiptDetailBLL = new Receipt_detailBLL();
        ProductBLL productBLL = new ProductBLL();
        for (Receipt_detail receiptDetail : receiptDetailBLL.getReceipt_detailList()) {
            if (receiptDetail.getReceipt_id() == receipt.getId()) {
                Object[] object = new Object[3];
                Product product = productBLL.findProductsBy(Map.of("id", receiptDetail.getProduct_id())).get(0);
                object[0] = product.getName();
                object[1] = receiptDetail.getQuantity();
                object[2] = receiptDetail.getTotal();
                receiptDetailList = Arrays.copyOf(receiptDetailList, receiptDetailList.length + 1);
                receiptDetailList[receiptDetailList.length - 1] = object;
            }

        }

        dataTable = new DataTable(receiptDetailList, new String[] {"Tên sản phẩm", "Số lượng", "Tổng tiền"}, e -> {});
        containerTable.removeAll();
        containerTable.add(dataTable.getTableHeader(), BorderLayout.NORTH);
        containerTable.add(dataTable, BorderLayout.CENTER);

        containerTable.repaint();
        containerTable.revalidate();
        scrollPaneDatatable.setViewportView(containerTable);
    }

    private void loadTableStaff() {
        StaffBLL staffBLL = new StaffBLL();
        dataTable = new DataTable(staffBLL.getData(), new String[] {"Mã nhân viên", "Họ tên", "Giới tính", "Ngày sinh", "Điện thoại", "Địa chỉ", "Email", "Ngày vào làm"}, e -> {});
        dataTable.setPreferredSize(new Dimension(1500, 700));
        containerTable.removeAll();
        containerTable.add(dataTable.getTableHeader(), BorderLayout.NORTH);
        containerTable.add(dataTable, BorderLayout.CENTER);

        Staff staff = staffBLL.findStaffsBy(Map.of("id", receipt.getStaff_id())).get(0);
        int index = staffBLL.getIndex(staff, "id", staffBLL.getStaffList());
        dataTable.setRowSelectionInterval(index, index);

        containerTable.repaint();
        containerTable.revalidate();
        scrollPaneDatatable.setViewportView(containerTable);
    }

//    private void loadTableCustomer() {
//        CustomerBLL staffBLL = new CustomerBLL();
//        dataTable = new DataTable(staffBLL.getData(), new String[] {"Mã khách hàng", "Tên khách hàng", "Giới tính", "Ngày sinh", "Số điện thoại", "Thành viên","Lần đăng nhập cuối","Điểm thưởng"}, e -> {});
//        dataTable.setPreferredSize(new Dimension(1500, 700));
//        containerTable.removeAll();
//        containerTable.add(dataTable.getTableHeader(), BorderLayout.NORTH);
//        containerTable.add(dataTable, BorderLayout.CENTER);
//
//        Customer staff = staffBLL.findCustomersBy(Map.of("id", receipt.getCustomer_id())).get(0);
//        int index = staffBLL.getIndex(staff, "id", staffBLL.getCustomerList());
//        dataTable.setRowSelectionInterval(index, index);
//
//        containerTable.repaint();
//        containerTable.revalidate();
//        scrollPaneDatatable.setViewportView(containerTable);
//    }
}
