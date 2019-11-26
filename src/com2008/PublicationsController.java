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

    public static String generateIssn() {
        Random random = new Random();
        String newIssn = String.format("%04d", random.nextInt(10000)) + "-" + String.format("%04d", random.nextInt(10000));
        Boolean isUnique = true;

        Statement stmt = null;

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT issn FROM journals");

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
        Statement stmt = null;
        ArrayList<Editor> results = new ArrayList<Editor>();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT u.title, u.forenames, u.surname, u.universityAffiliation, u.email, u.password, j.chiefEditorEmail " +
                    "FROM users u " +
                    "LEFT JOIN editors e ON e.email = u.email " +
                    "LEFT JOIN journals j ON  j.issn = e.issn " +
                    "WHERE j.issn='" + journalIssn + "'");

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
        Statement stmt = null;
        ArrayList<Journal> results = new ArrayList<Journal>();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT issn, name, chiefEditorEmail FROM journals");

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
        Statement stmt = null;

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT name, chiefEditorEmail FROM journals WHERE issn LIKE '" + issn + "'");

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
            "WHERE s.issn='" + issn + "' AND pa.vol=" + vol + " AND pa.number=" + number);
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

    public static Author[] getArticleAuthors(int submissionId) {
        Statement stmt = null;
        ArrayList<Author> results = new ArrayList<Author>();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT u.email, u.title, u.forenames, u.surname, u.universityAffiliation, u.password FROM users u LEFT JOIN authors a on u.email = a.email WHERE submissionID = " + submissionId);

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

    public static Submission getSubmissionInfo(int submissionId) {
        Statement stmt = null;

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM submissions\n" +
                    "    WHERE submissionID = " + submissionId);

            // Fetch each row from the result set
            while (res.next()) {
                String title = res.getString("title");
                String abs = res.getString("abstract");
                String pdfLink = res.getString("pdf");
                String mainAuthorsEmail = res.getString("mainAuthorsEmail");

                return new Submission(submissionId, title, abs, pdfLink, mainAuthorsEmail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Map<String, String> getArticleInfo(int submissionId) {
        Map<String, String> results = new HashMap<String, String>();

        Statement stmt = null;

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT j.name, s.issn, pa.vol, pa.number, pa.startPage, pa.endPage, s.title, s.abstract FROM publishedArticles pa\n" +
                    "    LEFT JOIN submissions s on s.submissionID = pa.submissionID\n" +
                    "    LEFT JOIN journals j on s.issn = j.issn\n" +
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

    public static void createJournal(Journal journal, Editor[] editors) {
        Statement stmt = null;

        //TODO: check if editors already exist, if so don't replace passwords. Later on include hashing passwords.

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            con.setAutoCommit(false);
            stmt = con.createStatement();

            String issn = journal.getIssn();
            String name = journal.getName();
            String chiefEditorEmail = journal.getChiefEditorEmail();

            int dbUpdate = stmt.executeUpdate("INSERT INTO journals (issn, name, chiefEditorEmail)" +
                    "VALUES ('" + issn + "', '" + name + "', '" + chiefEditorEmail + "')");

            // add author accounts
            for (Editor editor : editors) {
                // check if an account already exists
                ResultSet res = stmt.executeQuery("SELECT COUNT(email) AS count FROM users WHERE email = '" + editor.getEmail() + "'");
                int count = 0;
                while (res.next()) {
                    count = Integer.parseInt(res.getString("count"));
                }

                if (count < 1) {
                    //TODO: make sure the password is hashed or whatever
                    addUser(editor.getEmail(), editor.getTitle(), editor.getForenames(), editor.getSurname(), editor.getUniversityAffiliation(), editor.getPassword());
                }

                int editorsUpdate = stmt.executeUpdate("INSERT INTO editors (issn, email) VALUES ('" + issn + "', '" + editor.getEmail() + "')");
            }

            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public static ConsideredSubmission[] getConsideredSubmissions(String journalIssn) {
        Statement stmt = null;
        ArrayList<ConsideredSubmission> results = new ArrayList<ConsideredSubmission>();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT s.submissionID, s.title, s.abstract, s.pdf, s.mainAuthorsEmail, cs.decision\n" +
                    "                    FROM submissions s\n" +
                    "                    LEFT JOIN reviews r on s.submissionID = r.submissionID\n" +
                    "                    LEFT JOIN consideredSubmissions cs ON s.submissionID = cs.submissionId\n" +
                    "                    WHERE r.verdict != '' AND s.issn = '" + journalIssn + "' HAVING COUNT(r.verdict) >= 3");

            // Fetch each row from the result set
            while (res.next()) {
                int submissionID = res.getInt("submissionID");
                String title = res.getString("title");
                String abs = res.getString("abstract");
                String pdf = res.getString("pdf");
                String mainAuthorsEmail = res.getString("mainAuthorsEmail");
                String decision = res.getString("decision");

                results.add(new ConsideredSubmission(submissionID, title, abs, pdf, mainAuthorsEmail, decision));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ConsideredSubmission[] arrayResults = new ConsideredSubmission[results.size()];
        return results.toArray(arrayResults);
    }

    public static Role[] getRoles(String email) {
        Statement stmt = null;
        ArrayList<Role> results = new ArrayList<Role>();


        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();

            // Look for author roles
            ResultSet res = stmt.executeQuery("SELECT a.submissionID, s.title FROM authors a\n" +
                    "    LEFT JOIN submissions s on a.submissionID = s.submissionID\n" +
                    "    WHERE a.email = '" + email + "'");

            // Fetch each row from the result set
            while (res.next()) {
                String submissionId = res.getString("submissionID");
                String nameOrTitle = res.getString("title");

                results.add(new Role(email, "author", submissionId, nameOrTitle));
            }

            // Look for editor roles
            res = stmt.executeQuery("SELECT e.issn, j.name, j.chiefEditorEmail FROM editors e\n" +
                    "    LEFT JOIN journals j on j.issn = e.issn\n" +
                    "    WHERE e.email = '" + email + "'");

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
            res = stmt.executeQuery("SELECT r.submissionID, s.title FROM reviewers r\n" +
                    "    LEFT JOIN submissions s on r.submissionID = s.submissionID\n" +
                    "    WHERE r.email = '" + email + "'");

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

    public static Verdict[] getVerdicts(int submissionId) {
        Statement stmt = null;
        ArrayList<Verdict> results = new ArrayList<Verdict>();


        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();

            // Look for author roles
            ResultSet res = stmt.executeQuery("SELECT * FROM reviews a\n" +
                    "    WHERE submissionID = " + submissionId);

            // Fetch each row from the result set
            while (res.next()) {
                int reviewerId = res.getInt("reviewerID");
                String summary = res.getString("summary");
                String typographicalErrors = res.getString("typographicalErrors");
                String verdict = res.getString("verdict");

                results.add(new Verdict(submissionId, reviewerId, summary, typographicalErrors, verdict));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Verdict[] arrayResults = new Verdict[results.size()];
        return results.toArray(arrayResults);
    }

    private static int getVolumeNumber(String issn) {
        Statement stmt = null;

        Volume[] volumes = getVolumes(issn);
        Volume lastVolume = volumes[volumes.length-1];

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        if (lastVolume.getYear() == currentYear) {
            return lastVolume.getVol();
        } else {
            try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
                stmt = con.createStatement();

                int res = stmt.executeUpdate("INSERT INTO volumes (vol,year,issn) " +
                        "VALUES (" + (lastVolume.getVol()+1) + ", " + (lastVolume.getYear()+1) + ", '" + lastVolume.getIssn() + "')");

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return lastVolume.getVol()+1;
        }
    }

    public static void main(String[] args) {
        System.out.println(getVolumeNumber("0595-5898"));
    }

}
