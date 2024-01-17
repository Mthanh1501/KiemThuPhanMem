package com.supermarket.GUI.DialogGUI;


import com.supermarket.BLL.StaffBLL;
import com.supermarket.DTO.Staff;
import com.supermarket.GUI.StaffGUI;
import com.supermarket.GUI.components.DataTable;
import com.supermarket.GUI.components.RoundedPanel;
import com.supermarket.GUI.components.RoundedScrollPane;
import com.supermarket.utils.Date;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;


public class FormAddStaffGUI extends DialogForm{
    private final StaffBLL staffBLL = new StaffBLL();

    private DataTable dataTable;
    private RoundedPanel formDetail;
    private RoundedScrollPane scrollPaneFormDetail;
    private RoundedScrollPane scrollPaneDatatable;
    private List<JLabel> attributeStaff;
    private List<JComponent> jComponentStaff;
    private JButton buttonCancel;
    private JButton buttonAdd;
    private boolean flag;

    public FormAddStaffGUI() {
        super();
        super.setTitle("Thêm nhân viên");
        init();
        containerButton.setBackground(new Color(0xFFFFFF));
        setVisible(true);
    }

    public void init() {
        dataTable = new DataTable(new Object[][] {}, new String[] {}, e -> {});
        formDetail = new RoundedPanel();
        attributeStaff = new ArrayList<>();
        jComponentStaff = new ArrayList<>();
        buttonCancel = new JButton("Huỷ");
        buttonAdd = new JButton("Thêm");
        scrollPaneDatatable = new RoundedScrollPane(containerTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneFormDetail = new RoundedScrollPane(formDetail, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);


        scrollPaneDatatable.setPreferredSize(new Dimension(600, 700));
        leftContent.add(scrollPaneDatatable, BorderLayout.CENTER);
        containerTable.add(dataTable);

        scrollPaneFormDetail.setPreferredSize(new Dimension(670, 610));
        rightContent.add(scrollPaneFormDetail, BorderLayout.NORTH);

        formDetail.setBackground(new Color(0xFFBDD2DB));
        formDetail.setLayout(new MigLayout("", "50[]20[]10", "20[]20[]"));

        for (String string : new String[]{"Mã nhân viên:", "Tên nhân viên:", "Giới tính:", "Ngày sinh:", "Số điện thoại:", "Địa chỉ:","Email:","Ngày vào làm:"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("FlatLaf.style", Font.PLAIN, 16)));
            attributeStaff.add(label);
            formDetail.add(label);

            JTextField textField = new JTextField();
            if (string.equals("Mã nhân viên:")) {
                textField.setText(String.valueOf(staffBLL.getAutoID(staffBLL.searchStaffs())));
                textField.setEnabled(false);
            }
            String[] gender = {"Nam", "Nữ"};
            JComboBox cbbGender = new JComboBox<>(gender);
            if(string.equals("Ngày sinh:")){
                addPlaceholder(textField, "yyyy-mm-dd");
            }
            if (string.equals("Ngày vào làm:")) {
                addPlaceholder(textField, "yyyy-mm-dd");
            }
            if(string.equals("Giới tính:")){
                cbbGender.setPreferredSize(new Dimension(400, 50));
                cbbGender.setFont((new Font("FlatLaf.style", Font.BOLD, 14)));
                jComponentStaff.add(cbbGender);
                formDetail.add(cbbGender, "wrap");
            }else{
                textField.setPreferredSize(new Dimension(400, 50));
                textField.setFont((new Font("FlatLaf.style", Font.BOLD, 14)));
                jComponentStaff.add(textField);
                formDetail.add(textField, "wrap");
            }
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
                dispose();
            }
        });
        containerButton.add(buttonCancel);

        buttonAdd.setPreferredSize(new Dimension(100,40));
        buttonAdd.setFont(new Font("FlatLaf.style", Font.BOLD, 15));
        buttonAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                addStaff();
            }
        });
        containerButton.add(buttonAdd);

    }
    private static void addPlaceholder(JTextField textField, String placeholder) {
        textField.setForeground(Color.GRAY);
        textField.setText(placeholder);

        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeholder);
                }
            }
        });
    }
    private String getValueFromComponent(JComponent component) {
        if (component instanceof JTextField) {
            return ((JTextField) component).getText();
        } else if (component instanceof JComboBox) {
            Object selectedValue = ((JComboBox<?>) component).getSelectedItem();
            return (selectedValue != null) ? selectedValue.toString() : "";
        } else {
            // Xử lý các loại JComponent khác ở đây (nếu cần)
            return ""; // Trả về một giá trị mặc định hoặc xử lý đặc biệt cho các loại khác
        }
    }
    private void addStaff() {
        for (int i = 0; i < jComponentStaff.size(); i++) {
            if (i != 6 && getValueFromComponent(jComponentStaff.get(i)).isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin nhân viên.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        try{
            int id = Integer.parseInt(getValueFromComponent(jComponentStaff.get(0)));
            String staffName = getValueFromComponent(jComponentStaff.get(1));
            boolean gender = getValueFromComponent(jComponentStaff.get(2)).equals("Nam")?true:false;
            Date birthDate = Date.parseDate(getValueFromComponent(jComponentStaff.get(3)));
            String phoneNumber = getValueFromComponent(jComponentStaff.get(4));
            String address = getValueFromComponent(jComponentStaff.get(5));
            String email = getValueFromComponent(jComponentStaff.get(6));
            Date entry_date = Date.parseDate(getValueFromComponent(jComponentStaff.get(7)));

            Staff staff = new Staff(id,staffName,gender,birthDate,phoneNumber,address,email,entry_date,false);

            String[] options = new String[]{"Huỷ", "Xác nhận"};
            int choice = JOptionPane.showOptionDialog(null, "Xác nhận thêm nhân viên?",
                "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            if (choice == 1) {
                if (staffBLL.addStaff(staff).getKey()) {
                    JOptionPane.showMessageDialog(null, "Thêm nhân viên thành công!",
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    StaffGUI.loadDataTable(staffBLL.getData());
                    dispose();
                } else {
                    SmallDialog.showResult(staffBLL.addStaff(staff).getValue());
                }
            }
        }
        catch(Exception e){
            if(e.getMessage().equals("Invalid day.")){
                JOptionPane.showMessageDialog(null, "Định dạng ngày không hợp lệ.", "Thông báo", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
