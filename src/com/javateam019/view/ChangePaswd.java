package com.javateam019.view;

import com.javateam019.dao.UserDao;
import com.javateam019.model.User;
import com.javateam019.util.DbUtil;
import com.javateam019.util.StringUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ChangePaswd {
    private static JFrame frame = new JFrame("ChangePassword");
    private JPanel mainPanel;
    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField1;
    private JPasswordField newPasswordField2;
    private JButton confirmButton1;
    private JTextField userNameTextField;

    private DbUtil DbUtil = new DbUtil();
    private UserDao userDao = new UserDao();

        public ChangePaswd(){
        confirmButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changePasswordPerformed(e);
            }
        });
    }
    private void changePasswordPerformed(ActionEvent evt){
        String userName = this.userNameTextField.getText();
        String oldPassword = new String(this.oldPasswordField.getPassword());
        String newPassword1 = new String(this.newPasswordField1.getPassword());
        String newPassword2 = new String(this.newPasswordField2.getPassword());
        if (StringUtil.isEmpty(userName)) {
            JOptionPane.showMessageDialog(null, "Enter your user name");
            return;
        }
        if (StringUtil.isEmpty(oldPassword)) {
            JOptionPane.showMessageDialog(null, "Please enter your old password");
            return;
        }
        if (StringUtil.isEmpty(newPassword1)) {
            JOptionPane.showMessageDialog(null, "Please enter your new password");
            return;
        }
        if (StringUtil.isEmpty(newPassword2)) {
            JOptionPane.showMessageDialog(null, "Please confirm your password");
            return;
        }
        if(!newPassword1.equals(newPassword2)){
            JOptionPane.showMessageDialog(null, "please unify two passwords");
            return;
        }

        User oldUser = new User(userName, oldPassword);
        User newUser = new User(userName, newPassword1);
        Connection con = null;
        try {
            con = DbUtil.getCon();
            User recentUser = userDao.login(con, oldUser);
            int changeNum = userDao.changePassword(con,newUser);
            if (recentUser != null) {
                if (changeNum == 1){
                    JOptionPane.showMessageDialog(null, "change successful");
                    this.resetTxt();
                }else{
                    JOptionPane.showMessageDialog(null, "failed");
                }
            }else {
                JOptionPane.showMessageDialog(null, "initial user name or password is wrong");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                DbUtil.closeCon(con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * clear textbox
     */
    private void resetTxt(){
            this.oldPasswordField.setText("");
            this.newPasswordField1.setText("");
            this.newPasswordField2.setText("");
    }


    public static void showChangeP() {
        frame.setContentPane(new ChangePaswd().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenDimensions = toolkit.getScreenSize();
        frame.setSize(screenDimensions.width, screenDimensions.height);
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        showChangeP();
    }
}
