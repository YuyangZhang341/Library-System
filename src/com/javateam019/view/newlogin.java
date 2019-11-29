package com.javateam019.view;

import com.javateam019.dao.UserDao;
import com.javateam019.model.User;
import com.javateam019.util.DbUtil;
import com.javateam019.util.StringUtil;
import com2008.*;


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

    private DbUtil DbUtil = new DbUtil();
    private UserDao userDao = new UserDao();


    public newlogin() {
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                App.showMainApp();
                frame.dispose();
            }
        });

        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginActionPerformed(e);
            }
        });

        userNameTxt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // enter pressed inside userNameTxt. confirmButton.doClick() instead of direct call to login function
                // in order to highlight it to indicate that something happened.
                confirmButton.doClick();
            }
        });

        passwordTxt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // enter pressed inside passwordTxt. confirmButton.doClick() instead of direct call to login function
                // in order to highlight it to indicate that something happened.
                confirmButton.doClick();
            }
        });
    }

    /**
     * log in event handler
     * @param evt
     */
    private void loginActionPerformed(ActionEvent evt) {
        String userName =this.userNameTxt.getText();
        String password = new String(this.passwordTxt.getPassword());
        if(StringUtil.isEmpty(userName)){
            JOptionPane.showMessageDialog(null,"Please enter your username");
            return;
        }
        if(StringUtil.isEmpty(password)){
            JOptionPane.showMessageDialog(null,"Please enter your password");
            return;
        }

        User user = new User(userName, password);
        Connection con = null;
        try {
            con = DbUtil.getCon();
            User recentUser =userDao.login(con, user);
            if(recentUser!=null){
                // Check which roles the user is registered for
                Role roles[] = PublicationsController.getRoles(userName);

                // If there's only one role, go straight to the appropriate view. Else, show the role choice view.
                if(roles.length == 1) {
                    int submissionId;
                    String issn;

                    switch(roles[0].getRole()) {
                        case "author":
                            submissionId = Integer.parseInt(roles[0].getIssnOrSubmissionId());
                            AuthorView.showAuthorView(submissionId, userName);
                            break;
                        case "editor":
                        case "chief editor":
                            issn = roles[0].getIssnOrSubmissionId();
                            EditorView.showEditorView(issn, userName);
                            break;
                        case "reviewer":
                            submissionId = Integer.parseInt(roles[0].getIssnOrSubmissionId());
                            //TODO: Go to reviewer view.
                            break;
                    }
                } else {
                    RoleChoiceView.showRoleChoiceView(userName);
                }

                frame.dispose();
            }else{
                JOptionPane.showMessageDialog(null,"user name or password is wrong");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public static void showLogIn(){
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
