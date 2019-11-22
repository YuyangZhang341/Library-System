package com2008;

import com.mysql.cj.protocol.Resultset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.*;

public class PublicationsController {
    public static void main() {
        JOptionPane.showMessageDialog(null,"View the articles");
    }

    public static Journal[] getJournals() {
        Statement stmt = null;
        ArrayList<Journal> results = new ArrayList<Journal>();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT issn, name FROM journals");

            // Fetch each row from the result set
            while (res.next()) {
                String issn = res.getString("issn");
                String name = res.getString("name");

                results.add(new Journal(issn, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Journal arrayResults[] = new Journal[results.size()];
        return results.toArray(arrayResults);
    }

    public static Volume[] getVolumes(String issn) {
        Statement stmt = null;
        ArrayList<Volume> results = new ArrayList<Volume>();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT vol, year FROM volumes WHERE issn LIKE '" + issn + "'");

            // Fetch each row from the result set
            while (res.next()) {
                int vol = res.getInt("vol");
                int year = res.getInt("year");

                results.add(new Volume(issn, vol, year));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Volume arrayResults[] = new Volume[results.size()];
        return results.toArray(arrayResults);
    }

    public static Edition[] getEditions(String issn, int vol) {
        Statement stmt = null;
        ArrayList<Edition> results = new ArrayList<Edition>();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT number FROM editions WHERE issn='" + issn + "' AND vol=" + vol);

            // Fetch each row from the result set
            while (res.next()) {
                int number = res.getInt("number");

                results.add(new Edition(issn, vol, number));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Edition arrayResults[] = new Edition[results.size()];
        return results.toArray(arrayResults);
    }

    public static Article[] getArticles( String issn, int vol, int number) {
        Statement stmt = null;
        ArrayList<Article> results = new ArrayList<Article>();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT s.submissionID, s.title, s.abstract, s.pdf, pa.startPage, pa.endPage, s.mainAuthorsEmail " +
            "FROM publishedArticles pa " +
            "LEFT JOIN submissions s ON pa.submissionID = s.submissionID " +
            "LEFT JOIN users u ON s.mainAuthorsEmail = u.email " +
            "WHERE pa.issn='" + issn + "' AND pa.vol=" + vol + " AND pa.number=" + number);
            // Fetch each row from the result set

            while (res.next()) {
                int submissionID = res.getInt("submissionID");
                String title = res.getString("title");
                String abs = res.getString("abstract");
                String pdf = res.getString("pdf");
                String mainAuthorsEmail = res.getString("mainAuthorsEmail");
                int startPage = res.getInt("startPage");
                int endPage = res.getInt("endPage");

                results.add(new Article(submissionID, title, abs, pdf, mainAuthorsEmail, issn, vol, number, startPage, endPage));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Article arrayResults[] = new Article[results.size()];
        return results.toArray(arrayResults);
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

    public static void addUser(String email, String title, String forenames, String surname, String uniAffiliation, String password) {
        Statement stmt = null;

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();
            int dbUpdate = stmt.executeUpdate("INSERT INTO users (email, title, forenames, surname, universityAffiliation, password)" +
                    "VALUES ('" + email + "', '" + title + "', '" + forenames + "', '" + surname + "', '" + uniAffiliation + "', '" + password + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addSubmission(Submission submission, Author[] authors) {
        Statement stmt = null;

        //TODO: check if authors already exist, if so don't replace passwords. LAter on include hashing passwords.

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            con.setAutoCommit(false);
            stmt = con.createStatement();

            int submissionId = submission.getSubmissionId();

            int dbUpdate = stmt.executeUpdate("INSERT INTO submissions (title, abstract, pdf, mainAuthorsEmail)" +
                    "VALUES ('" + submission.getTitle() + "', '" + submission.getAbs() + "', '" + submission.getPdfLink() + "', '" + submission.getMainAuthorsEmail()+ "')");

            // check the submission id assigned
            ResultSet submissionRes = stmt.executeQuery("SELECT submissionID FROM submissions\n" +
                    "    WHERE mainAuthorsEmail = '" + submission.getMainAuthorsEmail() + "'\n" +
                    "    AND abstract = '" + submission.getAbs() + "'");
            while (submissionRes.next()) {
                submissionId = Integer.parseInt(submissionRes.getString("submissionID"));
            }

            // add author accounts
            for (Author author : authors) {
                // check if an account already exists
                ResultSet res = stmt.executeQuery("SELECT COUNT(email) AS count FROM users WHERE email = '" + author.getEmail() + "'");
                int count = 0;
                while (res.next()) {
                    count = Integer.parseInt(res.getString("count"));
                }

                if (count < 1) {
                    //TODO: make sure the password is hashed or whatever
                    addUser(author.getEmail(), author.getTitle(), author.getForenames(), author.getSurname(), author.getUniversityAffiliation(), author.getPassword());
                }

                if (submissionId != -1) {
                    int authorsUpdate = stmt.executeUpdate("INSERT INTO authors (submissionID, email) VALUES (" + submissionId + ", '" + author.getEmail() + "')");
                }
            }

            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
