package com.supermarket.GUI.DialogGUI;

import com.supermarket.GUI.components.RoundedPanel;
import com.supermarket.main.Mini_supermarketManagement;
import com.supermarket.utils.Excel;
import javafx.util.Pair;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.function.Function;

public class ExcelDialog extends JDialog {
    protected boolean cancel;
    protected final JTable table;
    protected final DefaultTableModel model;
    protected final RoundedPanel pnlButton;
    protected final JButton btnConfirm;
    protected final JButton btnCancel;
    protected final JButton btnAddRow;
    protected final JButton btnRemoveRow;
    protected final JButton btnPaste;
    protected final JButton btnReadFile;
    protected final List<Pair<String, Excel.Type>> columns;

    public ExcelDialog(List<Pair<String, Excel.Type>> columns, Function<List<String>, Pair<Boolean, String>> runWhenConfirm) {
        super((Frame) null, true);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cancel = true;
                dispose();
            }
        });
        this.getRootPane().registerKeyboardAction(e -> {
            cancel = true;
            dispose();
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setSize(1000, 600);
        this.setLocationRelativeTo(Mini_supermarketManagement.homeGUI);

        model = new DefaultTableModel();
        table = new JTable(model);
        pnlButton = new RoundedPanel();
        btnConfirm = new JButton("Xác nhận");
        btnCancel = new JButton("Hủy");
        btnAddRow = new JButton("Thêm dòng");
        btnRemoveRow = new JButton("Xóa dòng");
        btnPaste = new JButton("Dán");
        btnReadFile = new JButton("Mở file");

        this.columns = columns;
        for (Pair<String, Excel.Type> column : columns)
            model.addColumn(column.getKey());
        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);

        pnlButton.setLayout(new FlowLayout());
        this.add(pnlButton, BorderLayout.SOUTH);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, renderer);
        table.getTableHeader().setReorderingAllowed(false);
        model.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                Object value = model.getValueAt(row, column);
                Excel.Type type = columns.get(column).getValue();
                Object castValue;
                try {
                    castValue = Excel.castValue(value, type);
                } catch (ClassCastException exception) {
                    JOptionPane.showMessageDialog(Mini_supermarketManagement.homeGUI,
                        "Dữ liệu không đúng định dạng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    castValue = Excel.castValue(null, type);
                    model.setValueAt(castValue, row, column);
                    table.addRowSelectionInterval(row, row);
                }
            }
        });

        pnlButton.add(btnConfirm);
        pnlButton.add(btnCancel);
        pnlButton.add(btnAddRow);
        pnlButton.add(btnRemoveRow);
        pnlButton.add(btnPaste);
        pnlButton.add(btnReadFile);
        addListenerToButtons(runWhenConfirm);
    }

    public JTable getTable() {
        return table;
    }

    public DefaultTableModel getModel() {
        return model;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public void addListenerToButtons(Function<List<String>, Pair<Boolean, String>> runWhenConfirm) {
        btnConfirm.addActionListener(e -> {
            cancel = false;
            List<List<String>> data = getData();
            if (data == null)
                return;

            boolean hasError = false;
            for (int i = 0; i < data.size(); i++) {
                String messageArgument = data.get(i).toString();
                try {
                    List<String> row = data.get(i);
                    Pair<Boolean, String> result = runWhenConfirm.apply(row);
                    if (!result.getKey()) {
                        messageArgument = result.getValue();
                        throw new RuntimeException();
                    }
                    data.remove(i);
                    model.removeRow(i);
                    i--;
                } catch (Exception exception) {
                    hasError = true;
                    table.setRowSelectionInterval(i, i);
                    String message = "Tìm thấy lỗi ở dòng" + (i + 1) + ": " + messageArgument + ".";
                    Object[] options = new Object[]{"Bỏ qua", "Dừng lại"};
                    int choice = JOptionPane.showOptionDialog(this, message, "Lỗi",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
                    if (choice != 0) break;
                }
            }
            if (!hasError)
                dispose();
        });
        btnCancel.addActionListener(e -> {
            cancel = true;
            refresh();
            dispose();
        });
        btnAddRow.addActionListener(e -> {
            addRow();
            table.clearSelection();
            int row = model.getRowCount() - 1;
            table.addRowSelectionInterval(row, row);
        });
        btnRemoveRow.addActionListener(e -> {
            removeRow();
            table.clearSelection();
        });
        btnPaste.addActionListener(e -> pasteFromClipboard());
        btnReadFile.addActionListener(e -> {
            File file = Excel.openFile();
            if (file == null)
                return;
            Pair<List<List<Object>>, String> result = Excel.importExcel(file, columns);
            if (result.getKey() == null) {
                JOptionPane.showMessageDialog(this, result.getValue(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(this, result.getValue(),
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            table.clearSelection();
            List<List<Object>> data = result.getKey();
            for (List<Object> datum : data) {
                int row = model.getRowCount();
                addRow();
                for (int j = 0; j < data.get(0).size(); j++) {
                    model.setValueAt(datum.get(j), row, j);
                }
            }
        });
    }

    public void addRow() {
        List<Object> emptyData = new ArrayList<>();
        for (Pair<String, Excel.Type> column : columns) {
            Object datum = Excel.castValue(null, column.getValue());
            emptyData.add(datum);
        }
        model.addRow(emptyData.toArray());
    }

    public void removeRow() {
        int[] rows = table.getSelectedRows();
        for (int i = rows.length - 1; i >= 0 ; i--)
            model.removeRow(rows[i]);
    }

    public void pasteFromClipboard() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable transferable = clipboard.getContents(table);

        if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                String clipboardData = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                String[] data = clipboardData.split("\n");
                table.clearSelection();

                boolean hasError = false;
                for (String datum : data) {
                    int row = model.getRowCount();
                    String[] values = datum.split("\t");
                    model.addRow(values);
                    for (int j = 0; j < values.length; j++) {
                        Object value = model.getValueAt(row, j);
                        Excel.Type type = columns.get(j).getValue();
                        Object castValue;
                        try {
                            castValue = Excel.castValue(value, type);
                        } catch (ClassCastException exception) {
                            hasError = true;
                            castValue = Excel.castValue(null, type);
                            model.setValueAt(castValue, row, j);
                            table.addRowSelectionInterval(row, row);
                        }
                    }
                }
                if (hasError) {
                    JOptionPane.showMessageDialog(this,
                        "Những dữ liệu không hợp lệ đã được bỏ qua.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Đã xảy ra lỗi khi dán dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public List<List<String>> getData() {
        if (cancel)
            return null;
        List<List<String>> data = new ArrayList<>();
        for (Vector<?> row : model.getDataVector()) {
            List<String> rowData = new ArrayList<>();
            for (Object value : row)
                rowData.add(value.toString().trim());
            data.add(rowData);
        }
        return data;
    }

    public void refresh() {
        model.setRowCount(0);
    }
}
