package com.supermarket.GUI.DialogGUI;

import com.supermarket.BLL.BrandBLL;
import com.supermarket.BLL.ImportBLL;
import com.supermarket.BLL.ProductBLL;
import com.supermarket.BLL.SupplierBLL;
import com.supermarket.DTO.Brand;
import com.supermarket.DTO.Import;
import com.supermarket.DTO.Product;
import com.supermarket.DTO.Supplier;
import com.supermarket.GUI.HomeGUI;
import com.supermarket.GUI.ProductGUI;
import com.supermarket.GUI.components.DataTable;
import com.supermarket.GUI.components.RoundedPanel;
import com.supermarket.GUI.components.RoundedScrollPane;
import com.supermarket.utils.Date;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FormAddImportGUI extends DialogForm{
    private Import newImport;
    private final ImportBLL importBLL = new ImportBLL();
    private ProductBLL productBLL = new ProductBLL();
    private DataTable dataTable;
    private RoundedPanel formDetail;
    private RoundedScrollPane scrollPaneDatatable;
    private RoundedScrollPane scrollPaneImportDetail;
    private java.util.List<JLabel> attributeProduct;
    private List<JTextField> jTextFieldImport;
    private JButton buttonCancel;
    private JButton buttonAdd;
    private JButton buttonAddProduct;
    private boolean flag;
    private static List<Integer> productIDInImport;

    public FormAddImportGUI() {
        super();
        super.setTitle("Quản lý phiếu nhập");
        init();
        containerButton.setBackground(new Color(0xFFFFFF));
        setVisible(true);
    }

    private void init() {
        dataTable = new DataTable(new Object[][] {}, new String[] {}, e -> {});
        formDetail = new RoundedPanel();
        attributeProduct = new ArrayList<>();
        jTextFieldImport = new ArrayList<>();
        scrollPaneDatatable = new RoundedScrollPane(containerTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneImportDetail = new RoundedScrollPane(formDetail, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        buttonCancel = new JButton("Huỷ");
        buttonAdd = new JButton("Thêm");
        buttonAddProduct = new JButton("Thêm sản phẩm");
        productIDInImport = new ArrayList<>();

        scrollPaneDatatable.setPreferredSize(new Dimension(600, 700));
        leftContent.add(scrollPaneDatatable, BorderLayout.CENTER);
        containerTable.add(dataTable);

        scrollPaneImportDetail.setPreferredSize(new Dimension(670, 610));
        rightContent.add(scrollPaneImportDetail, BorderLayout.NORTH);

        formDetail.setBackground(new Color(0xFFBDD2DB));
        formDetail.setLayout(new MigLayout("", "50[]20[]10", "20[]20[]"));

        for (String string : new String[] {"Mã phiếu nhập:", "Mã nhân viên:", "Ngày nhập:", "Tổng tiền:", "Mã nhà cung cấp:"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("FlatLaf.style", Font.PLAIN, 16)));
            attributeProduct.add(label);
            formDetail.add(label);

            JTextField textField = new JTextField();
            if (string.equals("Mã phiếu nhập:")) {
                textField.setText(String.valueOf(importBLL.getAutoID(importBLL.searchImport())));
                textField.setEnabled(false);
                textField.setEditable(false);
            }
            if (string.equals("Mã nhân viên:")) {
                textField.setText(String.valueOf(HomeGUI.account.getStaffID()));
                textField.setEnabled(false);
            }
            if (string.equals("Ngày nhập:")) {
                textField.setText(Date.dateNow());
                textField.setEnabled(false);
            }
            if (string.equals("Tổng tiền:")) {
                textField.setEnabled(false);
            }
            if (string.equals("Mã nhà cung cấp:")) {
                textField.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        loadTableSupplier();
                    }
                });
                textField.setEnabled(false);
            }
            textField.setPreferredSize(new Dimension(400, 50));
            textField.setFont((new Font("FlatLaf.style", Font.PLAIN, 14)));
            jTextFieldImport.add(textField);
            formDetail.add(textField, "wrap");
        }

        buttonCancel.setPreferredSize(new Dimension(100,40));
        buttonCancel.setFont(new Font("FlatLaf.style", Font.BOLD, 15));
        buttonCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonCancel.setVisible(false);
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
                addImport();
            }
        });
        containerButton.add(buttonAdd);

        buttonAddProduct.setPreferredSize(new Dimension(100,40));
        buttonAddProduct.setFont(new Font("FlatLaf.style", Font.BOLD, 15));
        buttonAddProduct.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonAddProduct.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                new ProductGUI(List.of()).setVisible(false);
                new FormAddProductGUI(Integer.parseInt(jTextFieldImport.get(4).getText()));
                productBLL = new ProductBLL();
                int supplierID = Integer.parseInt(jTextFieldImport.get(4).getText());
                BrandBLL brandBLL = new BrandBLL();
                List<Integer> brandList = new ArrayList<>();
                for (Brand brand : brandBLL.findBrandsBy(Map.of("supplier_id", supplierID))) {
                    brandList.add(brand.getId());
                }
                List<Product> list = new ArrayList<>();
                for (Product product : productBLL.getProductList()) {
                    if (brandList.contains(product.getBrand_id())) {
                        list.add(product);
                    }
                }
                loadTableProduct(list);
            }
        });
        formDetail.add(buttonAddProduct);
        buttonAddProduct.setVisible(false);
    }

    private void refresh() {
        jTextFieldImport.get(0).setEnabled(true);
        jTextFieldImport.get(0).setText(String.valueOf(importBLL.getAutoID(importBLL.searchImport())));
        jTextFieldImport.get(0).setEnabled(false);
        jTextFieldImport.get(1).setEnabled(true);
        jTextFieldImport.get(1).setText(String.valueOf(HomeGUI.account.getStaffID()));
        jTextFieldImport.get(1).setEnabled(false);
        jTextFieldImport.get(2).setEnabled(true);
        jTextFieldImport.get(2).setText(Date.dateNow());
        jTextFieldImport.get(2).setEnabled(false);
        jTextFieldImport.get(3).setEnabled(true);
        jTextFieldImport.get(3).setText("");
        jTextFieldImport.get(3).setEnabled(false);
        jTextFieldImport.get(4).setEnabled(true);
        jTextFieldImport.get(4).setText("");
        jTextFieldImport.get(4).setEnabled(false);
        productIDInImport.removeAll(productIDInImport);
        containerTable.removeAll();
        containerTable.repaint();
        containerTable.revalidate();
        buttonAddProduct.setVisible(false);
    }

    private void addImport() {
        for (int i = 0; i < jTextFieldImport.size(); i++) {
            if (i != 3 && jTextFieldImport.get(i).getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin phiếu nhập.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        if (productIDInImport.isEmpty()) {
            int id = Integer.parseInt(jTextFieldImport.get(0).getText());
            int staffID = Integer.parseInt(jTextFieldImport.get(1).getText());
            Date receivedDate = null;
            try {
                receivedDate = Date.parseDate(jTextFieldImport.get(2).getText());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            int supplierID = Integer.parseInt(jTextFieldImport.get(4).getText());
            newImport = new Import(id, staffID, receivedDate, 0, supplierID);
            BrandBLL brandBLL = new BrandBLL();
            List<Integer> brandList = new ArrayList<>();
            for (Brand brand : brandBLL.findBrandsBy(Map.of("supplier_id", supplierID))) {
                brandList.add(brand.getId());
            }
            List<Product> list = new ArrayList<>();
            for (Product product : productBLL.getProductList()) {
                if (brandList.contains(product.getBrand_id())) {
                    list.add(product);
                }
            }
            loadTableProduct(list);
            buttonAddProduct.setVisible(true);
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm nhập", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } else {

            String[] options = new String[]{"Huỷ", "Xác nhận"};
            int choice = JOptionPane.showOptionDialog(null, "Xác nhận nhập các sản phẩm đã chọn?",
                "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            if (choice == 1) {
                new FormAddShipmentGUI(newImport ,productIDInImport);
                dispose();
            }
        }

    }
    private void loadTableProduct(List<Product> products) {
        Object[][] productList = new Object[0][0];
        dataTable = new DataTable(new Object[][]{}, new String[] {"Mã sản phẩm", "Tên sản phẩm", ""}, e -> addToDetail(), 2);
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        model.setRowCount(0);
        for (Product product : products) {
            Object[] objects1 = new Object[3];
            objects1[0] = product.getId();
            objects1[1] = product.getName();
            if (productIDInImport.contains(product.getId())) {
                objects1[2] = true;
            } else {
                objects1[2] = null;
            }
            productList = Arrays.copyOf(productList, productList.length + 1);
            productList[productList.length - 1] = objects1;
        }

        for (Object[] object : productList) {
            model.addRow(object);
        }
        containerTable.removeAll();
//        dataTable.setPreferredSize(new Dimension(1500, 700));
        containerTable.add(dataTable.getTableHeader(), BorderLayout.NORTH);
        containerTable.add(dataTable, BorderLayout.CENTER);
        containerTable.repaint();
        containerTable.revalidate();
        scrollPaneDatatable.setViewportView(containerTable);



    }

    private void addToDetail() {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        Object[] rowData = model.getDataVector().elementAt(dataTable.getSelectedRow()).toArray();
        String[] data = new String[rowData.length];
        for (int i = 0; i < rowData.length; i++) {
            if (rowData[i] != null)
                data[i] = rowData[i].toString();
        }
        String[] product = String.join(" | ", data).split(" \\| ");
        Product product1 = productBLL.findProductsBy(Map.of("id", Integer.parseInt(product[0]))).get(0);
        if (data[2] == null) {
            productIDInImport.add(product1.getId());
            model.setValueAt(true, dataTable.getSelectedRow(), 2);
        } else {
            productIDInImport.remove((Object)product1.getId());
            model.setValueAt(null, dataTable.getSelectedRow(), 2);
        }
    }

    private void loadTableSupplier() {
        productIDInImport.removeAll(productIDInImport);
        flag = true;
        SupplierBLL staffBLL = new SupplierBLL();
        dataTable = new DataTable(staffBLL.getData(), new String[] {"Mã nhà cung cấp", "Tên nhà cung cấp", "Số điện thoại", "Địa chỉ", "Email"}, e -> selectRowTable());
        dataTable.setPreferredSize(new Dimension(1500, 700));
        containerTable.removeAll();
        containerTable.add(dataTable.getTableHeader(), BorderLayout.NORTH);
        containerTable.add(dataTable, BorderLayout.CENTER);

        if (!jTextFieldImport.get(4).getText().isEmpty()) {
            Supplier staff = staffBLL.findSuppliersBy(Map.of("id", Integer.parseInt(jTextFieldImport.get(4).getText()))).get(0);
            int index = staffBLL.getIndex(staff, "id", staffBLL.getSupplierList());
            dataTable.setRowSelectionInterval(index, index);
        }
        containerTable.repaint();
        containerTable.revalidate();
        scrollPaneDatatable.setViewportView(containerTable);
    }

    public void selectRowTable() {
        String id;
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        int indexRow = dataTable.getSelectedRow();
        id = model.getDataVector().elementAt(indexRow).get(0).toString();
        jTextFieldImport.get(4).setEnabled(true);
        jTextFieldImport.get(4).setText(id);
        jTextFieldImport.get(4).setEnabled(false);
    }
}
