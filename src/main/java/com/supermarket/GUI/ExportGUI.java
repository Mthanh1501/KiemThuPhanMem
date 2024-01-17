package com.supermarket.GUI;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.supermarket.BLL.ExportBLL;
import com.supermarket.BLL.StaffBLL;
import com.supermarket.BLL.SupplierBLL;
import com.supermarket.DTO.Export;
import com.supermarket.DTO.Function;
import com.supermarket.DTO.Staff;
import com.supermarket.GUI.DialogGUI.FormAddExportGUI;
import com.supermarket.GUI.DialogGUI.FormDetailExportGUI;
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

public class ExportGUI extends Layout2 {
    private ExportBLL exportNoteBLL = new ExportBLL();
    private StaffBLL staffBLL = new StaffBLL();
    private static DataTable dataTable;
    private RoundedScrollPane scrollPane;
    private JLabel iconDetail;
    private JLabel iconAdd;
    private JComboBox cbbSort ;
    private List<JTextField> jTextFieldSearch;
    private JDateChooser[] jDateChooser;
    private JTextField[] jTextFields;
    private JTextField[] dateTextField;
    private static Object[][] exportNoteList;
    private static List<Export> exports;
    public ExportGUI(List<Function> functions) {
        super();
        initComponent(functions);
    }

    private void initComponent(List<Function> functions) {
        jTextFieldSearch = new ArrayList<>();
        exportNoteList = new Object[0][0];
        exports = new ArrayList<>();
        dataTable = new DataTable(new Object[][]{}, new String[]{"Mã phiếu xuất","Tên nhân viên", "Ngày xuất", "Tổng tiền", "Lý do"}, e -> {});
        scrollPane = new RoundedScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        rightContent.add(scrollPane, BorderLayout.CENTER);

        iconAdd = new JLabel();
        cbbSort = new JComboBox(new String[] {"Sắp xếp theo ngày xuất giảm dần", "Sắp xếp theo ngày xuất tăng dần"});

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
                    showDetailExport();
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
                    addExport();
                }
            });
            leftMenu.add(iconAdd);
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

        loadDataTable(exportNoteBLL.getExportList());
    }

    private void addExport() {
        new FormAddExportGUI();
    }

    private void search() {
        String staffName = jTextFieldSearch.get(0).getText();
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
            loadDataTable(exportNoteBLL.getExportList());
            return;
        }
        if (!Objects.equals(staffName, "")) {
            List<Export> list = new ArrayList<>();
            for (Export exportNote : exports) {
                Staff staff = staffBLL.findStaffsBy(Map.of("id", exportNote.getStaff_id())).get(0);
                if (staff.getName().toLowerCase().contains(staffName.toLowerCase()))
                    list.add(exportNote);
            }
            exports = list;
        }
        if (startDate != null) {
            System.out.println(startDate);
            List<Export> list = new ArrayList<>();
            for (Export exportNote : exports) {
                if (exportNote.getInvoice_date().isAfter(startDate))
                    list.add(exportNote);
            }
            exports = list;
        }
        if (endDate != null) {
            List<Export> list = new ArrayList<>();
            for (Export exportNote : exports) {
                if (exportNote.getInvoice_date().isBefore(endDate))
                    list.add(exportNote);
            }
            exports = list;
        }
        loadDataTable(exports);
    }

    public static void loadDataTable(List<Export> exports) {
        ExportGUI.exports = exports;
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        exportNoteList = new Object[0][0];
        StaffBLL staffBLL = new StaffBLL();
        SupplierBLL supplierBLL = new SupplierBLL();
        for (Export exportNote : exports) {
            Object[] object = new Object[5];
            object[0] = exportNote.getId();
            object[1] = staffBLL.findStaffsBy(Map.of("id", exportNote.getStaff_id())).get(0).getName();
            object[2] = exportNote.getInvoice_date();
            object[3] = exportNote.getTotal();
            object[4] = exportNote.getReason();
            exportNoteList = Arrays.copyOf(exportNoteList, exportNoteList.length + 1);
            exportNoteList[exportNoteList.length - 1] = object;
        }
        model.setRowCount(0);
        for (Object[] object : exportNoteList) {
            model.addRow(object);
        }
    }

    private void selectSortFilter() {
        if (Objects.requireNonNull(cbbSort.getSelectedItem()).toString().contains("Sắp xếp theo ngày xuất giảm dần")) {
            sortByReceivedDate(false);
        } else if (Objects.requireNonNull(cbbSort.getSelectedItem()).toString().contains("Sắp xếp theo ngày xuất tăng dần")) {
            sortByReceivedDate(true);
        }
    }

    private void sortByReceivedDate(Boolean b) {
        for (int i = 0; i < exports.size() -1; i++) {
            for (int j = i + 1; j < exports.size(); j++) {
                if (b && exports.get(i).getInvoice_date().isAfter(exports.get(j).getInvoice_date())) {
                    Export exportNote = exports.get(i);
                    exports.set(i, exports.get(j));
                    exports.set(j, exportNote);
                }
                if (!b && exports.get(i).getInvoice_date().isBefore(exports.get(j).getInvoice_date())) {
                    Export exportNote = exports.get(i);
                    exports.set(i, exports.get(j));
                    exports.set(j, exportNote);
                }
            }
        }
        loadDataTable(exports);
    }

    private void showDetailExport() {
        exportNoteBLL = new ExportBLL();
        if (dataTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn phiếu xuất cần xem chi tiết.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        new FormDetailExportGUI(exportNoteBLL.findExportBy(Map.of("id", Integer.parseInt(model.getValueAt(dataTable.getSelectedRow(), 0).toString()))).get(0));
    }
}
