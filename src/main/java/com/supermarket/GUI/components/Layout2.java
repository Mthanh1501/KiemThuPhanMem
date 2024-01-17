package com.supermarket.GUI.components;

import net.miginfocom.swing.MigLayout;

import java.awt.*;

public class Layout2 extends Layout1{
    public RoundedPanel leftContent;
    public RoundedPanel rightContent;
    public Layout2() {
        super();
        initComponents();
        setVisible(true);
    }

    public void initComponents(){
        super.initComponents();

        leftContent = new RoundedPanel();
        rightContent = new RoundedPanel();

        bottom.setBackground(new Color(0xFFFFFF));

        leftContent.setLayout(new MigLayout("", "10[]20[]10", "20[]20[]"));
        leftContent.setBackground(new Color(0xFFBDD2DB));
        leftContent.setPreferredSize(new Dimension(350, 710));
        bottom.add(leftContent, BorderLayout.WEST);

        rightContent.setLayout(new BorderLayout());
        rightContent.setBackground(new Color(0xA8A8AF));
        rightContent.setPreferredSize(new Dimension(800, 710));
        bottom.add(rightContent, BorderLayout.EAST);

    }
}
