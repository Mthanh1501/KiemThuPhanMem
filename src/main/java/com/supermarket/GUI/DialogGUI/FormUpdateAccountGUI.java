package com.supermarket.GUI.DialogGUI;

import com.supermarket.BLL.AccountBLL;
import com.supermarket.BLL.RoleBLL;
import com.supermarket.BLL.StaffBLL;
import com.supermarket.DTO.Account;
import com.supermarket.DTO.Role;
import com.supermarket.DTO.Staff;
import com.supermarket.GUI.AccountGUI;
import com.supermarket.GUI.components.DataTable;
import com.supermarket.GUI.components.RoundedPanel;
import com.supermarket.GUI.components.RoundedScrollPane;
import com.supermarket.utils.DateTime;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FormUpdateAccountGUI extends DialogForm {
    private final AccountBLL accountBLL = new AccountBLL();
    private final StaffBLL staffBLL = new StaffBLL();
    private final RoleBLL roleBLL = new RoleBLL();
    private DataTable dataTable;
    private RoundedPanel formDetail;
    private RoundedScrollPane scrollPaneFormDetail;
    private RoundedScrollPane scrollPaneDatatable;
    private java.util.List<JLabel> attributeAccount;
    private List<JTextField> jTextFieldAccount;
    private JButton buttonCancel;
    private JButton buttonUpdate;
    private Account account;
    public FormUpdateAccountGUI(Account account) {
        super();
        this.account = account;
        super.setTitle("Quản lý tài khoản");
        init();
        containerButton.setBackground(new Color(0xFFFFFF));
        setVisible(true);
    }

    public void init() {
        dataTable = new DataTable(new Object[][] {}, new String[] {}, e -> {});
        formDetail = new RoundedPanel();
        attributeAccount = new ArrayList<>();
        jTextFieldAccount = new ArrayList<>();
        buttonCancel = new JButton("Huỷ");
        buttonUpdate = new JButton("Cập nhật");
        scrollPaneDatatable = new RoundedScrollPane(containerTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneFormDetail = new RoundedScrollPane(formDetail, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);


        scrollPaneDatatable.setPreferredSize(new Dimension(600, 700));
        leftContent.add(scrollPaneDatatable, BorderLayout.CENTER);
        containerTable.add(dataTable);

        scrollPaneFormDetail.setPreferredSize(new Dimension(670, 610));
        rightContent.add(scrollPaneFormDetail, BorderLayout.NORTH);

        formDetail.setBackground(new Color(0xFFBDD2DB));
        formDetail.setLayout(new MigLayout("", "50[]20[]10", "20[]20[]"));

        for (String string : new String[]{"Mã tài khoản:", "Tên tài khoản:", "Mã chức vụ:", "Lần đăng nhập cuối:", "Mã nhân viên:"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("FlatLaf.style", Font.PLAIN, 16)));
            attributeAccount.add(label);
            formDetail.add(label);

            JTextField textField = new JTextField();
            if (string.equals("Mã tài khoản:")) {
                textField.setText(String.valueOf(account.getId()));
                textField.setEnabled(false);
            }
            if (string.equals("Tên tài khoản:")) {
                textField.setText(account.getUsername());
            }
//            if (string.equals("Mật khẩu:")) {
//                textField.setText(account.getPassword());
//            }
            if (string.equals("Mã chức vụ:")) {
                textField.setText(String.valueOf(account.getRoleID()));
                textField.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        loadTableRole();
                    }
                });
                textField.setEnabled(false);
            }
            if (string.equals("Lần đăng nhập cuối:")) {
                textField.setText(account.getLast_signed_in().toString());
                textField.setEnabled(false);
            }
            if (string.equals("Mã nhân viên:")) {
                textField.setText(String.valueOf(account.getStaffID()));
                textField.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        loadTableStaff();
                    }
                });
                textField.setEnabled(false);
            }
            textField.setPreferredSize(new Dimension(400, 50));
            textField.setFont((new Font("FlatLaf.style", Font.BOLD, 14)));
            jTextFieldAccount.add(textField);
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

        buttonUpdate.setPreferredSize(new Dimension(100,40));
        buttonUpdate.setFont(new Font("FlatLaf.style", Font.BOLD, 15));
        buttonUpdate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonUpdate.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                updateAccount();
            }
        });
        containerButton.add(buttonUpdate);

    }

    private void updateAccount() {
        for (int i = 0; i < jTextFieldAccount.size(); i++) {
            if (i != 3 && jTextFieldAccount.get(i).getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin tài khoản.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        int id = Integer.parseInt(jTextFieldAccount.get(0).getText());
        String username = jTextFieldAccount.get(1).getText();
        int roleID = Integer.parseInt(jTextFieldAccount.get(2).getText());
        int staffID = Integer.parseInt(jTextFieldAccount.get(4).getText());

        Account account = new Account(id, username, "", roleID, staffID, DateTime.parseDateTime(String.valueOf(new DateTime())), false);

        String[] options = new String[]{"Huỷ", "Xác nhận"};
        int choice = JOptionPane.showOptionDialog(null, "Xác nhận cập nhật tài khoản?",
            "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (choice == 1) {
            if (accountBLL.updateAccount(account).getKey()) {
                JOptionPane.showMessageDialog(null, "Cập nhật tài khoản thành công!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                AccountGUI.loadDataTable(accountBLL.getData());
                dispose();
            } else {
                SmallDialog.showResult(accountBLL.updateAccount(account).getValue());
            }
        }

    }

    private void refresh() {
        jTextFieldAccount.get(0).setEnabled(true);
        jTextFieldAccount.get(0).setText(String.valueOf(accountBLL.getAutoID(accountBLL.searchAccounts())));
        jTextFieldAccount.get(0).setEnabled(false);
        jTextFieldAccount.get(1).setText("");
        jTextFieldAccount.get(2).setEnabled(true);
        jTextFieldAccount.get(2).setText("");
        jTextFieldAccount.get(2).setEnabled(false);
        jTextFieldAccount.get(4).setEnabled(true);
        jTextFieldAccount.get(4).setText("");
        jTextFieldAccount.get(4).setEnabled(false);
    }

    private void loadTableStaff() {
        dataTable = new DataTable(staffBLL.getData(), new String[] {"Mã nhân viên", "Họ tên", "Giới tính", "Ngày sinh", "Điện thoại", "Địa chỉ", "Email", "Ngày vào làm"}, e -> {});
        dataTable.setPreferredSize(new Dimension(1500, 700));
        containerTable.removeAll();
        containerTable.add(dataTable.getTableHeader(), BorderLayout.NORTH);
        containerTable.add(dataTable, BorderLayout.CENTER);

        Staff staff = staffBLL.findStaffsBy(Map.of("id", Integer.parseInt(jTextFieldAccount.get(4).getText()))).get(0);
        int index = staffBLL.getIndex(staff, "id", staffBLL.getStaffList());
        dataTable.setRowSelectionInterval(index, index);

        containerTable.repaint();
        containerTable.revalidate();
        scrollPaneDatatable.setViewportView(containerTable);
    }

    private void loadTableRole() {
        dataTable = new DataTable(roleBLL.getData(), new String[] {"Mã chức vụ", "Tên chức vụ"}, e -> selectRowTable());
        containerTable.removeAll();
        containerTable.add(dataTable.getTableHeader(), BorderLayout.NORTH);
        containerTable.add(dataTable, BorderLayout.CENTER);

        Role role = roleBLL.findRolesBy(Map.of("id", Integer.parseInt(jTextFieldAccount.get(2).getText()))).get(0);
        int index = roleBLL.getIndex(role, "id", roleBLL.getRoleList());
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
        jTextFieldAccount.get(2).setEnabled(true);
        jTextFieldAccount.get(2).setText(id);
        jTextFieldAccount.get(2).setEnabled(false);
    }
}
