package com.supermarket.GUI.components;

import java.awt.*;

public class Layout4 extends Layout1{
    public RoundedPanel leftContent;
    public RoundedPanel rightContent;
    public Layout4() {
        super();
        initComponents();
        setVisible(true);
    }

    public void initComponents() {
        super.initComponents();

        leftContent = new RoundedPanel();
        rightContent = new RoundedPanel();

        bottom.setBackground(new Color(0xFFFFFF));

        leftContent.setLayout(new BorderLayout());
        leftContent.setBackground(new Color(0xA8A8AF));
        leftContent.setPreferredSize(new Dimension(800, 710));
        bottom.add(leftContent, BorderLayout.CENTER);

        rightContent.setLayout(new BorderLayout());
        rightContent.setBackground(new Color(0xA8A8AF));
        rightContent.setPreferredSize(new Dimension(350, 710));
    }
}
