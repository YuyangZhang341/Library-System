package com2008;

import org.jasypt.util.password.StrongPasswordEncryptor;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Util {
    private static Toolkit toolkit = Toolkit.getDefaultToolkit();
    private static Dimension screenDimensions = toolkit.getScreenSize();
    public static int HEIGHT = screenDimensions.height - 40;
    public static int WIDTH = screenDimensions.width;

    private static int countInString(char c, String s) {
        int counter = 0;
        for(int i = 0; i < s.length(); i++) {
            if(s.charAt(i) == c)
                counter += 1;
        }
        return counter;
    }

    public static boolean checkForbiddenCharacters(String s) {
        // return false if string contains ; : / \
        return !(s.contains(";") || s.contains(":") || s.contains("/") || s.contains("\\")
                || s.toLowerCase().contains("drop ") || s.toLowerCase().contains("select ")
                || s.toLowerCase().contains("insert ") || s.toLowerCase().contains("set "));
    }

    public static boolean verifyEmail(String email) {
        //return true if the @ sign is repeated exactly once in the string.
        return countInString('@', email) == 1;
    }

    public static boolean verifyTable(JTable table, Component parentComponent) {
        // if any field in the table is empty, return false. else return true.
        for(int i = 0; i < table.getColumnCount(); i++) {
            for(int j = 0; j < table.getRowCount(); j++) {
                if(table.getValueAt(j, i) == null || table.getValueAt(j, i).toString().equals("")) {
                    JOptionPane.showMessageDialog(parentComponent,"Field in row " + j+1 + ", column " + i+1 + " in the table is empty. (Make sure you finished editing the table. Press enter inside a cell to finish editing it.)");
                    return false;
                } else if(! checkForbiddenCharacters(table.getValueAt(j, i).toString())) {
                    JOptionPane.showMessageDialog(parentComponent,"Field in row " + j+1 + ", column " + i+1 + " contains a forbidden character (; : / \\)");
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean verifyTable(JTable table) {
        return verifyTable(table, null);
    }

    public static boolean verifyEmailInTable(JTable table, int emailColumn, Component parentComponent) {
        // check emails in the table
        for(int i = 0; i < table.getRowCount(); i++) {
            if(! Util.verifyEmail(table.getValueAt(i, emailColumn).toString())) {
                JOptionPane.showMessageDialog(parentComponent,"Email in row " + i+1 + " in the table is incorrect.");
                return false;
            }
        }
        return true;
    }

    public static boolean verifyEmailInTable(JTable table, int emailColumn) {
        return verifyEmailInTable(table, emailColumn, null);
    }

    public static boolean issnExists(String issn) {
        PreparedStatement pstmt = null;
        String query = "SELECT COUNT(issn) AS count FROM journals WHERE issn = ?";

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, issn);

            ResultSet res = pstmt.executeQuery();
            int count = 0;
            while (res.next()) {
                count = Integer.parseInt(res.getString("count"));
            }

            return count >= 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean checkUser(String email, String password) {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            // check if an account already exists
            PreparedStatement pstmt2 = con.prepareStatement("SELECT COUNT(email) AS count FROM users WHERE email = ?");

            pstmt2.setString(1, email);

            ResultSet res = pstmt2.executeQuery();
            int count = 0;
            while (res.next()) {
                count = Integer.parseInt(res.getString("count"));
            }

            if(count == 0) {
                return true;
            }

            //check if password matches
            StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
            String sql="SELECT * FROM users WHERE email = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                String encryptedPassword = rs.getString("password");
                if (passwordEncryptor.checkPassword(password, encryptedPassword)) {
                    //correct
                    return true;
                } else {
                    //wrong
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
