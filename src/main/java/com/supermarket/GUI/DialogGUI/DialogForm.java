package com.supermarket.GUI.DialogGUI;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.supermarket.GUI.components.RoundedPanel;
import com.supermarket.main.Mini_supermarketManagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DialogForm extends JDialog {
    public RoundedPanel leftContent;
    public RoundedPanel rightContent;
    public RoundedPanel containerTable;
    public RoundedPanel containerButton;

    public DialogForm() {
        super((Frame) null, "", true);
        getContentPane().setBackground(new Color(0xFFFFFF));

        setLayout(new BorderLayout());
        setIconImage(new FlatSVGIcon("img/application_logo.svg").getImage());
        setSize(new Dimension(1300,700));
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(Mini_supermarketManagement.homeGUI);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cancel();
            }
        });
        initComponents();

    }

    private void initComponents() {
        leftContent = new RoundedPanel();
        rightContent = new RoundedPanel();
        containerTable = new RoundedPanel();
        containerButton = new RoundedPanel();

        leftContent.setLayout(new BorderLayout());
        leftContent.setBackground(new Color(0xFFFFFF));
        leftContent.setPreferredSize(new Dimension(600, 700));
        add(leftContent, BorderLayout.WEST);

        rightContent.setLayout(new BorderLayout());
        rightContent.setBackground(new Color(0xFFFFFF));
        rightContent.setPreferredSize(new Dimension(670, 700));
        add(rightContent, BorderLayout.EAST);

        containerTable.setBackground(new Color(0xFFFFFF));
        containerTable.setLayout(new BorderLayout());

        containerButton.setBackground(new Color(0xFFBDD2DB));
        containerButton.setLayout(new FlowLayout());
        containerButton.setPreferredSize(new Dimension(670, 50));
        rightContent.add(containerButton, BorderLayout.SOUTH);
    }

    private void cancel() {
        String[] options = new String[]{"Huỷ", "Thoát"};
        int choice = JOptionPane.showOptionDialog(null, "Bạn có muốn thoát?",
            "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (choice == 1)
            dispose();
    }


}
