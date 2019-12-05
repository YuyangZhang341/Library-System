package com2008;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignUp {
    private JPanel panel1;
    private JLabel lableSignIn;
    private JPanel upperPanel;
    private JPanel lowPanel;
    private JTextField userNameField;
    private JTextField emialField;
    private JButton submit;
    private JPanel mainPanel;
    private JButton buttonBack;
    private JPasswordField passwordField;

    private static JFrame frame = new JFrame("SignUp");


    public SignUp(){
        buttonBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LogIn().showLogIn();
                frame.dispose();
            }
        });
    };

    public static void showSignUp() {
        frame.setContentPane(new SignUp().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(Util.WIDTH, Util.HEIGHT);
        frame.setVisible(true);
    }


}
