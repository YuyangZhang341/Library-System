package com2008;

import java.io.*;
import java.sql.*;
import java.util.*;
import org.jasypt.util.password.StrongPasswordEncryptor;

/*
 *    Copyright 2019 Apache PDFBox
 *    SPDX-License-Identifier: Apache-2.0
 */
import org.apache.pdfbox.pdmodel.PDDocument;

public class PublicationsController {
    public static String generateIssn() {
        Random random = new Random();
        String newIssn = String.format("%04d", random.nextInt(10000)) + "-" + String.format("%04d", random.nextInt(10000));
        Boolean isUnique = true;

        PreparedStatement pstmt = null;
        String query = "SELECT issn FROM journals";

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            pstmt = con.prepareStatement(query);
            ResultSet res = pstmt.executeQuery();

            // Fetch each row from the result set
            while (res.next()) {
                String issn = res.getString("issn");

                isUnique = issn != newIssn;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (isUnique) {
            return newIssn;
        } else {
            return generateIssn();
        }
    }

    public static Editor[] getEditors(String journalIssn) {
        PreparedStatement pstmt = null;
        String query = "SELECT u.title, u.forenames, u.surname, u.universityAffiliation, u.email, u.password, j.chiefEditorEmail " +
                "FROM users u " +
                "LEFT JOIN editors e ON e.email = u.email " +
                "LEFT JOIN journals j ON  j.issn = e.issn " +
                "WHERE j.issn = ?";
        ArrayList<Editor> results = new ArrayList<Editor>();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            pstmt = con.prepareStatement(query);

            pstmt.setString(1, journalIssn);

            ResultSet res = pstmt.executeQuery();

            // Fetch each row from the result set
            while (res.next()) {
                String title = res.getString("title");
                String forenames = res.getString("forenames");
                String surname = res.getString("surname");
                String universityAffiliation = res.getString("universityAffiliation");
                String email = res.getString("email");
                String password = res.getString("password");
                Boolean isChiefEditor = res.getString("chiefEditorEmail").equals(res.getString("email"));

                results.add(new Editor(title, forenames, surname, universityAffiliation, email, password, isChiefEditor, journalIssn));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Editor arrayResults[] = new Editor[results.size()];
        return results.toArray(arrayResults);
    }

    public static Journal[] getJournals() {
        PreparedStatement pstmt = null;
        String query = "SELECT issn, name, chiefEditorEmail FROM journals";
        ArrayList<Journal> results = new ArrayList<Journal>();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            pstmt = con.prepareStatement(query);
            ResultSet res = pstmt.executeQuery();

            // Fetch each row from the result set
            while (res.next()) {
                String issn = res.getString("issn");
                String name = res.getString("name");
                String chiefEditorEmail = res.getString("chiefEditorEmail");

                results.add(new Journal(issn, name, chiefEditorEmail));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Journal arrayResults[] = new Journal[results.size()];
        return results.toArray(arrayResults);
    }

    public static Journal getJournal(String issn) {
        PreparedStatement pstmt = null;
        String query = "SELECT name, chiefEditorEmail FROM journals WHERE issn LIKE ?";

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            pstmt = con.prepareStatement(query);

            pstmt.setString(1, issn);

            ResultSet res = pstmt.executeQuery();

            // Fetch each row from the result set
            while (res.next()) {
                String name = res.getString("name");
                String chiefEditorEmail = res.getString("chiefEditorEmail");

                return new Journal(issn, name, chiefEditorEmail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Volume[] getVolumes(String issn) {
        PreparedStatement pstmt = null;
        String query = "SELECT vol, year FROM volumes WHERE issn LIKE ?";
        ArrayList<Volume> results = new ArrayList<Volume>();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            pstmt = con.prepareStatement(query);

            pstmt.setString(1, issn);

            ResultSet res = pstmt.executeQuery();

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
        PreparedStatement pstmt = null;
        String query = "SELECT number FROM editions WHERE issn = ? AND vol = ?";
        ArrayList<Edition> results = new ArrayList<Edition>();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            pstmt = con.prepareStatement(query);

            pstmt.setString(1, issn);
            pstmt.setInt(2, vol);

            ResultSet res = pstmt.executeQuery();

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

    public static Article[] getArticles(String issn, int vol, int number) {
        PreparedStatement pstmt = null;
        String query = "SELECT s.submissionID, rs.title, rs.abstract, pa.startPage, pa.endPage, s.mainAuthorsEmail " +
                "FROM publishedArticles pa " +
                "LEFT JOIN submissions s ON pa.submissionID = s.submissionID " +
                "LEFT JOIN revisedSubmissions rs ON pa.submissionID = rs.submissionID " +
                "LEFT JOIN users u ON s.mainAuthorsEmail = u.email " +
                "WHERE s.issn = ? AND pa.vol = ? AND pa.number = ?";
        ArrayList<Article> results = new ArrayList<Article>();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            pstmt = con.prepareStatement(query);

            pstmt.setString(1, issn);
            pstmt.setInt(2, vol);
            pstmt.setInt(3, number);

            ResultSet res = pstmt.executeQuery();
            // Fetch each row from the result set

            while (res.next()) {
                int submissionID = res.getInt("submissionID");
                String title = res.getString("title");
                String abs = res.getString("abstract");
                String mainAuthorsEmail = res.getString("mainAuthorsEmail");
                int startPage = res.getInt("startPage");
                int endPage = res.getInt("endPage");

                results.add(new Article(submissionID, title, abs, null, mainAuthorsEmail, issn, vol, number, startPage, endPage));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Article arrayResults[] = new Article[results.size()];
        return results.toArray(arrayResults);
    }

    public static Author[] getArticleAuthors(int submissionId) {
        PreparedStatement pstmt = null;
        String query = "SELECT u.email, u.title, u.forenames, u.surname, u.universityAffiliation, u.password FROM users u LEFT JOIN authors a on u.email = a.email WHERE submissionID = ?";
        ArrayList<Author> results = new ArrayList<Author>();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            pstmt = con.prepareStatement(query);

            pstmt.setInt(1, submissionId);

            ResultSet res = pstmt.executeQuery();

            // Fetch each row from the result set
            while (res.next()) {
                String email = res.getString("email");
                String title = res.getString("title");
                String forenames = res.getString("forenames");
                String surname = res.getString("surname");
                String uniAffiliation = res.getString("universityAffiliation");
                String password = res.getString("password");

                results.add(new Author(email, title, forenames, surname, uniAffiliation, password));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Author[] arrayResults = new Author[results.size()];
        return results.toArray(arrayResults);
    }

    public static Submission[] getSubmissions() {
        Statement stmt = null;
        ArrayList<Submission> results = new ArrayList<Submission>();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT submissionID, title, abstract, pdf, mainAuthorsEmail, issn FROM submissions");

            File file = new File("src/pdf/submission.pdf");
            FileOutputStream output = new FileOutputStream(file);

            // Fetch each row from the result set
            while (res.next()) {
                int submissionID = res.getInt("submissionID");
                String title = res.getString("title");
                String abs = res.getString("abstract");
                InputStream input = res.getBinaryStream("pdf");
                String mainAuthorsEmail = res.getString("mainAuthorsEmail");
                String issn = res.getString("issn");

                byte[] buffer = new byte[1024];
                while(input.read(buffer) > 0) {
                    output.write(buffer);
                }

                results.add(new Submission(submissionID, title, abs, file, mainAuthorsEmail, issn));
            }
        }catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        Submission arrayResults[] = new Submission[results.size()];
        return results.toArray(arrayResults);
    }

    public static Submission getSubmission(int submissionId) {
        PreparedStatement pstmt = null;
        String query = "SELECT * FROM submissions\n" +
                "    WHERE submissionID = ?";

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            pstmt = con.prepareStatement(query);

            pstmt.setInt(1, submissionId);

            ResultSet res = pstmt.executeQuery();

            File file = new File("src/pdf/submission.pdf");
            FileOutputStream output = new FileOutputStream(file);

            // Fetch each row from the result set
            while (res.next()) {
                String title = res.getString("title");
                String abs = res.getString("abstract");
                InputStream input = res.getBinaryStream("pdf");
                String mainAuthorsEmail = res.getString("mainAuthorsEmail");
                String issn = res.getString("issn");

                byte[] buffer = new byte[1024];
                while(input.read(buffer) > 0) {
                    output.write(buffer);
                }

                return new Submission(submissionId, title, abs, file, mainAuthorsEmail, issn);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static RevisedSubmission getRevisedSubmission(int submissionId) {
        PreparedStatement pstmt = null;
        String query = "SELECT * FROM revisedSubmissions\n" +
                "    WHERE submissionID = ?";

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            pstmt = con.prepareStatement(query);

            pstmt.setInt(1, submissionId);

            ResultSet res = pstmt.executeQuery();

            File file = new File("src/pdf/revisedSubmission.pdf");
            FileOutputStream output = new FileOutputStream(file);

            // Fetch each row from the result set
            while (res.next()) {
                String title = res.getString("title");
                String abs = res.getString("abstract");
                InputStream input = res.getBinaryStream("pdf");

                byte[] buffer = new byte[1024];
                while(input.read(buffer) > 0) {
                    output.write(buffer);
                }

                return new RevisedSubmission(submissionId, title, abs, file);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Article getArticle(int submissionId) {
        PreparedStatement pstmt = null;
        String query = "SELECT s.issn, pa.vol, pa.number, pa.startPage, pa.endPage, rs.title, rs.abstract, rs.pdf, s.mainAuthorsEmail FROM publishedArticles pa\n" +
                "    LEFT JOIN submissions s on s.submissionID = pa.submissionID\n" +
                "    LEFT JOIN revisedSubmissions rs on s.submissionID = rs.submissionID" +
                "    WHERE pa.submissionID = ?";

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            pstmt = con.prepareStatement(query);

            pstmt.setInt(1, submissionId);

            ResultSet res = pstmt.executeQuery();

            File file = new File("src/pdf/article.pdf");
            FileOutputStream output = new FileOutputStream(file);

            // Fetch each row from the result set
            while (res.next()) {
                String issn = res.getString("issn");
                int vol = res.getInt("vol");
                int number = res.getInt("number");
                InputStream input = res.getBinaryStream("pdf");
                String title = res.getString("title");
                String abs = res.getString("abstract");
                int startPage = res.getInt("startPage");
                int endPage = res.getInt("endPage");
                String mainAuthorsEmail = res.getString("mainAuthorsEmail");

                byte[] buffer = new byte[1024];
                while(input.read(buffer) > 0) {
                    output.write(buffer);
                }

                return new Article(submissionId, title, abs, file, mainAuthorsEmail, issn, vol, number, startPage, endPage);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void addUser(String email, String title, String forenames, String surname, String uniAffiliation, String password) {
        PreparedStatement pstmt = null;
        String query = "INSERT INTO users (email, title, forenames, surname, universityAffiliation, password) VALUES (?,?,?,?,?,?)";

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            pstmt = con.prepareStatement(query);
            // check if an account already exists
            query = "SELECT COUNT(email) AS count FROM users WHERE email = ?";

            PreparedStatement pstmt2 = con.prepareStatement(query);

            pstmt2.setString(1, email);

            ResultSet res = pstmt2.executeQuery();
            int count = 0;
            while (res.next()) {
                count = Integer.parseInt(res.getString("count"));
            }

            if (count < 1) {
                // encypt the password
                StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
                String encryptedPassword = passwordEncryptor.encryptPassword(password);

                pstmt.setString(1, email);
                pstmt.setString(2, title);
                pstmt.setString(3, forenames);
                pstmt.setString(4, surname);
                pstmt.setString(5, uniAffiliation);
                pstmt.setString(6, encryptedPassword);

                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addEditor(String issn, String email) {
        PreparedStatement pstmt = null;
        String query = "INSERT INTO editors (issn, email) VALUES (?,?)";

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            pstmt = con.prepareStatement(query);

            pstmt.setString(1, issn);
            pstmt.setString(2, email);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addSubmission(Submission submission, Author[] authors) {
        PreparedStatement pstmt = null;
        String query = "INSERT INTO submissions (title, abstract, pdf, mainAuthorsEmail, issn) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            con.setAutoCommit(false);

            int submissionId = submission.getSubmissionId();

            pstmt = con.prepareStatement(query);

            pstmt.setString(1, submission.getTitle());
            pstmt.setString(2, submission.getAbs());
            pstmt.setBinaryStream(3, new FileInputStream(submission.getPdf()));
            pstmt.setString(4, submission.getMainAuthorsEmail());
            pstmt.setString(5, submission.getIssn());

            pstmt.executeUpdate();

            // check the submission id assigned
            query = "SELECT submissionID FROM submissions\n" +
                    "    WHERE mainAuthorsEmail = ?\n" +
                    "    AND abstract = ?";

            pstmt = con.prepareStatement(query);

            pstmt.setString(1, submission.getMainAuthorsEmail());
            pstmt.setString(2, submission.getAbs());

            ResultSet submissionRes = pstmt.executeQuery();
            while (submissionRes.next()) {
                submissionId = Integer.parseInt(submissionRes.getString("submissionID"));
            }

            // add author accounts
            for (Author author : authors) {
                addUser(author.getEmail(), author.getTitle(), author.getForenames(), author.getSurname(), author.getUniversityAffiliation(), author.getPassword());

                if (submissionId != -1) {
                    query = "INSERT INTO authors (submissionID, email) VALUES (?,?)";

                    pstmt = con.prepareStatement(query);

                    pstmt.setInt(1, submissionId);
                    pstmt.setString(2, author.getEmail());

                    int authorsUpdate = pstmt.executeUpdate();
                }
            }

            con.commit();
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void createJournal(Journal journal, Editor[] editors) {
        PreparedStatement pstmt = null;
        String query = "INSERT INTO journals (issn, name, chiefEditorEmail) VALUES (?,?,?)";

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            con.setAutoCommit(false);
            pstmt = con.prepareStatement(query);

            String issn = journal.getIssn();
            String name = journal.getName();
            String chiefEditorEmail = journal.getChiefEditorEmail();

            pstmt.setString(1, issn);
            pstmt.setString(2, name);
            pstmt.setString(3, chiefEditorEmail);

            int dbUpdate = pstmt.executeUpdate();

            // add author accounts
            for (Editor editor : editors) {
                addUser(editor.getEmail(), editor.getTitle(), editor.getForenames(), editor.getSurname(), editor.getUniversityAffiliation(), editor.getPassword());

                query = "INSERT INTO editors (issn, email) VALUES (?,?)";

                pstmt = con.prepareStatement(query);

                pstmt.setString(1, issn);
                pstmt.setString(2, editor.getEmail());

                int editorsUpdate = pstmt.executeUpdate();
            }

            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ConsideredSubmission[] getConsideredSubmissions(String journalIssn) {
        PreparedStatement pstmt = null;
        String query = "SELECT rs.submissionID, rs.title, rs.abstract, s.mainAuthorsEmail, cs.decision, COUNT(r.finalVerdict) as verdicts\n" +
                "                    FROM revisedSubmissions rs\n" +
                "                    LEFT JOIN submissions s ON rs.submissionID = s.submissionID\n" +
                "                    LEFT JOIN reviews r ON rs.submissionID = r.submissionID\n" +
                "                    LEFT JOIN consideredSubmissions cs ON rs.submissionID = cs.submissionId\n" +
                "                    WHERE r.finalVerdict != '' AND s.issn = ?\n" +
                "                    GROUP BY s.submissionID\n" +
                "                    HAVING verdicts>=3";
        ArrayList<ConsideredSubmission> results = new ArrayList<ConsideredSubmission>();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            pstmt = con.prepareStatement(query);

            pstmt.setString(1, journalIssn);

            ResultSet res = pstmt.executeQuery();

            // Fetch each row from the result set
            while (res.next()) {
                int submissionID = res.getInt("submissionID");
                String title = res.getString("title");
                String abs = res.getString("abstract");
                String mainAuthorsEmail = res.getString("mainAuthorsEmail");
                String decision = res.getString("decision");
                if(decision == null) decision = "";

                results.add(new ConsideredSubmission(submissionID, title, abs, null, mainAuthorsEmail, journalIssn, decision));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ConsideredSubmission[] arrayResults = new ConsideredSubmission[results.size()];
        return results.toArray(arrayResults);
    }

    public static Role[] getRoles(String email) {
        PreparedStatement pstmt = null;
        String query = "SELECT a.submissionID, s.title FROM authors a\n" +
                "    LEFT JOIN submissions s on a.submissionID = s.submissionID\n" +
                "    WHERE a.email = ?";
        ArrayList<Role> results = new ArrayList<Role>();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            pstmt = con.prepareStatement(query);

            pstmt.setString(1, email);

            // Look for author roles
            ResultSet res = pstmt.executeQuery();

            // Fetch each row from the result set
            while (res.next()) {
                String submissionId = res.getString("submissionID");
                String nameOrTitle = res.getString("title");

                results.add(new Role(email, "author", submissionId, nameOrTitle));
            }

            // Look for editor roles
            query = "SELECT e.issn, j.name, j.chiefEditorEmail FROM editors e\n" +
                    "    LEFT JOIN journals j on j.issn = e.issn\n" +
                    "    WHERE e.email = ?";

            pstmt = con.prepareStatement(query);

            pstmt.setString(1, email);

            res = pstmt.executeQuery();

            // Fetch each row from the result set
            while (res.next()) {
                String issn = res.getString("issn");
                String nameOrTitle = res.getString("name");
                String chiefEditorEmail = res.getString("chiefEditorEmail");

                if(chiefEditorEmail.equals(email)) {
                    results.add(new Role(email, "chief editor", issn, nameOrTitle));
                } else {
                    results.add(new Role(email, "editor", issn, nameOrTitle));
                }
            }

            // Look for reviewer roles
            query = "SELECT r.submissionID, s.title FROM reviewers r\n" +
                    "    LEFT JOIN submissions s on r.submissionID = s.submissionID\n" +
                    "    WHERE r.email = ?";

            pstmt = con.prepareStatement(query);

            pstmt.setString(1, email);

            res = pstmt.executeQuery();

            // Fetch each row from the result set
            while (res.next()) {
                String submissionId = res.getString("submissionID");
                String nameOrTitle = res.getString("title");

                results.add(new Role(email, "reviewer", submissionId, nameOrTitle));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Role[] arrayResults = new Role[results.size()];
        return results.toArray(arrayResults);
    }

    public static Criticism[] getCriticisms(int submissionId, int reviewerId) {
        PreparedStatement pstmt = null;
        String query = "SELECT * FROM criticisms\n" +
                "    WHERE submissionID = ? AND reviewerID = ?";
        ArrayList<Criticism> results = new ArrayList<Criticism>();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            pstmt = con.prepareStatement(query);

            pstmt.setInt(1, submissionId);
            pstmt.setInt(2, reviewerId);

            // Look for author roles
            ResultSet res = pstmt.executeQuery();

            // Fetch each row from the result set
            while (res.next()) {
                int criticismID = res.getInt("criticismID");
                int submissionID = res.getInt("submissionID");
                int reviewerID = res.getInt("reviewerID");
                String criticism = res.getString("criticism");
                String response = res.getString("response");

                results.add(new Criticism(criticismID, submissionID, reviewerID, criticism, response));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Criticism[] arrayResults = new Criticism[results.size()];
        return results.toArray(arrayResults);
    }

    public static Criticism[] getAllCriticisms(int submissionId) {
        PreparedStatement pstmt = null;
        String query = "SELECT * FROM criticisms\n" +
                "    WHERE submissionID = ?";
        ArrayList<Criticism> results = new ArrayList<Criticism>();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            pstmt = con.prepareStatement(query);

            pstmt.setInt(1, submissionId);

            // Look for author roles
            ResultSet res = pstmt.executeQuery();

            // Fetch each row from the result set
            while (res.next()) {
                int criticismID = res.getInt("criticismID");
                int submissionID = res.getInt("submissionID");
                int reviewerID = res.getInt("reviewerID");
                String criticism = res.getString("criticism");
                String response = res.getString("response");

                results.add(new Criticism(criticismID, submissionID, reviewerID, criticism, response));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Criticism[] arrayResults = new Criticism[results.size()];
        return results.toArray(arrayResults);
    }

    public static Review getReview(int submissionId, String email) {
        PreparedStatement pstmt = null;
        String query = "SELECT r.reviewerID, r.summary, r.typographicalErrors, r.initialVerdict, r.submissionID, r.finalVerdict FROM reviews r\n" +
                "    LEFT JOIN reviewers r2 on r.reviewerID = r2.reviewerID\n" +
                "    WHERE email = ? AND r.submissionID = ?";

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            pstmt = con.prepareStatement(query);

            pstmt.setString(1, email);
            pstmt.setInt(2, submissionId);

            ResultSet res = pstmt.executeQuery();

            // Fetch each row from the result set
            while (res.next()) {
                int reviewerId = res.getInt("reviewerID");
                String summary = res.getString("summary");
                String typographicalErrors = res.getString("typographicalErrors");
                String initialVerdict = res.getString("initialVerdict");
                String finalVerdict = res.getString("finalVerdict");

                return new Review(reviewerId, summary, typographicalErrors, initialVerdict, submissionId, finalVerdict);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Review[] getReviews(int submissionId) {
        PreparedStatement pstmt = null;
        String query = "SELECT * FROM reviews\n" +
                "    WHERE submissionID = ?";
        ArrayList<Review> results = new ArrayList<Review>();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            pstmt = con.prepareStatement(query);

            pstmt.setInt(1, submissionId);

            // Look for author roles
            ResultSet res = pstmt.executeQuery();

            // Fetch each row from the result set
            while (res.next()) {
                int reviewerId = res.getInt("reviewerID");
                String summary = res.getString("summary");
                String typographicalErrors = res.getString("typographicalErrors");
                String initialVerdict = res.getString("initialVerdict");
                String finalVerdict = res.getString("finalVerdict");

                results.add(new Review(reviewerId, summary, typographicalErrors, initialVerdict, submissionId, finalVerdict));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Review[] arrayResults = new Review[results.size()];
        return results.toArray(arrayResults);
    }

    public static void addVolume(String issn, int vol, int year) {
        PreparedStatement pstmt = null;
        String query = "INSERT INTO volumes (vol,year,issn) " +
                "VALUES (?,?,?)";

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            pstmt = con.prepareStatement(query);

            pstmt.setInt(1, vol);
            pstmt.setInt(2, year);
            pstmt.setString(3, issn);

            int res = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static int getVolumeNumber(String issn) {
        Volume[] volumes = getVolumes(issn);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        if(volumes.length == 0) {
            addVolume(issn, 1, currentYear);
            return 1;
        }

        Volume lastVolume = volumes[volumes.length-1];

        if (lastVolume.getYear() == currentYear) {
            return lastVolume.getVol();
        } else {
            addVolume(issn, lastVolume.getVol() + 1, lastVolume.getYear() + 1);
            return lastVolume.getVol() + 1;
        }
    }

    public static void publishEdition(String issn) {
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;

        int targetVolume = getVolumeNumber(issn);

        // get previous editions
        Edition previousEditions[] = getEditions(issn, targetVolume);

        // get next edition's number
        int newEditionNumber = previousEditions.length + 1;

        // get last edition's last page number
        int previousLastPage = 0;
        if(previousEditions.length > 0) {
            for(Article article : getArticles(issn, targetVolume, previousEditions.length)) {
                if(article.getEndPage() > previousLastPage) {
                    previousLastPage = article.getEndPage();
                }
            }
        }

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            String query = "INSERT INTO editions (number, issn, vol) " +
                    "VALUES (?,?,?)";
            pstmt = con.prepareStatement(query);

            pstmt.setInt(1, newEditionNumber);
            pstmt.setString(2, issn);
            pstmt.setInt(3, targetVolume);

            // Add the new edition
            int dbUpdate = pstmt.executeUpdate();

            // Get accepted submission for that issn
            query = "SELECT s.submissionID, rs.title, rs.abstract, rs.pdf, s.mainAuthorsEmail FROM submissions s\n" +
                    "    LEFT JOIN revisedSubmissions rs on s.submissionID = rs.submissionID" +
                    "    LEFT JOIN consideredSubmissions cs on s.submissionID = cs.submissionID\n" +
                    "    WHERE s.issn = ? AND cs.decision = 'accepted'";

            pstmt = con.prepareStatement(query);

            pstmt.setString(1, issn);

            ResultSet res = pstmt.executeQuery();

            // Add submissions to submitArticles with appropriate issn, vol and edition numbers
            while (res.next()) {
                int submissionId = res.getInt("submissionID");

                File file = new File("src/pdf/submission.pdf");
                FileOutputStream output = new FileOutputStream(file);
                InputStream input = res.getBinaryStream("pdf");

                byte[] buffer = new byte[1024];
                while(input.read(buffer) > 0) {
                    output.write(buffer);
                }

                //work out start page and end page
                // below is a reference for the package we used to get the number of pages in a PDF.
                /*
                 *    Copyright 2019 Apache PDFBox
                 *    SPDX-License-Identifier: Apache-2.0
                 */
                int startPage = previousLastPage + 1;
                int endPage = previousLastPage + PDDocument.load(file).getNumberOfPages();
                previousLastPage = endPage;

                String query2 = "INSERT INTO publishedArticles (submissionID, vol, number, startPage, endPage) " +
                        "VALUES (?,?,?,?,?)";

                pstmt2 = con.prepareStatement(query2);

                pstmt2.setInt(1, submissionId);
                pstmt2.setInt(2, targetVolume);
                pstmt2.setInt(3, newEditionNumber);
                pstmt2.setInt(4, startPage);
                pstmt2.setInt(5, endPage);

                dbUpdate = pstmt2.executeUpdate();

                // Delete the submission from consideredSubmissions table
                query2 = "DELETE FROM consideredSubmissions WHERE submissionID = ?";

                pstmt2 = con.prepareStatement(query2);

                pstmt2.setInt(1, submissionId);

                dbUpdate = pstmt2.executeUpdate();

                // Delete the reviews for the submission
                query2 = "DELETE FROM criticisms WHERE submissionID = ?";

                pstmt2 = con.prepareStatement(query2);

                pstmt2.setInt(1, submissionId);

                dbUpdate = pstmt2.executeUpdate();

                query2 = "DELETE FROM reviews WHERE submissionID = ?";

                pstmt2 = con.prepareStatement(query2);

                pstmt2.setInt(1, submissionId);

                dbUpdate = pstmt2.executeUpdate();

                // Delete the authors of the submission
                query2 = "SELECT email FROM authors WHERE submissionID = ?";

                pstmt2 = con.prepareStatement(query2);

                pstmt2.setInt(1, submissionId);

                ResultSet res2 = pstmt2.executeQuery();

                while(res2.next()) {
                    String userEmail = res2.getString("email");

                    String query3 = "DELETE FROM authors WHERE email = ?";
                    PreparedStatement pstmt3 = con.prepareStatement(query3);
                    pstmt3.setString(1, userEmail);
                    int res3 = pstmt3.executeUpdate();

                    if (PublicationsController.getRoles(userEmail).length == 0) {
                        query3 = "DELETE FROM users WHERE email = ?";
                        pstmt3 = con.prepareStatement(query3);
                        pstmt3.setString(1, userEmail);
                        res3 = pstmt3.executeUpdate();
                    }
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void addRevisedSubmission(RevisedSubmission revisedSubmission) {
        PreparedStatement pstmt = null;

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            pstmt = con.prepareStatement(
                    "INSERT INTO revisedSubmissions (submissionID, title, abstract, pdf) VALUES (?, ?, ?, ?)"
            );
            pstmt.setInt(1, revisedSubmission.getSubmissionId());
            pstmt.setString(2, revisedSubmission.getTitle());
            pstmt.setString(3, revisedSubmission.getAbs());
            pstmt.setBinaryStream(4, new FileInputStream(revisedSubmission.getPdf()));

            pstmt.executeUpdate();
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void respondToCriticism(int criticismID, String response) {
        PreparedStatement pstmt = null;
        String query = "UPDATE criticisms SET response = ? WHERE criticismID = ?";

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            pstmt = con.prepareStatement(query);

            pstmt.setString(1, response);
            pstmt.setInt(2, criticismID);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static SelectedArticles[] getSelectedArticles() {
        PreparedStatement pstmt = null;
        String query = "SELECT issn, name FROM selectedArticles";
        ArrayList<SelectedArticles> results = new ArrayList<SelectedArticles>();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            pstmt = con.prepareStatement(query);

            ResultSet res = pstmt.executeQuery();

            // Fetch each row from the result set
            while (res.next()) {
                String issn = res.getString("issn");
                String name = res.getString("name");
                String chiefEditorEmail = res.getString("chiefEditorEmail");

                results.add(new SelectedArticles(issn, name,chiefEditorEmail));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        SelectedArticles arrayResults[] = new SelectedArticles[results.size()];
        return results.toArray(arrayResults);
    }

    public static SelectedArticles getSelectedArticles(String issn) {
        PreparedStatement pstmt = null;
        String query = "SELECT name FROM selectedArticles WHERE issn LIKE ?";

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            pstmt = con.prepareStatement(query);

            pstmt.setString(1, issn);

            ResultSet res = pstmt.executeQuery();

            // Fetch each row from the result set
            while (res.next()) {
                String name = res.getString("name");
                String chiefEditorEmail = res.getString("chiefEditorEmail");

                return new SelectedArticles(issn, name,chiefEditorEmail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int getReviewerId(int submissionId, String userEmail) {
        PreparedStatement pstmt = null;
        String query = "SELECT reviewerID FROM reviewers WHERE submissionID = ? AND email = ?";

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            pstmt = con.prepareStatement(query);

            pstmt.setInt(1, submissionId);
            pstmt.setString(2, userEmail);

            ResultSet res = pstmt.executeQuery();

            // Fetch each row from the result set
            while (res.next()) {
                return res.getInt("reviewerID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static void addInitialReview(Review review, Criticism[] criticisms) {
        PreparedStatement pstmt = null;

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            // add review
            pstmt = con.prepareStatement(
                    "INSERT INTO reviews VALUES (?, ?, ?, ?, ?, null)"
            );
            pstmt.setInt(1, review.getReviewerId());
            pstmt.setString(2, review.getSummary());
            pstmt.setString(3, review.getTypographicalErrors());
            pstmt.setString(4, review.getInitialVerdict());
            pstmt.setInt(5, review.getSubmissionId());

            pstmt.executeUpdate();

            // add criticisms
            for(Criticism criticism : criticisms) {
                pstmt = con.prepareStatement(
                        "INSERT INTO criticisms (submissionID, reviewerID, criticism, response) VALUES (?, ?, ?, null)"
                );
                pstmt.setInt(1, review.getSubmissionId());
                pstmt.setInt(2, review.getReviewerId());
                pstmt.setString(3, criticism.getCriticism());

                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addFinalVerdict(int submissionId, int reviewerId, String verdict, String userEmail) {
        PreparedStatement pstmt = null;
        String query = "UPDATE reviews SET finalVerdict = ? WHERE submissionID = ? AND reviewerID = ?";

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            // add final verdict
            pstmt = con.prepareStatement(query);

            pstmt.setString(1, verdict);
            pstmt.setInt(2, submissionId);
            pstmt.setInt(3, reviewerId);

            pstmt.executeUpdate();

            // delete the reviewer
            pstmt = con.prepareStatement("DELETE FROM reviewers WHERE email = ? AND submissionID = ?");
            pstmt.setString(1, userEmail);
            pstmt.setInt(2, submissionId);

            pstmt.executeUpdate();

            // if no roles left, delete the user from the system
            if (PublicationsController.getRoles(userEmail).length == 0) {
                pstmt = con.prepareStatement("DELETE FROM users WHERE email = ?");
                pstmt.setString(1, userEmail);
                pstmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
