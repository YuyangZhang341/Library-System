package com2008;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App {
    private JPanel mainPanel;
    private JPanel buttonsPanel;

    private JButton viewArticlesButton;
    private JButton logInButton;
    private JButton submitArticleButton;
    private JLabel imageLabelLeft;
    private JPanel leftPanel;

    private static JFrame frame = new JFrame("App");

    public App() {
        viewArticlesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new JournalsView().showJournalsView();

                frame.dispose();
            }
        });

        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LogIn().showLogInPage();//Test01

                LogIn.main();
            }
        });

        submitArticleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new AddSubmissionView().showAddSubmissionView();

                frame.dispose();
            }
        });
    }

    public static void main(String[] args) {
        frame.setContentPane(new App().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenDimensions = toolkit.getScreenSize();
        frame.setSize(screenDimensions.width, screenDimensions.height);

        frame.setVisible(true);
    }
}
