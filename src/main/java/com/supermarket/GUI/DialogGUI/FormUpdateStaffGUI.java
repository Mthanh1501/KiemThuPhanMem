package com.supermarket.GUI.DialogGUI;

import com.supermarket.BLL.RoleBLL;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class FormUpdateStaffGUI extends DialogForm {
    private final StaffBLL staffBLL = new StaffBLL();
    private final RoleBLL roleBLL = new RoleBLL();
    private DataTable dataTable;
    private RoundedPanel formDetail;
    private RoundedScrollPane scrollPaneFormDetail;
    private RoundedScrollPane scrollPaneDatatable;
    private java.util.List<JLabel> attributeStaff;
    private List<JComponent> jComponentsStaff;
    private JButton buttonCancel;
    private JButton buttonUpdate;
    private Staff staff;
    public FormUpdateStaffGUI(Staff staff) {
        super();
        this.staff = staff;
        super.setTitle("Sửa nhân viên");
        init();
        containerButton.setBackground(new Color(0xFFFFFF));
        setVisible(true);
    }

    public void init() {
        dataTable = new DataTable(new Object[][] {}, new String[] {}, e -> {});
        formDetail = new RoundedPanel();
        attributeStaff = new ArrayList<>();
        jComponentsStaff = new ArrayList<>();
        buttonCancel = new JButton("Huỷ");
        buttonUpdate = new JButton("Cập nhật");
        scrollPaneDatatable = new RoundedScrollPane(containerTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneFormDetail = new RoundedScrollPane(formDetail, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);


        scrollPaneDatatable.setPreferredSize(new Dimension(600, 700));
        leftContent.add(scrollPaneDatatable, BorderLayout.CENTER);
        containerTable.add(dataTable);

        scrollPaneFormDetail.setPreferredSize(new Dimension(670, 610));
        rightContent.add(scrollPaneFormDetail, BorderLayout.NORTH);

        formDetail.setBackground(new Color(0xFFBDD2DB));
        formDetail.setLayout(new MigLayout("", "50[]20[]10", "20[]20[]"));

        for (String string : new String[]{"Mã nhân viên:", "Tên nhân viên:", "Giới tính:", "Ngày sinh:", "Số điện thoại:", "Địa chỉ:", "Email:", "Ngày vào làm:"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("FlatLaf.style", Font.PLAIN, 16)));
            attributeStaff.add(label);
            formDetail.add(label);

            JTextField textField = new JTextField();
            String[] gender = {"Nam", "Nữ"};
            JComboBox cbbGender = new JComboBox<>(gender);
            if (string.equals("Mã nhân viên:")) {
                textField.setText(String.valueOf(staff.getId()));
                textField.setEnabled(false);
            }
            if (string.equals("Tên nhân viên:")) {
                textField.setText(staff.getName());
            }

            if (string.equals("Ngày sinh:")) {
                textField.setText(String.valueOf(staff.getBirthday()));
            }
            if (string.equals("Số điện thoại:")) {
                textField.setText(staff.getPhone());
            }

            if (string.equals("Địa chỉ:")) {
                textField.setText(staff.getAddress());
            }
            if (string.equals("Email:")) {
                textField.setText(staff.getEmail());
            }
            if (string.equals("Ngày vào làm:")) {
                textField.setText(String.valueOf(staff.getEntry_date()));
            }
            if(string.equals("Giới tính:")){
                if(staff.getGender() == true){
                    cbbGender.setSelectedItem("Nam");
                }else{
                    cbbGender.setSelectedItem("Nữ");
                }
                cbbGender.setPreferredSize(new Dimension(400, 50));
                cbbGender.setFont((new Font("FlatLaf.style", Font.BOLD, 14)));
                jComponentsStaff.add(cbbGender);
                formDetail.add(cbbGender, "wrap");
            }else{
                textField.setPreferredSize(new Dimension(400, 50));
                textField.setFont((new Font("FlatLaf.style", Font.BOLD, 14)));
                jComponentsStaff.add(textField);
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

        buttonUpdate.setPreferredSize(new Dimension(100,40));
        buttonUpdate.setFont(new Font("FlatLaf.style", Font.BOLD, 15));
        buttonUpdate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonUpdate.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                updateStaff();
            }
        });
        containerButton.add(buttonUpdate);

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

    private void updateStaff() {
        for (int i = 0; i < jComponentsStaff.size(); i++) {
            if (i != 6 && getValueFromComponent(jComponentsStaff.get(i)).isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin khách hàng.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }


        try{
            int id = Integer.parseInt(getValueFromComponent(jComponentsStaff.get(0)));
            String staffName = getValueFromComponent(jComponentsStaff.get(1));
            boolean gender = getValueFromComponent(jComponentsStaff.get(2)).equals("Nam")?true:false;
            Date birthDate = Date.parseDate(getValueFromComponent(jComponentsStaff.get(3)));
            String phoneNumber = getValueFromComponent(jComponentsStaff.get(4));
            String address = getValueFromComponent(jComponentsStaff.get(5));
            String email = getValueFromComponent(jComponentsStaff.get(6));
            Date entry_date = Date.parseDate(getValueFromComponent(jComponentsStaff.get(7)));

            Staff staff = new Staff(id,staffName,gender,birthDate,phoneNumber,address,email,entry_date,false);

            String[] options = new String[]{"Huỷ", "Xác nhận"};
            int choice = JOptionPane.showOptionDialog(null, "Xác nhận cập nhật khách hàng?",
                "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            if (choice == 1) {
                if (staffBLL.updateStaff(staff).getKey()) {
                    JOptionPane.showMessageDialog(null, "Cập nhật nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    StaffGUI.loadDataTable(staffBLL.getData());
                    dispose();
                } else {
                    SmallDialog.showResult(staffBLL.updateStaff(staff).getValue());
                }
            }
        }
        catch(Exception e){
            System.out.println(e.getLocalizedMessage());
        }
    }

}
