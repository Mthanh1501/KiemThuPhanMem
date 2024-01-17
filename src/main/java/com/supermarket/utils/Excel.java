package com.supermarket.utils;

import com.supermarket.main.Mini_supermarketManagement;
import javafx.util.Pair;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyleInfo;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Excel {
    public static final String EXCEL_EXTENSION = ".xlsx";
    private static Workbook workbook;
    private static Sheet sheet;

    public static Pair<List<List<Object>>, String> importExcel(File file, List<Pair<String, Type>> columns) {
        try {
            FileInputStream inputStream = new FileInputStream(file);
            workbook = new XSSFWorkbook(inputStream);
            sheet = workbook.getSheetAt(0);
            int numColumns = sheet.getRow(0).getPhysicalNumberOfCells();
            if (numColumns != columns.size()) {
                StringBuilder columnNames = new StringBuilder();
                for (Pair<String, Type> column : columns)
                    columnNames.append(column.getKey()).append(", ");
                columnNames.setLength(columnNames.length() - 2);
                return new Pair<>(null, "Dữ liệu cần phải có " + columns.size() + " cột: " +  columnNames);
            }
            CellStyle redStyle = workbook.createCellStyle();
            redStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
            redStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            List<List<Object>> data = new ArrayList<>();
            int numRows = sheet.getLastRowNum();
            boolean ok = true;
            for (int i = 1; i <= numRows; i++) {
                List<Object> row = new ArrayList<>();
                boolean hasInvalidData = false;
                for (int j = 0; j < columns.size(); j++) {
                    Cell cell = sheet.getRow(i).getCell(j);
                    Object value;
                    try {
                        if (cell == null)
                            value = null;
                        else
                            value = getValueFrom(cell, cell.getCellType());
                        value = castValue(value, columns.get(j).getValue());
                        row.add(value);
                    } catch (Exception e) {
                        if (cell != null)
                            cell.setCellStyle(redStyle);
                        hasInvalidData = true;
                    }
                }
                if (hasInvalidData)
                    ok = false;
                else
                    data.add(row);
            }
            inputStream.close();
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
            if (ok)
                return new Pair<>(data, "Nhập dữ liệu thành công.");
            else
                return new Pair<>(data, "Những dữ liệu không hợp lệ đã được bỏ qua\nVui lòng mở file để xem những dữ liệu không hợp lệ được đánh dấu màu đỏ.");
        } catch (Exception e) {
            return new Pair<>(null, "Đã xảy ra lỗi khi xuất file Excel. Vui lòng óng file hoặc kiểm tra định dạng file.");
        }
    }

    public static Pair<Boolean, String> exportExcel(File file, TableModel model) {
        try {
            workbook = new XSSFWorkbook();
            sheet = workbook.createSheet();

            Font titleFont = newFont(14, true, false, false, Font.U_NONE);
            CellStyle titleStyle = newCellStyle(titleFont, HorizontalAlignment.CENTER);

            XSSFTable table = ((XSSFSheet) sheet).createTable(null);
            CTTable ctTable = table.getCTTable();
            ctTable.setId(1L);
            CTTableColumns columns = ctTable.addNewTableColumns();
            columns.setCount(model.getColumnCount());

            // Table header
            sheet.createRow(0);
            for (int i = 0; i < model.getColumnCount(); i++) {
                columns.addNewTableColumn().setId(i + 1);
                setCell(model.getColumnName(i), 0, i, null);
            }

            AreaReference dataRange = new AreaReference(
                new CellReference(0, 0),
                new CellReference(model.getRowCount(), model.getColumnCount() - 1),
                SpreadsheetVersion.EXCEL2007
            );
            ctTable.setRef(dataRange.formatAsString());
            ctTable.addNewAutoFilter().setRef(dataRange.formatAsString());
            ctTable.addNewSortState().setRef(dataRange.formatAsString());

            // Table data
            for (int i = 0; i < model.getRowCount(); i++) {
                sheet.createRow(i + 1);
                for (int j = 0; j < model.getColumnCount(); j++) {
                    Object value = model.getValueAt(i, j);
                    if (value != null)
                        setCell(value, i + 1, j, null);
                }
            }
            CTTableStyleInfo styleInfo = ctTable.addNewTableStyleInfo();
            styleInfo.setName("TableStyleLight11");

            // Resize all the columns to fit their content's size
            for (int i = 0; i < model.getColumnCount(); i++) {
                sheet.autoSizeColumn(i);
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 500);
            }

            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();
            return new Pair<>(true, "Xuất excel thành công.");
        } catch (IOException e) {
            return new Pair<>(false, "Đã xảy ra lỗi khi xuất file Excel. Vui lòng đóng file hoặc kiểm tra định dạng file.");
        }
    }

    public static File openFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn file để lấy dữ liệu");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XLSX files", "xlsx");
        fileChooser.setFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setCurrentDirectory(new File(Objects.requireNonNull(Resource.getPathOutsideJAR())));

        int userChoice = fileChooser.showOpenDialog(Mini_supermarketManagement.homeGUI);
        if (userChoice != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        File file = fileChooser.getSelectedFile();
        if (!file.getAbsolutePath().toLowerCase().endsWith(EXCEL_EXTENSION)) {
            file = new File(file.getAbsolutePath() + EXCEL_EXTENSION);
        }
        if (!file.exists()) {
            JOptionPane.showMessageDialog(Mini_supermarketManagement.homeGUI, "File không tồn tại.");
            return null;
        }
        return file;
    }

    public static File saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn file để lưu dữ liệu");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XLSX files", "xlsx");
        fileChooser.setFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setCurrentDirectory(new File(Objects.requireNonNull(Resource.getPathOutsideJAR())));

        int userChoice = fileChooser.showSaveDialog(Mini_supermarketManagement.homeGUI);
        if (userChoice != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        File file = fileChooser.getSelectedFile();
        if (!file.getAbsolutePath().toLowerCase().endsWith(EXCEL_EXTENSION)) {
            file = new File(file.getAbsolutePath() + EXCEL_EXTENSION);
        }
        if (file.exists()) {
            String[] options = new String[]{"Có", "Không"};
            int choice = JOptionPane.showOptionDialog(Mini_supermarketManagement.homeGUI,
                "File đã tồn tại. Bạn có muốn ghi đè dữ liệu mới không?", "Cảnh báo",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
            if (choice != 0)
                return null;
            return file;
        }
        return file;
    }

    public static Font newFont(int size, boolean bold, boolean italic, boolean strikeout, byte underline) {
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) size);
        font.setColor(IndexedColors.BLACK1.getIndex());
        font.setBold(bold);
        font.setItalic(italic);
        font.setStrikeout(strikeout);
        font.setUnderline(underline);
        return font;
    }

    public static CellStyle newCellStyle(Font font, HorizontalAlignment align) {
        CellStyle cellStyle = workbook.createCellStyle();
        if (font != null)
            cellStyle.setFont(font);
        cellStyle.setAlignment(align);
        return cellStyle;
    }

    public static void setCell(Object value, int row, int column, CellStyle style) {
        Cell cell = sheet.getRow(row).createCell(column);
        if (style != null)
            cell.setCellStyle(style);
        setValue(value, cell);
    }

    public static void setCellRange(Object value, CellRangeAddress rangeAddress, CellStyle style) {
        sheet.addMergedRegion(rangeAddress);
        Cell cell = sheet.getRow(rangeAddress.getFirstRow()).createCell(rangeAddress.getFirstColumn());
        if (style != null)
            cell.setCellStyle(style);
        setValue(value, cell);
    }

    public static Object getValueFrom(Cell cell, CellType type) {
        return switch (type) {
            case STRING, BLANK, _NONE, ERROR -> cell.getStringCellValue();
            case NUMERIC -> cell.getNumericCellValue();
            case BOOLEAN -> cell.getBooleanCellValue();
            case FORMULA -> getValueFrom(cell, cell.getCachedFormulaResultType());
        };
    }

    public static void setValue(Object value, Cell cell) {
        if (value instanceof Number)
            cell.setCellValue(((Number) value).doubleValue());
        else
            cell.setCellValue(value.toString());
    }

    public static Object castValue(Object value, Type type) throws ClassCastException {
        return switch (type) {
            case STRING -> {
                if (value == null)
                    yield "";
                yield (String) value;
            }
            case NUMERIC -> {
                if (value == null)
                    yield 0.0;
                yield (Double) value;
            }
            case BOOLEAN -> {
                if (value == null)
                    yield false;
                yield (Boolean) value;
            }
        };
    }

    public enum Type {
        STRING, NUMERIC, BOOLEAN
    }
}
