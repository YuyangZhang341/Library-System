package com.javateam019.view;

import com.javateam019.dao.UserDao;
import com.javateam019.model.User;
import com.javateam019.util.DbUtil;
import com.javateam019.util.StringUtil;
import com2008.App;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


public class newlogin {


    private static JFrame frame = new JFrame("LogIn");
    private JTextField userNameTxt;
    private JPasswordField passwordTxt;
    private JPanel mainPanel;
    private JButton confirmButton;
    private JButton backButton;
    private JButton changePassword;

    private DbUtil DbUtil = new DbUtil();
    private UserDao userDao = new UserDao();


    public newlogin() {
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                App.showMainApp();
                frame.dispose();
            }
        });
        changePassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ChangePaswd.showChangeP();
                frame.dispose();
            }
        });
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginActionPerformed(e);
            }
        });
    }

    /**
     * log in event handler
     *
     * @param evt
     */
    private void loginActionPerformed(ActionEvent evt) {
        String userName = this.userNameTxt.getText();
        String password = new String(this.passwordTxt.getPassword());
        if (StringUtil.isEmpty(userName)) {
            JOptionPane.showMessageDialog(null, "Please enter your username");
            return;
        }
        if (StringUtil.isEmpty(password)) {
            JOptionPane.showMessageDialog(null, "Please enter your password");
            return;
        }

        User user = new User(userName, password);
        Connection con = null;
        try {
            con = DbUtil.getCon();
            User recentUser = userDao.login(con, user);
            if (recentUser != null) {
                frame.dispose();
                MainFrame.showMainFrame();

            } else {
                JOptionPane.showMessageDialog(null, "user name or password is wrong");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public static void showLogIn() {
        frame.setContentPane(new newlogin().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenDimensions = toolkit.getScreenSize();
        frame.setSize(screenDimensions.width, screenDimensions.height);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        showLogIn();
    }
}
