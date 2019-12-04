package com.javateam019.view;

import com.javateam019.dao.UserDao;
import com.javateam019.model.User;
import com.javateam019.util.DbUtil;
import com.javateam019.util.StringUtil;
import com2008.*;
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
