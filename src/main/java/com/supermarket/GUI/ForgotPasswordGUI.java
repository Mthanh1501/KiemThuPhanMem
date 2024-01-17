package com.supermarket.GUI;

import com.supermarket.BLL.AccountBLL;
import com.supermarket.BLL.StaffBLL;
import com.supermarket.DTO.Account;
import com.supermarket.DTO.Staff;
import com.supermarket.GUI.DialogGUI.SmallDialog;
import com.supermarket.main.Mini_supermarketManagement;
import com.supermarket.utils.DateTime;
import com.supermarket.utils.Email;
import com.supermarket.utils.Password;
import com.supermarket.utils.VNString;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Map;

public class ForgotPasswordGUI extends JDialog {
    private String activeOtp;
    private Account account;
    private String email;
    private JPanel otpEnterEmail;
    private JPanel otpConfirmPanel;
    private JPanel otpChangePassword;
    private int step;
    private long seconds = 180;
    private Thread currentCountDownThread;

    public ForgotPasswordGUI() {
        super((Frame) null, "Quên mật khẩu", true);

        otpEnterEmail = new JPanel(new MigLayout());
        otpEnterEmail.setPreferredSize(new Dimension(500, 300));

        otpConfirmPanel = new JPanel(new MigLayout());
        otpConfirmPanel.setPreferredSize(new Dimension(500, 300));

        otpChangePassword = new JPanel(new MigLayout());
        otpChangePassword.setPreferredSize(new Dimension(500, 300));

        account = new Account();
        email = "";
        toStep(step = 1);
        setSize(500, 300);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(Mini_supermarketManagement.loginGUI);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cancel();
            }
        });
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    private void showEnterEmail() {
        otpEnterEmail.removeAll();

        JLabel lbEnterEmail = new JLabel("Nhập email của bạn",  SwingConstants.CENTER);
        lbEnterEmail.setFont(new Font("Arial", Font.BOLD, 20));
        lbEnterEmail.setPreferredSize( new Dimension(500, 50));
        otpEnterEmail.add(lbEnterEmail, "span, center");

        JTextField txtEnterEmail = new JTextField();
        txtEnterEmail.setPreferredSize(new Dimension(350, 40));
        otpEnterEmail.add(txtEnterEmail, "span, center, wrap");

        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(new EmptyBorder(15,0,0,0));
        otpEnterEmail.add(panel, "wrap,center");

        JButton[] buttons = new JButton[2];
        buttons[0] = new JButton("Huỷ");
        buttons[0].setBackground(new Color(0x018847));
        buttons[0].setForeground(new Color(0xFFFFFF));
        buttons[0].setFont(new Font("Arial", Font.BOLD, 12));
        buttons[0].addActionListener(e -> dispose());
        panel.add(buttons[0]);

        buttons[1] = new JButton("Tiếp tục");
        buttons[1].setBackground(new Color(0x018847));
        buttons[1].setForeground(new Color(0xFFFFFF));
        buttons[1].setFont(new Font("Arial", Font.BOLD, 12));
        buttons[1].addActionListener(e -> validateStep1(txtEnterEmail.getText()));
        panel.add(buttons[1]);
    }

    private void showConfirmPanel() {
        otpConfirmPanel.removeAll();

        JLabel username = new JLabel(account.getUsername(), SwingConstants.CENTER);
        username.setFont(new Font("Arial", Font.BOLD, 20));
        username.setPreferredSize(new Dimension(500, 32));
        otpConfirmPanel.add(username, "span,center");

        JLabel label2 = new JLabel("Vui lòng nhập mã vào ô bên dưới.", SwingConstants.CENTER);
        label2.setFont(new Font("Arial", Font.BOLD, 14));
        label2.setPreferredSize(new Dimension(500, 32));
        otpConfirmPanel.add(label2, "span,center,wrap");

        JTextField textField = new JTextField();
        textField.setFont(new Font("Arial", Font.BOLD, 20));
        textField.setPreferredSize(new Dimension(90, 40));
        textField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                JTextField textField = (JTextField) input;
                String text = textField.getText();
                return text.matches("\\d{1,6}"); // Only allow 1 to 6 digits
            }
        });
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                currentText = currentText.substring(0, offset) + text + currentText.substring(offset + length);

                if (currentText.matches("\\d{0,6}")) { // Only allow 0 to 6 digits
                    super.replace(fb, offset, length, text, attrs);
                    if (currentText.length() == 6)
                        validateStep2(currentText);
                }
            }
        });
        otpConfirmPanel.add(textField, "wrap,center,span");

        JLabel nothing = new JLabel();
        nothing.setPreferredSize(new Dimension(20, 10));
        otpConfirmPanel.add(nothing, "wrap,center");

        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(new EmptyBorder(15,0,0,0));
        otpConfirmPanel.add(panel, "wrap,center");

        JButton[] buttons = new JButton[3];
        buttons[0] = new JButton();
        buttons[0].setText("Gửi lại");
        buttons[0].setFont(new Font("Arial", Font.PLAIN, 12));
        buttons[0].setBackground(new Color(0x018847));
        buttons[0].setForeground(new Color(0xFFFFFF));
        buttons[0].addActionListener(e -> {
            sendOTP(nothing);
        });
        panel.add(buttons[0]);

        buttons[1] = new JButton();
        buttons[1].setText("Quay lại");
        buttons[1].setFont(new Font("Arial", Font.PLAIN, 12));
        buttons[1].setBackground(new Color(0x018847));
        buttons[1].setForeground(new Color(0xFFFFFF));
        buttons[1].addActionListener(e -> toStep(--step));
        panel.add(buttons[1]);

        buttons[2] = new JButton();
        buttons[2].setText("Hủy");
        buttons[2].setFont(new Font("Arial", Font.PLAIN, 12));
        buttons[2].setBackground(new Color(0x018847));
        buttons[2].setForeground(new Color(0xFFFFFF));
        buttons[2].addActionListener(e -> this.dispose());
        panel.add(buttons[2]);

        sendOTP(nothing);
    }

    private void showChangePassword() {
        JLabel title = new JLabel("Vui lòng đổi lại mật khẩu", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setPreferredSize(new Dimension(500, 50));
        otpChangePassword.add(title, "span,center");

        JLabel label1 = new JLabel("Nhập mật khẩu mới", SwingConstants.CENTER);
        label1.setFont(new Font("Arial", Font.BOLD, 14));
        label1.setPreferredSize(new Dimension(200, 32));
        otpChangePassword.add(label1, "span,center,wrap");

        JPasswordField passwordField1 = new JPasswordField();
        JPasswordField passwordField2 = new JPasswordField();

        passwordField1.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField1.setPreferredSize(new Dimension(200, 32));
        passwordField1.addActionListener(e -> {
            String password = new String(passwordField1.getPassword());
            String confirm = new String(passwordField2.getPassword());
            validateStep3(password, confirm);
        });
        otpChangePassword.add(passwordField1, "span,center,wrap");

        JLabel label2 = new JLabel("Nhập lại mật khẩu", SwingConstants.CENTER);
        label2.setFont(new Font("Arial", Font.BOLD, 14));
        label2.setPreferredSize(new Dimension(200, 32));
        otpChangePassword.add(label2, "span,center,wrap");

        passwordField2.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField2.setPreferredSize(new Dimension(200, 32));
        passwordField2.addActionListener(e -> {
            String password = new String(passwordField1.getPassword());
            String confirm = new String(passwordField2.getPassword());
            validateStep3(password, confirm);
        });
        otpChangePassword.add(passwordField2, "span,center,wrap");

        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(new EmptyBorder(15,0,0,0));
        otpChangePassword.add(panel, "span,center,wrap");

        JButton[] buttons = new JButton[2];
        buttons[0] = new JButton();
        buttons[0].setText("Xác nhận");
        buttons[0].setFont(new Font("Arial", Font.PLAIN, 12));
        buttons[0].setBackground(new Color(0x018847));
        buttons[0].setForeground(new Color(0xFFFFFF));
        buttons[0].addActionListener(e -> {
            String password = new String(passwordField1.getPassword());
            String confirm = new String(passwordField2.getPassword());
            validateStep3(password, confirm);
        });
        panel.add(buttons[0]);

        buttons[1] = new JButton();
        buttons[1].setText("Hủy");
        buttons[1].setFont(new Font("Arial", Font.PLAIN, 12));
        buttons[1].setBackground(new Color(0x018847));
        buttons[1].setForeground(new Color(0xFFFFFF));
        buttons[1].addActionListener(e -> this.dispose());
        panel.add(buttons[1]);
    }


    private void cancel() {
        String[] options = new String[]{"Huỷ", "Thoát chương trình"};
        int choice = JOptionPane.showOptionDialog(null, "Bạn có muốn thoát chương trình?",
            "Lỗi", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
        if (choice == 1)
            Mini_supermarketManagement.exit(1);
    }

    public void toStep(int step){
        JPanel panel = new JPanel();
        switch (step) {
            case 1 -> { showEnterEmail(); panel = otpEnterEmail; }
            case 2 -> { showConfirmPanel(); panel = otpConfirmPanel; }
            case 3 -> { showChangePassword(); panel = otpChangePassword; }
        }
        setContentPane(panel);
        repaint();
        revalidate();
    }

    private void sendOTP(JLabel nothing)    {
        if (currentCountDownThread != null)
            currentCountDownThread.interrupt();
        currentCountDownThread = new Thread(() -> {
            activeOtp = Email.getOTP();
            nothing.setText("Hệ thống đang gửi mã OTP...");
            Email.sendOTP(email, "Đặt lại mật khẩu Bách Hoá Xanh", activeOtp);
            DateTime start = new DateTime();
            long temp = 0;
            while (seconds - temp > 0 && !Thread.currentThread().isInterrupted()) {
                temp = DateTime.calculateTime(start, new DateTime());
                nothing.setText("(" + (seconds - temp) + "s)");
            }
            activeOtp = "";
            nothing.setText("Mã OTP đã hết thời gian hiệu lực vui lòng chọn gửi lại.");
        });
        currentCountDownThread.start();
    }

    private void validateStep1(String email) {
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập email của bạn.");
            return;
        }
        if(!VNString.checkFormatOfEmail(email)){
            SmallDialog.showResult("Vui lòng nhập email đúng định dạng");
        }
        List<Staff> foundStaffs = new StaffBLL().findStaffsBy(Map.of("email", email));
        if (foundStaffs.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy tài khoản.");
            return;
        }
        List<Account> foundAccounts = new AccountBLL().findAccountsBy(Map.of("staff_id", foundStaffs.get(0).getId()));
        if (foundAccounts.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy tài khoản.");
            return;
        }
        account = foundAccounts.get(0);
        this.email = email;
        toStep(++step);
    }

    private void validateStep2(String otp) {
        if (otp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã OTP.");
            return;
        }
        if (otp.length() != 6) {
            JOptionPane.showMessageDialog(this, "Mã OTP gồm 6 chữ số.");
            return;
        }

        if (!activeOtp.equals(otp)) {
            JOptionPane.showMessageDialog(this, "Mã OTP không đúng. Vui lòng thử lại");
            return;
        }
        toStep(++step);
    }

    private void validateStep3(String password, String confirm) {
        if (password.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin.");
            return;
        }
        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Nhập lại mật khẩu không trùng khớp với mật khẩu mới.");
            return;
        }
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[^\\s]{3,32}$")) {
            JOptionPane.showMessageDialog(this, "Mật khẩu không được chứa khoảng trắng.\nMật khẩu phải chứa ít nhất 1 chữ cái thường, 1 chữ cái hoa and 1 chữ số");
            return;
        }
        String hashedPassword = Password.hashPassword(password);
        account.setPassword(hashedPassword);
        if (!new AccountBLL().updateAccountPassword(account)) {
            JOptionPane.showMessageDialog(this, "Thay đổi mật khẩu không thành công. Vui lòng thử lại sau.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Thay đổi mật khẩu thành công.");
        this.dispose();
    }
    public static void main(String[] args) {
        new ForgotPasswordGUI();
//        System.out.println(DateTime.calculateTime(new DateTime(2023, 10, 14, 19, 51, 10, 0), DateTime.now()));

    }
}
