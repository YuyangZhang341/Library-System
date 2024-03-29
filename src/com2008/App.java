package com2008;

import com.javateam019.view.newlogin;

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
    private JButton createJournalButton;

    private static JFrame frame = new JFrame("App");

    public App() {
        viewArticlesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JournalsView.showJournalsView();

                frame.dispose();
            }
        });

        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newlogin.showLogIn();

                frame.dispose();
            }
        });

        submitArticleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddSubmissionView.showAddSubmissionView();

                frame.dispose();
            }
        });

        createJournalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateJournalView.showCreateJournalView();
            }
        });
    }

    public static void showMainApp() {
        frame.setContentPane(new App().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(Util.WIDTH, Util.HEIGHT);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        showMainApp();
    }
}
