
package com2008;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogIn {

    private JPanel mainPanel;
    private JPanel buttonsPanel;
    private JPanel usernamePanel;
    private JPanel Title;
    private JPanel mainBody;
    private JPanel goBack;

    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField Username;
    private JPasswordField Password;
    private JButton signUpAsEditor;
    private JButton signUpAsReviewer;
    private JButton logIn;
    private JButton back;

    private static JFrame frame = new JFrame("LogIn");

    public LogIn(){
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                App.showMainApp();
                frame.dispose();
            }
        });

        logIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {}
        });

        signUpAsEditor.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new SignUp().showSignUp();
                frame.dispose();
            }
        });

        signUpAsReviewer.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new SignUp().showSignUp();
                frame.dispose();
            }
        });


    }

    public static void showLogIn(){
        frame.setContentPane(new LogIn().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenDimensions = toolkit.getScreenSize();
        frame.setSize(screenDimensions.width, screenDimensions.height);
        frame.setVisible(true);
    }
}

