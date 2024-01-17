package com.supermarket.GUI;

import com.formdev.flatlaf.extras.FlatSVGIcon;
//import com.supermarket.BLL.CustomerBLL;
import com.supermarket.BLL.ReceiptBLL;
import com.supermarket.BLL.StaffBLL;
import com.supermarket.DTO.Function;
import com.supermarket.DTO.Receipt;
import com.supermarket.DTO.Staff;
import com.supermarket.GUI.DialogGUI.FormDetailReceiptGUI;
import com.supermarket.GUI.components.DataTable;
import com.supermarket.GUI.components.Layout2;
import com.supermarket.GUI.components.RoundedScrollPane;
import com.supermarket.utils.Date;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.*;

public class ReceiptGUI extends Layout2 {
    private ReceiptBLL receiptBLL = new ReceiptBLL();
    private StaffBLL staffBLL = new StaffBLL();
//    private CustomerBLL customerBLL = new CustomerBLL();
    private static DataTable dataTable;
    private RoundedScrollPane scrollPane;
    private JLabel iconDetail;
    private JComboBox cbbSort ;
    private List<JTextField> jTextFieldSearch;
    private JDateChooser[] jDateChooser;
    private JTextField[] jTextFields;
    private JTextField[] dateTextField;
    private static Object[][] receiptList;
    private static List<Receipt> receipts;
    public ReceiptGUI(List<Function> functions) {
        super();
        initComponent(functions);
    }

    private void initComponent(List<Function> functions) {
        jTextFieldSearch = new ArrayList<>();
        receiptList = new Object[0][0];
        receipts = new ArrayList<>();
        dataTable = new DataTable(new Object[][]{}, new String[]{"Mã hoá đơn","Tên nhân viên", "Ngày lập", "Tổng tiền"}, e -> {});
        scrollPane = new RoundedScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        rightContent.add(scrollPane, BorderLayout.CENTER);

        iconDetail = new JLabel();
        cbbSort = new JComboBox(new String[] {"Sắp xếp theo ngày lập", "Sắp xếp theo tổng tiền tăng dần", "Sắp xếp theo tổng tiền giảm dần"});

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
            dateTextField[i].getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    search();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    search();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    search();
                }
            });
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

        if (functions.stream().anyMatch(f -> f.getName().equals("Chi tiết"))) {
            iconDetail.setIcon(new FlatSVGIcon("icon/detail.svg"));
            iconDetail.setCursor(new Cursor(Cursor.HAND_CURSOR));
            iconDetail.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    showDetailReceipt();
                }
            });
            leftMenu.add(iconDetail);
        }

        cbbSort.setPreferredSize(new Dimension(230, 30));
        cbbSort.addActionListener(e -> selectSortFilter());
        cbbSort.setSelectedIndex(0);
        selectSortFilter();
        rightMenu.add(cbbSort);

        for (String string : new String[] {"Tên nhân viên:", "Từ ngày:", "Đến ngày:"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("FlatLaf.style", Font.PLAIN, 16)));
            leftContent.add(label);

            JTextField textField = new JTextField();
            if (string.equals("Từ ngày:")) {
                jDateChooser[0].addPropertyChangeListener("date", new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        search();
                    }
                });
                leftContent.add(jDateChooser[0], "wrap");
                continue;
            }

            if (string.equals("Đến ngày:")) {
                jDateChooser[1].addPropertyChangeListener("date", new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        search();
                    }
                });
                leftContent.add(jDateChooser[1], "wrap");
                continue;
            }
            textField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {search();}

                @Override
                public void removeUpdate(DocumentEvent e) {
                    search();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    search();
                }
            });
            textField.setPreferredSize(new Dimension(400, 50));
            textField.setFont((new Font("FlatLaf.style", Font.PLAIN, 14)));
            jTextFieldSearch.add(textField);
            leftContent.add(textField, "wrap");
        }

        loadDataTable(receiptBLL.getReceiptList());
    }

    private void search() {
        String staffName = jTextFieldSearch.get(0).getText();
//        String customerName = jTextFieldSearch.get(1).getText();
        Date startDate = null;
        if (jDateChooser[0].getDateEditor().getDate() != null) {
            try {
                startDate = new Date(jDateChooser[0].getDate());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Lỗi định dạng ngày.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        Date endDate = null;
        if (jDateChooser[1].getDateEditor().getDate() != null) {
            try {
                endDate = new Date(jDateChooser[1].getDate());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Lỗi định dạng ngày.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        if (staffName.isEmpty() && jDateChooser[0].getDateEditor().getDate() == null && jDateChooser[1].getDateEditor().getDate() == null) {
            loadDataTable(receiptBLL.getReceiptList());
            return;
        }
        if (!Objects.equals(staffName, "")) {
            List<Receipt> list = new ArrayList<>();
            for (Receipt receipt : receipts) {
                Staff staff = staffBLL.findStaffsBy(Map.of("id", receipt.getStaff_id())).get(0);
                if (staff.getName().toLowerCase().contains(staffName.toLowerCase()))
                    list.add(receipt);
            }
            receipts = list;
        }
//        if (!Objects.equals(customerName, "")) {
//            List<Receipt> list = new ArrayList<>();
//            for (Receipt receipt : receipts) {
//                Customer customer = customerBLL.findCustomersBy(Map.of("id", receipt.getCustomer_id())).get(0);
//                if (customer.getName().toLowerCase().contains(customerName.toLowerCase()))
//                    list.add(receipt);
//            }
//            receipts = list;
//        }
        if (startDate != null) {
            System.out.println(startDate);
            List<Receipt> list = new ArrayList<>();
            for (Receipt receipt : receipts) {
                if (receipt.getInvoice_date().isAfter(startDate))
                    list.add(receipt);
            }
            receipts = list;
        }
        if (endDate != null) {
            List<Receipt> list = new ArrayList<>();
            for (Receipt receipt : receipts) {
                if (receipt.getInvoice_date().isBefore(endDate))
                    list.add(receipt);
            }
            receipts = list;
        }
        loadDataTable(receipts);
    }

    private void loadDataTable(List<Receipt> receipts) {
        ReceiptGUI.receipts = receipts;
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        receiptList = new Object[0][0];
        StaffBLL staffBLL = new StaffBLL();
//        CustomerBLL customerBLL = new CustomerBLL();
        for (Receipt receipt : receipts) {
            Object[] object = new Object[5];
            object[0] = receipt.getId();
            object[1] = staffBLL.findStaffsBy(Map.of("id", receipt.getStaff_id())).get(0).getName();
//            object[2] = customerBLL.findCustomersBy(Map.of("id", receipt.getCustomer_id())).get(0).getName();
            object[2] = receipt.getInvoice_date();
            object[3] = receipt.getTotal();
            receiptList = Arrays.copyOf(receiptList, receiptList.length + 1);
            receiptList[receiptList.length - 1] = object;
        }
        model.setRowCount(0);
        for (Object[] object : receiptList) {
            model.addRow(object);
        }
    }

    private void selectSortFilter() {
        if (Objects.requireNonNull(cbbSort.getSelectedItem()).toString().contains("Sắp xếp theo ngày lập")) {
            sortByInvoiceDate();
        } else if (Objects.requireNonNull(cbbSort.getSelectedItem()).toString().contains("Sắp xếp theo tổng tiền tăng dần")) {
            sortByTotal(true);
        } else {
            sortByTotal(false);
        }
    }

    private void sortByTotal(boolean b) {
        if (b) {
            for (int i = 0; i < receipts.size() -1; i++) {
                for (int j = i + 1; j < receipts.size(); j++) {
                    if (receipts.get(i).getTotal() > receipts.get(j).getTotal()) {
                        Receipt receipt = receipts.get(i);
                        receipts.set(i, receipts.get(j));
                        receipts.set(j, receipt);
                    }
                }
            }
        } else {
            for (int i = 0; i < receipts.size() -1; i++) {
                for (int j = i + 1; j < receipts.size(); j++) {
                    if (receipts.get(i).getTotal() < receipts.get(j).getTotal()) {
                        Receipt receipt = receipts.get(i);
                        receipts.set(i, receipts.get(j));
                        receipts.set(j, receipt);
                    }
                }
            }
        }
        loadDataTable(receipts);
    }

    private void sortByInvoiceDate() {
        for (int i = 0; i < receipts.size() -1; i++) {
            for (int j = i + 1; j < receipts.size(); j++) {
                if (receipts.get(i).getInvoice_date().isAfter(receipts.get(j).getInvoice_date())) {
                    Receipt receipt = receipts.get(i);
                    receipts.set(i, receipts.get(j));
                    receipts.set(j, receipt);
                }
            }
        }
        loadDataTable(receipts);
    }

    private void showDetailReceipt() {
        receiptBLL = new ReceiptBLL();
        if (dataTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm cần xem chi tiết.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        new FormDetailReceiptGUI(receiptBLL.findReceiptsBy(Map.of("id", Integer.parseInt(model.getValueAt(dataTable.getSelectedRow(), 0).toString()))).get(0));
    }

}
