package com2008;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class ViewArticles {
    public static void main() {
        JOptionPane.showMessageDialog(null,"View the articles");
    }

    public static void fetchJournals(JTable table) {
        Statement stmt = null;
        DefaultTableModel model = new DefaultTableModel(new String[]{"ISSN", "Name"}, 0);
        table.setModel(model);

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT issn, name FROM journals");

            // Fetch each row from the result set
            while (res.next()) {
                String issn = res.getString("issn");
                String name = res.getString("name");

                model.addRow(new Object[]{issn,name});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }
}
