package com2008;

import javax.swing.*;
import java.awt.*;

public class Util {
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
                    JOptionPane.showMessageDialog(parentComponent,"Field in row " + j + ", column " + i + " in the table is empty. (Make sure you finished editing the table.)");
                    return false;
                } else if(! checkForbiddenCharacters(table.getValueAt(j, i).toString())) {
                    JOptionPane.showMessageDialog(parentComponent,"Field in row " + j + ", column " + i + " contains a forbidden character (; : / \\)");
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
                JOptionPane.showMessageDialog(parentComponent,"Email in row " + i + " in the table is incorrect.");
                return false;
            }
        }
        return true;
    }

    public static boolean verifyEmailInTable(JTable table, int emailColumn) {
        return verifyEmailInTable(table, emailColumn, null);
    }
}
