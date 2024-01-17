package com.supermarket.GUI.DialogGUI;

import com.supermarket.BLL.ImportBLL;
import com.supermarket.BLL.ProductBLL;
import com.supermarket.BLL.ShipmentBLL;
import com.supermarket.DTO.Import;
import com.supermarket.DTO.Product;
import com.supermarket.DTO.Shipment;
import com.supermarket.GUI.ImportGUI;
import com.supermarket.GUI.components.DataTable;
import com.supermarket.GUI.components.RoundedPanel;
import com.supermarket.GUI.components.RoundedScrollPane;
import com.supermarket.utils.Date;
import com.toedter.calendar.JDateChooser;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FormAddShipmentGUI extends DialogForm{
    private List<Integer> productIDInImport;
    private List<Shipment> shipments;
    private Import newImport;
    private final ImportBLL importBLL = new ImportBLL();
    private final ShipmentBLL shipmentBLL = new ShipmentBLL();
    private final ProductBLL productBLL = new ProductBLL();
    private DataTable dataTable;
    private RoundedPanel formDetail;
    private RoundedScrollPane scrollPaneDatatable;
    private RoundedScrollPane scrollPaneImportDetail;
    private List<JTextField> jTextFieldShipment;
    private JDateChooser[] jDateChooser;
    private JTextField[] jTextFields;
    private JTextField[] dateTextField;
    private JButton buttonCancel;
    private JButton buttonAdd;
    private JButton buttonConfirm;
    public FormAddShipmentGUI(Import newImport, List<Integer> productIDInImport) {
        super();
        super.setTitle("Quản lý phiếu nhập");
        this.newImport = newImport;
        this.productIDInImport = productIDInImport;
        init();
        containerButton.setBackground(new Color(0xFFFFFF));
        setVisible(true);
    }

    private void init() {
        shipments = new ArrayList<>();
        dataTable = new DataTable(new Object[][] {}, new String[] {}, e -> {});
        formDetail = new RoundedPanel();
        jTextFieldShipment = new ArrayList<>();
        scrollPaneDatatable = new RoundedScrollPane(containerTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneImportDetail = new RoundedScrollPane(formDetail, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        buttonCancel = new JButton("Huỷ");
        buttonAdd = new JButton("Thêm");
        buttonConfirm = new JButton("Xác nhận");

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
            jDateChooser[i].setPreferredSize(new Dimension(400, 50));
            jDateChooser[i].setMinSelectableDate(new Date(1, 1, 1000).toJDateSafe());
            dateTextField[i] = (JTextField) jDateChooser[i].getDateEditor().getUiComponent();
            dateTextField[i].setFont(new Font("Tahoma", Font.BOLD, 14));
//            dateTextField[i].getDocument().addDocumentListener(new DocumentListener() {
//                @Override
//                public void insertUpdate(DocumentEvent e) {
//                    saveValue();
//                }
//
//                @Override
//                public void removeUpdate(DocumentEvent e) {
//                    saveValue();
//                }
//
//                @Override
//                public void changedUpdate(DocumentEvent e) {
//                    saveValue();
//                }
//            });
//            jDateChooser[i].addPropertyChangeListener("date", new PropertyChangeListener() {
//                @Override
//                public void propertyChange(PropertyChangeEvent evt) {
//                    saveValue();
//                }
//            });
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

        scrollPaneDatatable.setPreferredSize(new Dimension(600, 700));
        leftContent.add(scrollPaneDatatable, BorderLayout.CENTER);
        containerTable.add(dataTable);

        scrollPaneImportDetail.setPreferredSize(new Dimension(670, 610));
        rightContent.add(scrollPaneImportDetail, BorderLayout.NORTH);

        formDetail.setBackground(new Color(0xFFBDD2DB));
        formDetail.setLayout(new MigLayout("", "50[]20[]10", "20[]20[]"));

        for (String string : new String[]{"Mã sản phẩm:", "Đơn giá:", "Số lượng nhập:", "Ngày sản xuất:", "Ngày hết hạn:", "Mã SKU:"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("FlatLaf.style", Font.PLAIN, 16)));
            formDetail.add(label);

            JTextField textField = new JTextField();
            if (string.equals("Mã sản phẩm:")) {
                textField.setEnabled(false);
            }
            if (string.equals("Đơn giá:")) {
                textField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        char c = e.getKeyChar();
                        if (!Character.isDigit(c) && c != KeyEvent.VK_PERIOD) {
                            e.consume();
                        }
                    }
                });
            }
            if (string.equals("Số lượng nhập:")) {
                textField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        char c = e.getKeyChar();
                        if (!Character.isDigit(c) && c != KeyEvent.VK_PERIOD) {
                            e.consume();
                        }
                    }
                });
            }
            if (string.equals("Ngày sản xuất:")) {
                formDetail.add(jDateChooser[0], "wrap");
                continue;
            }
            if (string.equals("Ngày hết hạn:")) {
                formDetail.add(jDateChooser[1], "wrap");
                continue;
            }
            if (string.equals("Mã SKU:")) {
                textField.setEnabled(false);
            }
            textField.setPreferredSize(new Dimension(400, 50));
            textField.setFont((new Font("FlatLaf.style", Font.PLAIN, 14)));
            jTextFieldShipment.add(textField);
            formDetail.add(textField, "wrap");
        }

        for (Integer i : productIDInImport) {
            Shipment shipment = new Shipment(shipmentBLL.getAutoID(shipmentBLL.searchShipments()), i);
            shipment.setImport_id(newImport.getId());
            shipment.setSku("00" + shipment.getId() + "00" + i);
            shipments.add(shipment);
        }

        buttonConfirm.setPreferredSize(new Dimension(100,40));
        buttonConfirm.setFont(new Font("FlatLaf.style", Font.BOLD, 15));
        buttonConfirm.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonConfirm.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                comfirmShipment();
            }
        });
        formDetail.add(buttonConfirm);

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
                addShipment();
            }
        });
        containerButton.add(buttonAdd);

        loadTableProduct();

    }

    private void comfirmShipment() {
        for (Shipment shipment : shipments) {
            if (shipment.getProduct_id() == Integer.parseInt(jTextFieldShipment.get(0).getText())) {
                try {
                    double uniPrice = Double.parseDouble(jTextFieldShipment.get(1).getText());
                    shipment.setUnit_price(uniPrice);

                    double quantity = Double.parseDouble(jTextFieldShipment.get(2).getText());
                    shipment.setQuantity(quantity);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                if (jDateChooser[0].getDateEditor().getDate() != null) {
                    shipment.setMfg(new Date(jDateChooser[0].getDate()));
                }
                if (jDateChooser[1].getDateEditor().getDate() != null) {
                    shipment.setExp(new Date(jDateChooser[1].getDate()));
                }
            }
        }
    }

    private void loadTableProduct() {
        ProductBLL productBLL = new ProductBLL();
        Object[][] productList = new Object[0][0];
        for (Product product : productBLL.getProductList()) {
            if (productIDInImport.contains(product.getId())) {
                Object[] objects1 = new Object[2];
                objects1[0] = product.getId();
                objects1[1] = product.getName();
                productList = Arrays.copyOf(productList, productList.length + 1);
                productList[productList.length - 1] = objects1;
            }
        }
        dataTable = new DataTable(productList, new String[] {"Mã sản phẩm", "Tên sản phẩm"}, e -> selectRowTable());
        containerTable.removeAll();
        containerTable.add(dataTable.getTableHeader(), BorderLayout.NORTH);
        containerTable.add(dataTable, BorderLayout.CENTER);

        if (!jTextFieldShipment.get(0).getText().isEmpty()) {
            Product product = productBLL.findProductsBy(Map.of("id", Integer.parseInt(jTextFieldShipment.get(0).getText()))).get(0);
            int index = productBLL.getIndex(product, "id", productBLL.getProductList());
            dataTable.setRowSelectionInterval(index, index);
        }
        containerTable.repaint();
        containerTable.revalidate();
        scrollPaneDatatable.setViewportView(containerTable);
    }
    private void addShipment() {
        for (Shipment shipment : shipments) {
            if (shipment.getUnit_price() == 0.0) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin lô hàng.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (shipment.getQuantity() == 0.0) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin lô hàng.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (shipment.getMfg() == null) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin lô hàng.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (shipment.getExp() == null) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin lô hàng.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        String[] options = new String[]{"Huỷ", "Xác nhận"};
        int choice = JOptionPane.showOptionDialog(null, "Xác nhận nhập các lô hàng?",
            "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (choice == 1) {
            double total = 0;
            for (Shipment shipment : shipments) {
                shipment.setRemain(shipment.getQuantity());
                shipment.setDeleted(false);
                total += shipment.getQuantity() * shipment.getUnit_price();
            }
            newImport.setTotal(total);
            if (importBLL.addImport(newImport)) {
                JOptionPane.showMessageDialog(null, "Thêm phiếu nhập thành công!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                for (Shipment shipment : shipments) {
                    shipment.setId(shipmentBLL.getAutoID(shipmentBLL.searchShipments()));
                    shipmentBLL.addShipment(shipment);
                }
                ImportGUI.loadDataTable(importBLL.getImportList());
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Thêm phiếu nhập không thành công!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refresh() {
        jTextFieldShipment.get(1).setText("");
        jTextFieldShipment.get(2).setText("");
        jDateChooser[0].getDateEditor().setDate(null);
        jDateChooser[1].getDateEditor().setDate(null);
    }

    public void selectRowTable() {
        String id;
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        int indexRow = dataTable.getSelectedRow();
        id = model.getDataVector().elementAt(indexRow).get(0).toString();
        jTextFieldShipment.get(1).setText("");
        jTextFieldShipment.get(2).setText("");
        jDateChooser[0].getDateEditor().setDate(null);
        jDateChooser[1].getDateEditor().setDate(null);
        for (Shipment shipment : shipments) {
            if (shipment.getProduct_id() == Integer.parseInt(id)) {
                jTextFieldShipment.get(0).setText(String.valueOf(shipment.getProduct_id()));
                if (shipment.getUnit_price() != 0.0) {
                    jTextFieldShipment.get(1).setText(String.valueOf(shipment.getUnit_price()));
                } else {
                    jTextFieldShipment.get(1).setText("");
                }
                if (shipment.getQuantity() != 0.0) {
                    jTextFieldShipment.get(2).setText(String.valueOf(shipment.getQuantity()));

                } else {
                    jTextFieldShipment.get(2).setText("");
                }
                if (shipment.getMfg() != null) {
                    jDateChooser[0].getDateEditor().setDate(shipment.getMfg().toJDateSafe());
                } else {
                    jDateChooser[0].getDateEditor().setDate(null);
                }
                if (shipment.getExp() != null) {
                    jDateChooser[1].getDateEditor().setDate(shipment.getExp().toJDateSafe());
                } else {
                    jDateChooser[1].getDateEditor().setDate(null);
                }
                jTextFieldShipment.get(3).setText(String.valueOf(shipment.getSku()));
            }
        }

    }
}
