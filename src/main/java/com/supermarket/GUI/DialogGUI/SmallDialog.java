package com.supermarket.GUI.DialogGUI;

import javax.swing.*;

public class SmallDialog {
    public static void showResult(String message) {
        JOptionPane.showMessageDialog(null, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
