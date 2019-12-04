package com.javateam019.view;

import com.javateam019.dao.UserDao;
import com.javateam019.model.User;
import com.javateam019.util.DbUtil;
import com.javateam019.util.StringUtil;
import com2008.App;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;


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
        ResultSet rs = null;
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        try {
            con = DbUtil.getCon();
            User recentUser = userDao.login(con,  user);
            // find the user if not null
            if (recentUser != null) {
                String sql="select * from users where email='"+userName+"' ";
                PreparedStatement pstmt = con.prepareStatement(sql);
                rs = pstmt.executeQuery();
                //check the user typed in text box and check if password is correct
                if(rs.next()){
                    String encryptedPassword = rs.getString("password");
                    if (passwordEncryptor.checkPassword(password, encryptedPassword)) {
                        //correct
                        JOptionPane.showMessageDialog(null, "login successful");
                        frame.dispose();
                        MainFrame.showMainFrame();
                    } else {
                        //wrong
                        JOptionPane.showMessageDialog(null, "wrong user name or password");
                    }
                }else{
                    System.out.println("failed");
                }
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
