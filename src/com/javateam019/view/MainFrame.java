package com.javateam019.view;

import com.sun.tools.javac.Main;
import com2008.App;

import javax.swing.*;
import java.awt.*;

public class MainFrame {
    private JPanel mainPanel;


    private static JFrame frame = new JFrame("MainFrame");

    public static void showMainFrame() {
        frame.setContentPane(new MainFrame().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenDimensions = toolkit.getScreenSize();
        frame.setSize(screenDimensions.width, screenDimensions.height);

        frame.setVisible(true);
    }

}
