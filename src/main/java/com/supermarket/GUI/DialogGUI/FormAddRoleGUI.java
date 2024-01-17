package com.supermarket.GUI.DialogGUI;

import com.supermarket.BLL.RoleBLL;
import com.supermarket.DTO.Role;
import com.supermarket.GUI.DecentralizationGUI;
import com.supermarket.GUI.components.DataTable;
import com.supermarket.GUI.components.RoundedPanel;
import com.supermarket.GUI.components.RoundedScrollPane;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class FormAddRoleGUI extends DialogForm {
    private final RoleBLL roleBLL = new RoleBLL();
    private DataTable dataTable;
    private RoundedPanel formDetail;
    private RoundedScrollPane scrollPaneFormDetail;
    private RoundedScrollPane scrollPaneDataTable;
    private List<JLabel> attributeRole;
    private List<JTextField> jTextFieldRole;
    private JButton buttonCancel;
    private JButton buttonAdd;
    private boolean flag;

    public FormAddRoleGUI() {
        super();
        super.setTitle("Quản lý chức vụ");
        init();
        containerButton.setBackground(new Color(0xFFFFFF));
        setVisible(true);
    }

    public void init() {
        dataTable = new DataTable(new Object[][] {}, new String[] {}, e -> {});
        formDetail = new RoundedPanel();
        attributeRole = new ArrayList<>();
        jTextFieldRole = new ArrayList<>();
        buttonCancel = new JButton("Huỷ");
        buttonAdd = new JButton("Thêm");
        scrollPaneDataTable = new RoundedScrollPane(containerTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneFormDetail = new RoundedScrollPane(formDetail, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);


        scrollPaneDataTable.setPreferredSize(new Dimension(600, 700));
        leftContent.add(scrollPaneDataTable, BorderLayout.CENTER);
//        containerTable.add(dataTable);

        scrollPaneFormDetail.setPreferredSize(new Dimension(670, 610));
        rightContent.add(scrollPaneFormDetail, BorderLayout.NORTH);

        formDetail.setBackground(new Color(0xFFBDD2DB));
        formDetail.setLayout(new MigLayout("", "50[]20[]10", "20[]20[]"));

        for (String string : new String[]{"Mã chức vụ:", "Tên chức vụ:"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont(new Font("FlatLaf.style", Font.PLAIN, 16));
            attributeRole.add(label);
            formDetail.add(label);

            JTextField textField = new JTextField();
            if (string.equals("Mã chức vụ:")) {
                textField.setText(String.valueOf(roleBLL.getAutoID(roleBLL.searchRoles())));
                textField.setEnabled(false);
            }
            textField.setPreferredSize(new Dimension(400, 50));
            textField.setFont(new Font("FlatLaf.style", Font.BOLD, 14));
            jTextFieldRole.add(textField);
            formDetail.add(textField, "wrap");
        }

        buttonCancel.setPreferredSize(new Dimension(100, 40));
        buttonCancel.setFont(new Font("FlatLaf.style", Font.BOLD, 15));
        buttonCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonCancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                refresh();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                buttonCancel.setBackground(new Color(0xD54218));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buttonCancel.setBackground(null);
            }
        });
        containerButton.add(buttonCancel);

        buttonAdd.setPreferredSize(new Dimension(100, 40));
        buttonAdd.setFont(new Font("FlatLaf.style", Font.BOLD, 15));
        buttonAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                addRole();
            }
        });
        containerButton.add(buttonAdd);
    }

    private void addRole() {
        for (int i = 0; i < jTextFieldRole.size(); i++) {
            if (jTextFieldRole.get(i).getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin chức vụ.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        int id = Integer.parseInt(jTextFieldRole.get(0).getText());
        String name = jTextFieldRole.get(1).getText();

        Role role = new Role(id, name, false);

        String[] options = new String[]{"Huỷ", "Xác nhận"};
        int choice = JOptionPane.showOptionDialog(null, "Xác nhận thêm tài khoản?",
            "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (choice == 1) {
            if (roleBLL.addRole(role)) {
                JOptionPane.showMessageDialog(null, "Thêm tài khoản thành công!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);

                DecentralizationGUI.loadDataTable(roleBLL.getData());
                refresh();
            } else {
                JOptionPane.showMessageDialog(null, "Thêm tài khoản không thành công!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refresh() {
        jTextFieldRole.get(0).setEnabled(true);
        jTextFieldRole.get(0).setText(String.valueOf(roleBLL.getAutoID(roleBLL.searchRoles())));
        jTextFieldRole.get(0).setEnabled(false);
        jTextFieldRole.get(1).setText("");
    }
}
