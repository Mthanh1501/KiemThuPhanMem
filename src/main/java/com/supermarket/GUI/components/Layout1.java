package com.supermarket.GUI.components;

import java.awt.*;

public class Layout1 extends RoundedPanel {
    public RoundedPanel top;
    public RoundedPanel bottom;
    public RoundedPanel leftMenu;
    public RoundedPanel rightMenu;

    public Layout1() {
        initComponents();
        setVisible(true);
    }

    public void initComponents() {
        setBackground(new Color(0xFFFFFF));
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1140, 760));

        top = new RoundedPanel();
        bottom = new RoundedPanel();
        leftMenu = new RoundedPanel();
        rightMenu = new RoundedPanel();

        top.setLayout(new BorderLayout());
        top.setBackground(new Color(0xFFFFFF));
        top.setPreferredSize(new Dimension(1140, 50));
        add(top, BorderLayout.NORTH);

        bottom.setLayout(new BorderLayout());
        bottom.setBackground(new Color(0xA8A8AF));
        bottom.setPreferredSize(new Dimension(1140, 710));
        add(bottom, BorderLayout.SOUTH);

        leftMenu.setLayout(new FlowLayout(FlowLayout.CENTER));
        leftMenu.setPreferredSize(new Dimension(650, 50));
        top.add(leftMenu, BorderLayout.WEST);

        rightMenu.setLayout(new FlowLayout(FlowLayout.CENTER));
        rightMenu.setPreferredSize(new Dimension(490, 50));
        top.add(rightMenu, BorderLayout.EAST);
    }
}
