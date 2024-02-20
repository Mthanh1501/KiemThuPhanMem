package com.supermarket.main;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.supermarket.GUI.HomeGUI;
import com.supermarket.GUI.LoginGUI;

import javax.swing.*;
import java.awt.*;

public class Mini_supermarketManagement {
    public static LoginGUI loginGUI;
    public static HomeGUI homeGUI;

    public static void main(String[] args) {
        FlatIntelliJLaf.registerCustomDefaultsSource("style");
        FlatIntelliJLaf.setup();
        UIManager.put("ProgressBar.selectionForeground", Color.black);
        UIManager.put("ProgressBar.selectionBackground", Color.black);
        UIManager.put( "ScrollBar.trackArc", 999 );
        UIManager.put( "ScrollBar.thumbArc", 999 );
        UIManager.put( "ScrollBar.trackInsets", new Insets( 2, 4, 2, 4 ) );
        UIManager.put( "ScrollBar.thumbInsets", new Insets( 2, 2, 2, 2 ) );
        UIManager.put( "ScrollBar.track", new Color(0xAE8EBCDA, true) );
        UIManager.put("PasswordField.showRevealButton", true);
        UIManager.put("PasswordField.capsLockIcon", new FlatSVGIcon("icon/capslock.svg"));
        UIManager.put("TitlePane.iconSize", new Dimension(25, 25));
        UIManager.put("TitlePane.iconMargins", new Insets(3, 5, 0, 20));

        Thread thread = new Thread(() -> homeGUI = new HomeGUI());
        thread.start();
        loginGUI = new LoginGUI();
        loginGUI.setVisible(true);//test tai sao no ko luu
    }

    public static void exit(int status) {
        System.exit(status);
    }
}
