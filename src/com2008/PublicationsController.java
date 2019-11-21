package com2008;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.*;

public class PublicationsController {
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

    public static void fetchVolumes(JTable table, String issn) {
        Statement stmt = null;
        DefaultTableModel model = new DefaultTableModel(new String[]{"Volume", "Year"}, 0);
        table.setModel(model);

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT vol, year FROM volumes WHERE issn LIKE '" + issn);

            // Fetch each row from the result set
            while (res.next()) {
                String vol = res.getString("vol");
                String year = res.getString("year");

                model.addRow(new Object[]{vol,year});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void fetchEditions(JTable table, String issn, int vol) {
        Statement stmt = null;
        DefaultTableModel model = new DefaultTableModel(new String[]{"Volume Number", "Edition Number"}, 0);
        table.setModel(model);

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT vol, number FROM editions WHERE issn='" + issn + "' AND vol=" + vol);

            // Fetch each row from the result set
            while (res.next()) {
                String volume = res.getString("vol");
                String number = res.getString("number");

                model.addRow(new Object[]{volume,number});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void fetchArticles(JTable table, String issn, int vol, int number) {
        Statement stmt = null;
        DefaultTableModel model = new DefaultTableModel(new String[]{"Submission ID", "Title", "Abstract", "Author's Forenames", "Surname"}, 0);
        table.setModel(model);

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT s.submissionID, s.title, s.abstract, u.forenames, u.surname " +
            "FROM publishedArticles pa " +
            "LEFT JOIN submissions s ON pa.submissionID = s.submissionID " +
            "LEFT JOIN users u ON s.mainAuthorsEmail = u.email " +
            "WHERE pa.issn='" + issn + "' AND pa.vol=" + vol + " AND pa.number=" + number);
            // Fetch each row from the result set

            while (res.next()) {
                int submissionID = res.getInt("submissionID");
                String title = res.getString("title");
                String abs = res.getString("abstract");
                String forenames = res.getString("forenames");
                String surname = res.getString("surname");

                model.addRow(new Object[]{submissionID,title,abs,forenames,surname});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void fetchArticleAuthors(JTable table, int submissionId) {
        Statement stmt = null;
        DefaultTableModel model = new DefaultTableModel(new String[]{"Title", "Forename", "Surname"}, 0);
        table.setModel(model);

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT u.title, u.forenames, u.surname FROM users u LEFT JOIN authors a on u.email = a.email WHERE submissionID = " + submissionId);

            // Fetch each row from the result set
            while (res.next()) {
                String title = res.getString("title");
                String forenames = res.getString("forenames");
                String surname = res.getString("surname");

                model.addRow(new Object[]{title, forenames, surname});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, String> getArticleInfo(int submissionId) {
        Map<String, String> results = new HashMap<String, String>();

        Statement stmt = null;

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT j.name, pa.issn, pa.vol, pa.number, pa.startPage, pa.endPage, s.title, s.abstract FROM publishedArticles pa\n" +
                    "    LEFT JOIN submissions s on s.submissionID = pa.submissionID\n" +
                    "    LEFT JOIN journals j on pa.issn = j.issn\n" +
                    "    WHERE pa.submissionID = " + submissionId);

            // Fetch each row from the result set
            while (res.next()) {
                String name = res.getString("name");
                String issn = res.getString("issn");
                String vol = res.getString("vol");
                String number = res.getString("number");
                String title = res.getString("title");
                String abs = res.getString("abstract");
                String startPage = res.getString("startPage");
                String endPage = res.getString("endPage");

                results.put("name", name);
                results.put("issn", issn);
                results.put("vol", vol);
                results.put("number", number);
                results.put("title", title);
                results.put("abstract", abs);
                results.put("startPage", startPage);
                results.put("endPage", endPage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    public static void addSubmission(String title, String abs, String pdf, String mainAuthorsEmail) {
        Statement stmt = null;

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();
            int dbUpdate = stmt.executeUpdate("INSERT INTO submissions (title, abstract, pdf, mainAuthorsEmail)" +
                    "VALUES (" + title + ", " + abs + ", " + pdf + ", " + mainAuthorsEmail + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addAuthorToSubmission(int submissionId, String email) {
        Statement stmt = null;

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();
            int dbUpdate = stmt.executeUpdate("INSERT INTO authors (submissionID, email)" +
                    "VALUES (" + submissionId + ", " + email + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
