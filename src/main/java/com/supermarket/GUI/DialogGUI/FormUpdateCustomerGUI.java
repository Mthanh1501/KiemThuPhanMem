package com.supermarket.GUI.DialogGUI;

import com.supermarket.BLL.CustomerBLL;
import com.supermarket.BLL.RoleBLL;
import com.supermarket.BLL.StaffBLL;
import com.supermarket.DTO.Customer;
import com.supermarket.GUI.CustomerGUI;
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

public class FormUpdateCustomerGUI extends DialogForm {
    private final CustomerBLL customerBLL = new CustomerBLL();
    private final StaffBLL staffBLL = new StaffBLL();
    private final RoleBLL roleBLL = new RoleBLL();
    private DataTable dataTable;
    private RoundedPanel formDetail;
    private RoundedScrollPane scrollPaneFormDetail;
    private RoundedScrollPane scrollPaneDatatable;
    private java.util.List<JLabel> attributeCustomer;
    private List<JComponent> jComponentCustomer;
    private JButton buttonCancel;
    private JButton buttonUpdate;
    private Customer customer;
    public FormUpdateCustomerGUI(Customer customer) {
        super();
        this.customer = customer;
        super.setTitle("Quản lý khách hàng");
        init();
        containerButton.setBackground(new Color(0xFFFFFF));
        setVisible(true);
    }

    public void init() {
        dataTable = new DataTable(new Object[][] {}, new String[] {}, e -> {});
        formDetail = new RoundedPanel();
        attributeCustomer = new ArrayList<>();
        jComponentCustomer = new ArrayList<>();
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

        for (String string : new String[]{"Mã khách hàng:", "Tên khách hàng:", "Giới tính:", "Ngày sinh:", "Số điện thoại:", "Thành viên:", "Ngày đăng ký thành viên:", "Điểm thưởng:"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("FlatLaf.style", Font.PLAIN, 16)));
            attributeCustomer.add(label);
            formDetail.add(label);

            JTextField textField = new JTextField();
            if (string.equals("Mã khách hàng:")) {
                textField.setText(String.valueOf(customer.getId()));
                textField.setEnabled(false);
            }
            String[] gender = {"Nam", "Nữ"};
            JComboBox cbbGender = new JComboBox<>(gender);
            String[] membership = {"Có","Không"};
            JComboBox cbbMembership = new JComboBox<>(membership);
            if (string.equals("Tên khách hàng:")) {
                textField.setText(customer.getName());
            }
            if (string.equals("Ngày sinh:")) {
                textField.setText(String.valueOf(customer.getBirthday()));
            }
            if (string.equals("Số điện thoại:")) {
                textField.setText(customer.getPhone());
            }
            if (string.equals("Ngày đăng ký thành viên:")) {
                textField.setText(String.valueOf(customer.getSigned_up_date()));
                textField.setEnabled(false);
            }
            if (string.equals("Điểm thưởng:")) {
                textField.setText(String.valueOf(customer.getPoint()));
            }
            if(string.equals("Thành viên:")){
                cbbMembership.setPreferredSize(new Dimension(400, 50));
                cbbMembership.setFont((new Font("FlatLaf.style", Font.BOLD, 14)));
                jComponentCustomer.add(cbbMembership);
                formDetail.add(cbbMembership, "wrap");
            }else if(string.equals("Giới tính:")){
                if(customer.isGender() == true){
                    cbbGender.setSelectedItem("Nam");
                }else{
                    cbbGender.setSelectedItem("Nữ");
                }
                cbbGender.setPreferredSize(new Dimension(400, 50));
                cbbGender.setFont((new Font("FlatLaf.style", Font.BOLD, 14)));
                jComponentCustomer.add(cbbGender);
                formDetail.add(cbbGender, "wrap");
            }else{
                textField.setPreferredSize(new Dimension(400, 50));
                textField.setFont((new Font("FlatLaf.style", Font.BOLD, 14)));
                jComponentCustomer.add(textField);
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
                updateCustomer();
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

    private void updateCustomer() {
        for (int i = 0; i < jComponentCustomer.size(); i++) {
            if (i != 6 && getValueFromComponent(jComponentCustomer.get(i)).isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin khách hàng.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }


        try{
            int id = Integer.parseInt(getValueFromComponent(jComponentCustomer.get(0)));
            String customerName = getValueFromComponent(jComponentCustomer.get(1));
            boolean gender = getValueFromComponent(jComponentCustomer.get(2)).equals("Nam")?true:false;
            Date birthDate = Date.parseDate(getValueFromComponent(jComponentCustomer.get(3)));
            String phoneNumber = getValueFromComponent(jComponentCustomer.get(4));
            boolean isMember = getValueFromComponent(jComponentCustomer.get(5)).equals("Không")?false:true;
            Date lastSignedIn = Date.parseDate(getValueFromComponent(jComponentCustomer.get(6)));
            int bonusPoint = Integer.parseInt(getValueFromComponent(jComponentCustomer.get(7)));

            Customer customer = new Customer(id,customerName,gender,birthDate,phoneNumber,isMember,lastSignedIn,bonusPoint,false);

            String[] options = new String[]{"Huỷ", "Xác nhận"};
            int choice = JOptionPane.showOptionDialog(null, "Xác nhận cập nhật khách hàng?",
                "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            if (choice == 1) {
                if (customerBLL.updateCustomer(customer)) {
                    JOptionPane.showMessageDialog(null, "Cập nhật khách hàng thành công!",
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);

                    CustomerGUI.loadDataTable(customerBLL.getData());
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Cập nhật khách hàng không thành công!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        catch(Exception e){
            System.out.println(e.getLocalizedMessage());
        }
    }
}
