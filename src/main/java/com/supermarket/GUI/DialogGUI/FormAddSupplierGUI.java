package com.supermarket.GUI.DialogGUI;

import com.supermarket.BLL.SupplierBLL;
import com.supermarket.DTO.Supplier;
import com.supermarket.GUI.SupplierGUI;
import com.supermarket.GUI.components.DataTable;
import com.supermarket.GUI.components.RoundedPanel;
import com.supermarket.GUI.components.RoundedScrollPane;
import com.supermarket.utils.VNString;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;

public class FormAddSupplierGUI extends DialogForm{
    private final SupplierBLL supplierBLL = new SupplierBLL();
    private DataTable dataTable;
    private RoundedPanel formDetail;
    private RoundedScrollPane scrollPaneFormDetail;
    private RoundedScrollPane scrollPaneDatatable;
    private List<JLabel> attributeSupplier;
    private List<JTextField> jTextFieldSupplier;
    private JButton buttonCancel;
    private JButton buttonAdd;
    private boolean flag;
    public boolean inputEntered ;

    public FormAddSupplierGUI() {
        super();
        super.setTitle("Quản lý tài khoản");
        init();
        containerButton.setBackground(new Color(0xFFFFFF));
        setVisible(true);

    }

    public void init() {
        inputEntered = false;
        dataTable = new DataTable(new Object[][] {}, new String[] {}, e -> {});
        formDetail = new RoundedPanel();
        attributeSupplier = new ArrayList<>();
        jTextFieldSupplier = new ArrayList<>();
        buttonCancel = new JButton("Huỷ");
        buttonAdd = new JButton("Thêm");
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
            attributeSupplier.add(label);
            formDetail.add(label);

            JTextField textField = new JTextField();
            if (string.equals("Mã nhà cung cấp:")) {
                textField.setText(String.valueOf(supplierBLL.getAutoID(supplierBLL.searchSuppliers())));
                textField.setEnabled(false);
            }

            textField.setPreferredSize(new Dimension(400, 50));
            textField.setFont((new Font("FlatLaf.style", Font.BOLD, 14)));
            jTextFieldSupplier.add(textField);
            formDetail.add(textField, "wrap");

            // Thêm sự kiện FocusListener vào mỗi ô nhập liệu
            textField.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (!textField.getText().isEmpty()) {
                        inputEntered = true;
                    }
                    // In ra command tương ứng khi người dùng click vào ô để nhập thông tin
                    if (string.equals("Tên nhà cung cấp:")) {
                        System.out.println("Bạn đang nhập tên nhà cung cấp");
                    } else if (string.equals("Email:")) {
                        System.out.println("Bạn đang nhập email nhà cung cấp");
                    } else if (string.equals("SĐT:")) {
                        System.out.println("Bạn đang nhập số điện thoại nhà cung cấp");
                    } else if (string.equals("Địa chỉ:")) {
                        System.out.println("Bạn đang nhập địa chỉ nhà cung cấp");
                    } // Thêm các trường hợp khác nếu cần
                }

                @Override
                public void focusLost(FocusEvent e) {
                    // Kiểm tra lỗi và hiển thị thông báo nếu cần khi ô mất focus
                    if(inputEntered){
                    if (string.equals("Tên nhà cung cấp:")) {
                        Pair<Boolean, String> nameValidation = validateName(textField.getText());
                        if (!nameValidation.getKey()) {
                            JOptionPane.showMessageDialog(null, nameValidation.getValue(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    } else if (string.equals("SĐT:")) {
                        Pair<Boolean, String> phoneValidation = validatePhone(textField.getText());
                        if (!phoneValidation.getKey()) {
                            JOptionPane.showMessageDialog(null, phoneValidation.getValue(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    } else if (string.equals("Email:")) {
                        Pair<Boolean, String> emailValidation = validateEmail(textField.getText());
                        if (!emailValidation.getKey()) {
                            JOptionPane.showMessageDialog(null, emailValidation.getValue(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    } // Thêm các trường hợp khác nếu cần
                }}
            });
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
                addSupplier();
            }
        });
        containerButton.add(buttonAdd);
    }
    private Pair<Boolean, String> validateName(String name) {
        if (VNString.containsNumber(name))
            return new Pair<>(false,"Tên ncc không được chứa số.");
        if (VNString.containsSpecial(name))
            return new Pair<>(false,"Tên ncc không được chứa ký tự đặc biệt.");
        return new Pair<>(true,name);
    }

    private Pair<Boolean, String> validatePhone(String phone) {
        if (!VNString.checkFormatPhone(phone))
            return new Pair<>(false,"Số điện thoại ncc phải bắt đầu với \"0x\" hoặc \"+84x\" hoặc \"84x\" với \"x\" thuộc {3, 5, 7, 8, 9}.");
        return new Pair<>(true,phone);
    }

    private Pair<Boolean, String> validateEmail(String email) {
        if (VNString.containsUnicode(email))
            return new Pair<>(false,"Email ncc không được chứa unicode.");
        if (!VNString.checkFormatOfEmail(email))
            return new Pair<>(false,"Email ncc phải theo định dạng (username@domain.name).");
        return new Pair<>(true,email);
    }

    private void addSupplier() {
        for (int i = 0; i < jTextFieldSupplier.size(); i++) {
            if (i != 4 && jTextFieldSupplier.get(i).getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin nhà cung cấp.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        int id = Integer.parseInt(jTextFieldSupplier.get(0).getText());
        String name = jTextFieldSupplier.get(1).getText();
        String phone = jTextFieldSupplier.get(2).getText();
        String address = jTextFieldSupplier.get(3).getText();
        String email = jTextFieldSupplier.get(4).getText();

        Supplier supplier = new Supplier(id, name, phone, address, email, false);

        String[] options = new String[]{"Huỷ", "Xác nhận"};
        int choice = JOptionPane.showOptionDialog(null, "Xác nhận thêm nhà cung cấp?",
            "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (choice == 1) {
            if (supplierBLL.addSupplier(supplier).getKey()) {
                JOptionPane.showMessageDialog(null, "Thêm nhà cung cấp thành công!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                SupplierGUI.loadDataTable(supplierBLL.getData());
                dispose();
            } else {
                SmallDialog.showResult(supplierBLL.addSupplier(supplier).getValue());
            }
        }

    }

    private void refresh() {
        jTextFieldSupplier.get(0).setEnabled(true);
        jTextFieldSupplier.get(0).setText(String.valueOf(supplierBLL.getAutoID(supplierBLL.searchSuppliers())));
        jTextFieldSupplier.get(0).setEnabled(false);
        jTextFieldSupplier.get(1).setText("");
        jTextFieldSupplier.get(2).setText("");
        jTextFieldSupplier.get(3).setEnabled(true);
        jTextFieldSupplier.get(3).setText("");
        jTextFieldSupplier.get(3).setEnabled(false);
        jTextFieldSupplier.get(5).setEnabled(true);
        jTextFieldSupplier.get(5).setText("");
        jTextFieldSupplier.get(5).setEnabled(false);
    }

    public void selectRowTable() {
        String id = "";
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        int indexRow = dataTable.getSelectedRow();
        id = model.getDataVector().elementAt(indexRow).get(0).toString();
        if (flag) {
            jTextFieldSupplier.get(3).setEnabled(true);
            jTextFieldSupplier.get(3).setText(id);
            jTextFieldSupplier.get(3).setEnabled(false);
        } else {
            jTextFieldSupplier.get(5).setEnabled(true);
            jTextFieldSupplier.get(5).setText(id);
            jTextFieldSupplier.get(5).setEnabled(false);
        }
    }
}
