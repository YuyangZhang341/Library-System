package com.javateam019.view;

import com.javateam019.dao.UserDao;
import com.javateam019.model.User;
import com.javateam019.util.DbUtil;
import com.javateam019.util.StringUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com2008.ArticleActionsDialog;
import javafx.scene.control.TextFormatter;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.StrongTextEncryptor;

public class ChangePaswd extends JDialog {
    private JPanel mainPanel;
    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField1;
    private JPasswordField newPasswordField2;
    private JButton confirmButton1;
    private JTextField userNameTextField;

    private DbUtil DbUtil = new DbUtil();
    private UserDao userDao = new UserDao();

    static ChangePaswd d;

    public ChangePaswd(String userEmail) {
        this();
        userNameTextField.setText(userEmail);
        userNameTextField.setEditable(false);
    }

    public ChangePaswd(){
        setContentPane(mainPanel);
        setModal(true);

        confirmButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changePasswordPerformed(e);
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    private void changePasswordPerformed(ActionEvent evt){
        String userName = this.userNameTextField.getText();
        String oldPassword = new String(this.oldPasswordField.getPassword());
        String newPassword1 = new String(this.newPasswordField1.getPassword());
        String newPassword2 = new String(this.newPasswordField2.getPassword());
        if (StringUtil.isEmpty(userName)) {
            JOptionPane.showMessageDialog(d, "Enter your user name");
            return;
        }
        if (StringUtil.isEmpty(oldPassword)) {
            JOptionPane.showMessageDialog(d, "Please enter your old password");
            return;
        }
        if (StringUtil.isEmpty(newPassword1)) {
            JOptionPane.showMessageDialog(d, "Please enter your new password");
            return;
        }
        if (StringUtil.isEmpty(newPassword2)) {
            JOptionPane.showMessageDialog(d, "Please confirm your password");
            return;
        }
        if(!newPassword1.equals(newPassword2)){
            JOptionPane.showMessageDialog(d, "please unify two passwords");
            return;
        }

        // encypte
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        String encryptedPassword = passwordEncryptor.encryptPassword(newPassword1);

        User oldUser = new User(userName, oldPassword);
        User newUser = new User(userName, encryptedPassword);

        Connection con = null;
        ResultSet rs = null;
        try {
            con = DbUtil.getCon();
            User recentUser = userDao.login(con, oldUser);
//            int changeNum = userDao.changePassword(con,newUser);
            if (recentUser != null) {

                String sql="select * from users where email='"+userName+"' ";
                PreparedStatement pstmt = con.prepareStatement(sql);
                rs = pstmt.executeQuery();
                //check the user typed in text box and check if password is correct
                if(rs.next()){

                    String encryptedPassword2 = rs.getString("password");
                    if (passwordEncryptor.checkPassword(oldPassword, encryptedPassword2)) {
                        //correct
                        userDao.changePassword(con,newUser);
                        JOptionPane.showMessageDialog(d, "change successful");
                        this.resetTxt();
                        System.out.println(encryptedPassword2);
                        System.out.println("true");
                        dispose();
                    } else {
                        //wrong
                        JOptionPane.showMessageDialog(d, "initial user name or password is wrong");
                        System.out.println(encryptedPassword2);
                    }
                }else{
                    System.out.println("failed");
                }
            }else {
                JOptionPane.showMessageDialog(d, "no user");
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

    public static void showChangeP(String userEmail) {
        d = new ChangePaswd(userEmail);
        d.pack();
        d.setSize(275,400);
        d.setLocationRelativeTo(null);
        d.setVisible(true);
    }

    public static void showChangeP() {
        d = new ChangePaswd();
        d.pack();
        d.setSize(275,400);
        d.setLocationRelativeTo(null);
        d.setVisible(true);
    }
    public static void main(String[] args) {
        showChangeP();
    }
}
