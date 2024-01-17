package com.supermarket.GUI.components;

import java.awt.*;

public class Layout3 extends RoundedPanel{
    public RoundedPanel top;
    public RoundedPanel bottom;
    public RoundedPanel leftMenu;
    public RoundedPanel rightMenu;
    public RoundedPanel leftContent;
    public RoundedPanel rightContent;
    public RoundedPanel topContent;
    public RoundedPanel bottomContent;
    public Layout3() {
        super();
        initComponents();
    }

    public void initComponents(){
        setBorderColor(new Color(0xFFFFFF));
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1140, 760));

        top = new RoundedPanel();
        bottom = new RoundedPanel();
        leftMenu = new RoundedPanel();
        rightMenu = new RoundedPanel();

        bottom.setLayout(new BorderLayout());
        bottom.setBackground(new Color(0xA8A8AF));
        bottom.setPreferredSize(new Dimension(1140, 760));
        add(bottom, BorderLayout.CENTER);

        leftContent = new RoundedPanel();
        rightContent = new RoundedPanel();
        topContent = new RoundedPanel();
        bottomContent = new RoundedPanel();

        bottom.setBackground(new Color(0xFFFFFF));

        leftContent.setLayout(new BorderLayout());
        leftContent.setBackground(new Color(0xFFFFFF));
        leftContent.setPreferredSize(new Dimension(800, 760));
        bottom.add(leftContent, BorderLayout.WEST);

        rightContent.setLayout(new BorderLayout());
        rightContent.setBackground(new Color(0xFFFFFF));
        rightContent.setPreferredSize(new Dimension(350, 760));
        bottom.add(rightContent, BorderLayout.EAST);

        topContent.setLayout(new BorderLayout());
//        topContent.setBackground(new Color(0xA8A8AF));
        topContent.setPreferredSize(new Dimension(800, 375));
        leftContent.add(topContent, BorderLayout.NORTH);

        bottomContent.setLayout(new BorderLayout());
//        bottomContent.setBackground(new Color(0xA8A8AF));
        bottomContent.setPreferredSize(new Dimension(800, 375));
        leftContent.add(bottomContent, BorderLayout.SOUTH);

    }
}
