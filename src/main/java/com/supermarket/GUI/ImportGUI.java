package com.supermarket.GUI;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.supermarket.BLL.ImportBLL;
import com.supermarket.BLL.StaffBLL;
import com.supermarket.BLL.SupplierBLL;
import com.supermarket.DTO.Function;
import com.supermarket.DTO.Import;
import com.supermarket.DTO.Staff;
import com.supermarket.DTO.Supplier;
import com.supermarket.GUI.DialogGUI.FormAddImportGUI;
import com.supermarket.GUI.DialogGUI.FormDetailImportGUI;
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

public class ImportGUI extends Layout2 {
    private ImportBLL importNoteBLL = new ImportBLL();
    private StaffBLL staffBLL = new StaffBLL();
    private SupplierBLL supplierBLL = new SupplierBLL();
    private static DataTable dataTable;
    private RoundedScrollPane scrollPane;
    private JLabel iconDetail;
    private JLabel iconAdd;
    private JComboBox cbbSort ;
    private List<JTextField> jTextFieldSearch;
    private JDateChooser[] jDateChooser;
    private JTextField[] jTextFields;
    private JTextField[] dateTextField;
    private static Object[][] importNoteList;
    private static List<Import> imports;
    public ImportGUI(List<Function> functions) {
        super();
        initComponent(functions);
    }

    private void initComponent(List<Function> functions) {
        jTextFieldSearch = new ArrayList<>();
        importNoteList = new Object[0][0];
        imports = new ArrayList<>();
        dataTable = new DataTable(new Object[][]{}, new String[]{"Mã phiếu nhập","Tên nhân viên", "Ngày nhập", "Tổng tiền", "Nhà cung cấp"}, e -> {});
        scrollPane = new RoundedScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        rightContent.add(scrollPane, BorderLayout.CENTER);

        iconAdd = new JLabel();
        cbbSort = new JComboBox(new String[] {"Sắp xếp theo ngày nhập giảm dần", "Sắp xếp theo ngày nhập tăng dần"});

        iconDetail = new JLabel();


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
                    showDetailImport();
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
                    addImport();
                }
            });
            leftMenu.add(iconAdd);
        }

        cbbSort.setPreferredSize(new Dimension(230, 30));
        cbbSort.addActionListener(e -> selectSortFilter());
        cbbSort.setSelectedIndex(0);
        selectSortFilter();
        rightMenu.add(cbbSort);

        for (String string : new String[] {"Tên nhân viên:", "Tên nhà cung cấp:", "Từ ngày:", "Đến ngày:"}) {
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

        loadDataTable(importNoteBLL.getImportList());
    }

    private void addImport() {
        new FormAddImportGUI();
    }

    private void search() {
        String staffName = jTextFieldSearch.get(0).getText();
        String supplierName = jTextFieldSearch.get(1).getText();
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
        if (staffName.isEmpty() && supplierName.isEmpty() && jDateChooser[0].getDateEditor().getDate() == null && jDateChooser[1].getDateEditor().getDate() == null) {
            loadDataTable(importNoteBLL.getImportList());
            return;
        }
        if (!Objects.equals(staffName, "")) {
            List<Import> list = new ArrayList<>();
            for (Import importNote : imports) {
                Staff staff = staffBLL.findStaffsBy(Map.of("id", importNote.getStaff_id())).get(0);
                if (staff.getName().toLowerCase().contains(staffName.toLowerCase()))
                    list.add(importNote);
            }
            imports = list;
        }
        if (!Objects.equals(supplierName, "")) {
            List<Import> list = new ArrayList<>();
            for (Import importNote : imports) {
                Supplier supplier = supplierBLL.findSuppliersBy(Map.of("id", importNote.getSupplier_id())).get(0);
                if (supplier.getName().toLowerCase().contains(supplierName.toLowerCase()))
                    list.add(importNote);
            }
            imports = list;
        }
        if (startDate != null) {
            System.out.println(startDate);
            List<Import> list = new ArrayList<>();
            for (Import importNote : imports) {
                if (importNote.getReceived_date().isAfter(startDate))
                    list.add(importNote);
            }
            imports = list;
        }
        if (endDate != null) {
            List<Import> list = new ArrayList<>();
            for (Import importNote : imports) {
                if (importNote.getReceived_date().isBefore(endDate))
                    list.add(importNote);
            }
            imports = list;
        }
        loadDataTable(imports);
    }

    public static void loadDataTable(List<Import> imports) {
        ImportGUI.imports = imports;
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        importNoteList = new Object[0][0];
        StaffBLL staffBLL = new StaffBLL();
        SupplierBLL supplierBLL = new SupplierBLL();
        for (Import importNote : imports) {
            Object[] object = new Object[6];
            object[0] = importNote.getId();
            object[1] = staffBLL.findStaffsBy(Map.of("id", importNote.getStaff_id())).get(0).getName();
            object[2] = importNote.getReceived_date();
            object[3] = importNote.getTotal();
            object[4] = supplierBLL.findSuppliersBy(Map.of("id", importNote.getSupplier_id())).get(0).getName();
            importNoteList = Arrays.copyOf(importNoteList, importNoteList.length + 1);
            importNoteList[importNoteList.length - 1] = object;
        }
        model.setRowCount(0);
        for (Object[] object : importNoteList) {
            model.addRow(object);
        }
    }

    private void selectSortFilter() {
        if (Objects.requireNonNull(cbbSort.getSelectedItem()).toString().contains("Sắp xếp theo ngày nhập giảm dần")) {
            sortByReceivedDate(false);
        } else if (Objects.requireNonNull(cbbSort.getSelectedItem()).toString().contains("Sắp xếp theo ngày nhập tăng dần")) {
            sortByReceivedDate(true);
        }
    }

    private void sortByReceivedDate(Boolean b) {
        for (int i = 0; i < imports.size() -1; i++) {
            for (int j = i + 1; j < imports.size(); j++) {
                if (b && imports.get(i).getReceived_date().isAfter(imports.get(j).getReceived_date())) {
                    Import importNote = imports.get(i);
                    imports.set(i, imports.get(j));
                    imports.set(j, importNote);
                }
                if (!b && imports.get(i).getReceived_date().isBefore(imports.get(j).getReceived_date())) {
                    Import importNote = imports.get(i);
                    imports.set(i, imports.get(j));
                    imports.set(j, importNote);
                }
            }
        }
        loadDataTable(imports);
    }

    private void showDetailImport() {
        importNoteBLL = new ImportBLL();
        if (dataTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn phiếu nhập cần xem chi tiết.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        new FormDetailImportGUI(importNoteBLL.findImportBy(Map.of("id", Integer.parseInt(model.getValueAt(dataTable.getSelectedRow(), 0).toString()))).get(0));
    }
}
