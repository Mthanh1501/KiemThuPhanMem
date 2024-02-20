package com.supermarket.GUI;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.supermarket.BLL.SupplierBLL;
import com.supermarket.DTO.Function;
import com.supermarket.DTO.Supplier;
import com.supermarket.GUI.DialogGUI.ExcelDialog;
import com.supermarket.GUI.DialogGUI.FormAddSupplierGUI;
import com.supermarket.GUI.DialogGUI.FormDetailSupplierGUI;
import com.supermarket.GUI.DialogGUI.FormUpdateSupplierGUI;
import com.supermarket.GUI.components.DataTable;
import com.supermarket.GUI.components.Layout1;
import com.supermarket.GUI.components.RoundedScrollPane;
import com.supermarket.main.Mini_supermarketManagement;
import com.supermarket.utils.Excel;
import javafx.util.Pair;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SupplierGUI extends Layout1 {
    private SupplierBLL supplierBLL = new SupplierBLL();
    private JLabel iconDetail;
    private JLabel iconAdd;
    private JLabel iconEdit;
    private JLabel iconDelete;
    private JLabel iconPDF;
    private JLabel iconExcel;
    private ExcelDialog dialogExcel;
    private static DataTable dataTable;
    private RoundedScrollPane scrollPane;
    private JTextField jTextFieldSearch;
    private JComboBox cbbAttributeProduct;
    private Object[][] supplierList;

    public SupplierGUI(List<Function> functions) {
        super();
        init(functions);
    }

    public void init(List<Function> functions) {
        supplierList = new Object[0][0];
        dataTable = new DataTable(new Object[][] {},
            new String[] {"Mã nhà cung cấp", "Tên nhà cung cấp", "SĐT", "Địa chỉ", "Email"}, e -> {});
        scrollPane = new RoundedScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        iconDetail = new JLabel();
        iconAdd = new JLabel();
        iconEdit = new JLabel();
        iconDelete = new JLabel();
        iconPDF = new JLabel();
        iconExcel = new JLabel();
        jTextFieldSearch = new JTextField();
        cbbAttributeProduct = new JComboBox(new String[] {"Tên nhà cung cấp", "SĐT", "Địa chỉ"});

        if (functions.stream().anyMatch(f -> f.getName().equals("Chi tiết"))) {
            iconDetail.setIcon(new FlatSVGIcon("icon/detail.svg"));
            iconDetail.setCursor(new Cursor(Cursor.HAND_CURSOR));
            iconDetail.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    showDetailSupplier();
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
                    addSupplier();
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
                    updateSupplier();
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
                    deleteSupplier();
                }
            });
            leftMenu.add(iconDelete);
        }

        if (functions.stream().anyMatch(f -> f.getName().equals("Xuất"))) {
            iconPDF.setIcon(new FlatSVGIcon("icon/export.svg"));
            iconPDF.setCursor(new Cursor(Cursor.HAND_CURSOR));
            iconPDF.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    exportSupplier();
                }
            });
            leftMenu.add(iconPDF);
        }

        if (functions.stream().anyMatch(f -> f.getName().equals("Nhập"))) {
            dialogExcel = new ExcelDialog(List.of(
                new Pair<>("Tên", Excel.Type.STRING),
                new Pair<>("SĐT", Excel.Type.STRING),
                new Pair<>("Địa chỉ", Excel.Type.STRING),
                new Pair<>("Email", Excel.Type.STRING)
            ), row -> {
                String name = row.get(0).trim().toUpperCase();
                String phone = row.get(1).trim();
                String address = row.get(2).trim();
                String email = row.get(3).trim();
                Supplier supplier = new Supplier(supplierBLL.getAutoID(supplierBLL.searchSuppliers()), name, phone, address, email, false);
                return supplierBLL.addSupplier(supplier);
            });
            iconExcel.setIcon(new FlatSVGIcon("icon/excel.svg"));
            iconExcel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            iconExcel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    importSupplier();
                }
            });
            leftMenu.add(iconExcel);
        }

        cbbAttributeProduct.setPreferredSize(new Dimension(180, 30));
        cbbAttributeProduct.addActionListener(e -> selectSearchFilter());
        rightMenu.add(cbbAttributeProduct);

        jTextFieldSearch.setPreferredSize(new Dimension(200, 30));
        jTextFieldSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                selectSearchFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                selectSearchFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                selectSearchFilter();
            }
        });
        rightMenu.add(jTextFieldSearch);

        bottom.add(scrollPane, BorderLayout.CENTER);

        loadDataTable(supplierBLL.getData());
    }

    private void searchByName() {
        if (jTextFieldSearch.getText().isEmpty()) {
            loadDataTable(supplierBLL.getData());
        } else {
            loadDataTable(supplierBLL.getData(supplierBLL.findSuppliers("name", jTextFieldSearch.getText())));
        }
    }
    private void searchByPhone() {
        if (jTextFieldSearch.getText().isEmpty()) {
            loadDataTable(supplierBLL.getData());
        } else {
            loadDataTable(supplierBLL.getData(supplierBLL.findSuppliers("phone", jTextFieldSearch.getText())));
        }
    }
    private void searchByAddress() {
        if (jTextFieldSearch.getText().isEmpty()) {
            loadDataTable(supplierBLL.getData());
        } else {
            loadDataTable(supplierBLL.getData(supplierBLL.findSuppliers("address", jTextFieldSearch.getText())));
        }
    }

    private void selectSearchFilter() {
        if (Objects.requireNonNull(cbbAttributeProduct.getSelectedItem()).toString().contains("Tên nhà cung cấp")) {
            searchByName();
        } else if (Objects.requireNonNull(cbbAttributeProduct.getSelectedItem()).toString().contains("SĐT")) {
            searchByPhone();
        } else {
            searchByAddress();
        }
    }
    private void addSupplier() {
        new FormAddSupplierGUI();
    }

    private void showDetailSupplier() {
        supplierBLL = new SupplierBLL();
        if (dataTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn nhà cung cấp cần xem chi tiết.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        new FormDetailSupplierGUI(supplierBLL.findSuppliersBy(Map.of("id", Integer.parseInt(model.getValueAt(dataTable.getSelectedRow(), 0).toString()))).get(0));
    }

    private void updateSupplier() {
        supplierBLL = new SupplierBLL();
        if (dataTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn nhà cung cấp cần cập nhật.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        new FormUpdateSupplierGUI(supplierBLL.findSuppliersBy(Map.of("id", Integer.parseInt(model.getValueAt(dataTable.getSelectedRow(), 0).toString()))).get(0));

    }

    private void deleteSupplier() {
        if (dataTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn nhà cung cấp cần xoá.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();

        String[] options = new String[]{"Huỷ", "Xác nhận"};
        int choice = JOptionPane.showOptionDialog(null, "Xác nhận xoá nhà cung cấp?",
            "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (choice == 1) {
            if (supplierBLL.deleteSupplier(supplierBLL.findSuppliersBy(Map.of("id", Integer.parseInt(model.getValueAt(dataTable.getSelectedRow(), 0).toString()))).get(0))) {
                JOptionPane.showMessageDialog(null, "Xoá nhà cung cấp thành công!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);

                loadDataTable(supplierBLL.getData());
            } else {
                JOptionPane.showMessageDialog(null, "Xoá nhà cung cấp không thành công!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void importSupplier() {
        dialogExcel.setVisible(true);
        if (!dialogExcel.isCancel()) {
            JOptionPane.showMessageDialog(Mini_supermarketManagement.homeGUI,
                "Nhập dữ liệu thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            loadDataTable(supplierBLL.getData());
        }
    }

    private void exportSupplier() {
        File file = Excel.saveFile();
        if (file == null)
            return;
        Pair<Boolean, String> result = Excel.exportExcel(file, dataTable.getModel());
        if (result.getKey())
            JOptionPane.showMessageDialog(Mini_supermarketManagement.homeGUI, result.getValue(), "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        else
            JOptionPane.showMessageDialog(Mini_supermarketManagement.homeGUI, result.getValue(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    public static void loadDataTable(Object[][] objects) {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();

        model.setRowCount(0);
        for (Object[] object : objects) {
            model.addRow(object);
        }
    }
}
