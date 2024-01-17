package com.supermarket.GUI;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.supermarket.BLL.DecentralizationBLL;
import com.supermarket.BLL.RoleBLL;
import com.supermarket.DTO.Function;
import com.supermarket.DTO.Role;
import com.supermarket.GUI.DialogGUI.FormAddRoleGUI;
import com.supermarket.GUI.DialogGUI.FormUpdateRoleGUI;
import com.supermarket.GUI.components.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

public class DecentralizationGUI extends Layout4 {
    private RoleBLL roleBLL = new RoleBLL();
    private DecentralizationBLL decentralizationBLL = new DecentralizationBLL();
    private static DataTable dataTable;
    private RoundedScrollPane scrollPane;
    private JLabel iconDetail;
    private JLabel iconAdd;
    private JLabel iconEdit;
    private JLabel iconDelete;
    private RoundedPanel formDetail;
    private JComboBox cbbAttributeRole;
    private JTextField jTextFieldSearch;
    private static Object[][] roleList;
    private DecentralizationTable detailTable;

    public DecentralizationGUI(List<Function> functions) {
        super();
        init(functions);
    }

    public void init(List<Function> functions) {
        roleList = new Object[0][0];
        dataTable = new DataTable(new Object[][] {},
            new String[] {"Mã chức vụ", "Tên chức vụ"}, e -> showDetailRole());
        scrollPane = new RoundedScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        iconDetail = new JLabel();
        iconAdd = new JLabel();
        iconEdit = new JLabel();
        iconDelete = new JLabel();
        jTextFieldSearch = new JTextField();
        cbbAttributeRole = new JComboBox(new String[] {"Tên chức vụ"});

        if (functions.stream().anyMatch(f -> f.getName().equals("Chi tiết"))) {
            iconDetail.setIcon(new FlatSVGIcon("icon/detail.svg"));
            iconDetail.setCursor(new Cursor(Cursor.HAND_CURSOR));
            iconDetail.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    showDetailRole();
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
                    addRole();
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
                    updateRole();
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
                    deleteRole();
                }
            });
            leftMenu.add(iconDelete);
        }

        cbbAttributeRole.setPreferredSize(new Dimension(130, 30));
        rightMenu.add(cbbAttributeRole);

        jTextFieldSearch.setPreferredSize(new Dimension(200, 30));
        jTextFieldSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchRoles();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchRoles();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchRoles();
            }
        });
        rightMenu.add(jTextFieldSearch);

        detailTable = new DecentralizationTable();
        bottom.add(detailTable);
        bottom.add(rightContent, BorderLayout.EAST);

        rightContent.add(scrollPane);
        loadDataTable(roleBLL.getData());
    }

    private void searchRoles() {
        if (jTextFieldSearch.getText().isEmpty()) {
            loadDataTable(roleBLL.getData());
        } else {
            loadDataTable(roleBLL.getData(roleBLL.findRoles("name", jTextFieldSearch.getText())));
        }
    }

    private void addRole() {
        new FormAddRoleGUI();
    }

    private void updateRole() {
        roleBLL = new RoleBLL();
        if (dataTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn chức vụ cần cập nhật.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        new FormUpdateRoleGUI(roleBLL.findRolesBy(Map.of("id", Integer.parseInt(model.getValueAt(dataTable.getSelectedRow(), 0).toString()))).get(0));
    }

    private void deleteRole() {
        if (dataTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn chức vụ cần xóa.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();

        String[] options = new String[]{"Huỷ", "Xác nhận"};
        int choice = JOptionPane.showOptionDialog(null, "Xác nhận xoá chức vụ?",
            "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (choice == 1) {
            if (roleBLL.deleteRole(roleBLL.findRolesBy(Map.of("id", Integer.parseInt(model.getValueAt(dataTable.getSelectedRow(), 0).toString()))).get(0))) {
                JOptionPane.showMessageDialog(null, "Xoá chức vụ thành công!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);

                loadDataTable(roleBLL.getData());
            } else {
                JOptionPane.showMessageDialog(null, "Xoá chức vụ không thành công!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showDetailRole() {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        String roleId = (String) model.getValueAt(dataTable.getSelectedRow(), 0);
        Role role = roleBLL.findRolesBy(Map.of("id", Integer.parseInt(roleId))).get(0);
        detailTable.refreshTable();
        detailTable.setRole(role);
    }

    public static void loadDataTable(Object[][] objects) {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        model.setRowCount(0);
        for (Object[] object : objects) {
            model.addRow(object);
        }
    }
}
