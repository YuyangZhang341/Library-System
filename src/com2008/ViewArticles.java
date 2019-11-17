package com2008;

import javax.swing.*;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class ViewArticles {
    public static void main() {
        JOptionPane.showMessageDialog(null,"View the articles");
    }

    public static String[] getJournals() {
        Statement stmt = null;
        String[] journalsList = new String[0];

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT issn, name FROM journals");

//            Array a = res.getArray("issn");
//            String[] nullable = (String[])a.getArray();
//            journalsList = nullable.clone();

//            // Fetch each row from the result set
//            while (res.next()) {
//                String issn = res.getString("issn");
//                String name = res.getString("name");
//
//                journalsList.add(new Journal(issn, name));
//            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return journalsList;
    }

    public static void main(String[] args) {
        System.out.println(getJournals());
    }
}
