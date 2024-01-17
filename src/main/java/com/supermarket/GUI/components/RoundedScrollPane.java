package com.supermarket.GUI.components;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedScrollPane extends JScrollPane {
    public RoundedScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
        super(view,vsbPolicy, hsbPolicy);
        getViewport().setBackground(new Color(0xFFFFFF));
        getVerticalScrollBar().setUnitIncrement(30);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Create a rounded rectangle shape that matches the size of the JScrollPane
        RoundRectangle2D roundedRect = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 50, 50);

        // Create a Graphics2D object
        Graphics2D g2 = (Graphics2D) g;

        // Set anti-aliasing for smoother rounded edges
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Set the background color
        g2.setColor(getBackground());

        // Fill the rounded rectangle with the background color
        g2.fill(roundedRect);
        setBorder(null);
        // Call the parent's paintComponent to draw the scroll pane's content
        super.paintComponent(g);
    }
}

