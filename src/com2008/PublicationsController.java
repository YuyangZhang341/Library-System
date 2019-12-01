package com2008;

import javax.swing.*;
import java.io.*;
import java.sql.*;
import java.util.*;

public class PublicationsController {
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
            ResultSet res = stmt.executeQuery("SELECT s.submissionID, rs.title, rs.abstract, pa.startPage, pa.endPage, s.mainAuthorsEmail " +
            "FROM publishedArticles pa " +
            "LEFT JOIN submissions s ON pa.submissionID = s.submissionID " +
            "LEFT JOIN revisedSubmissions rs ON pa.submissionID = rs.submissionID " +
            "LEFT JOIN users u ON s.mainAuthorsEmail = u.email " +
            "WHERE s.issn='" + issn + "' AND pa.vol=" + vol + " AND pa.number=" + number);
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

    public static Submission[] getSubmissions() {
        Statement stmt = null;
        ArrayList<Submission> results = new ArrayList<Submission>();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT submissionID, title, abstract, pdf, mainAuthorEmail, issn FROM submissions");

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
        Statement stmt = null;

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM submissions\n" +
                    "    WHERE submissionID = " + submissionId);

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
        Statement stmt = null;

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM revisedSubmissions\n" +
                    "    WHERE submissionID = " + submissionId);

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
        Statement stmt = null;

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT s.issn, pa.vol, pa.number, pa.startPage, pa.endPage, rs.title, rs.abstract, rs.pdf, s.mainAuthorsEmail FROM publishedArticles pa\n" +
                    "    LEFT JOIN submissions s on s.submissionID = pa.submissionID\n" +
                    "    LEFT JOIN revisedSubmissions rs on s.submissionID = rs.submissionID" +
                    "    WHERE pa.submissionID = " + submissionId);

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

            PreparedStatement pstmt = con.prepareStatement(
                    "INSERT INTO submissions (title, abstract, pdf, mainAuthorsEmail, issn) VALUES (?, ?, ?, ?, ?)"
            );
            pstmt.setString(1, submission.getTitle());
            pstmt.setString(2, submission.getAbs());
            pstmt.setBinaryStream(3, new FileInputStream(submission.getPdf()));
            pstmt.setString(4, submission.getMainAuthorsEmail());
            pstmt.setString(5, submission.getIssn());

            pstmt.executeUpdate();

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
        } catch (SQLException | FileNotFoundException e) {
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
            ResultSet res = stmt.executeQuery("SELECT rs.submissionID, rs.title, rs.abstract, s.mainAuthorsEmail, cs.decision, COUNT(r.finalVerdict) as verdicts\n" +
                    "                    FROM revisedSubmissions rs\n" +
                    "                    LEFT JOIN submissions s\n" +
                    "                    LEFT JOIN reviews r on s.submissionID = r.submissionID\n" +
                    "                    LEFT JOIN consideredSubmissions cs ON s.submissionID = cs.submissionId\n" +
                    "                    WHERE r.finalVerdict != '' AND s.issn = '" + journalIssn + "'\n" +
                    "                    GROUP BY s.submissionID\n"+
                    "                    HAVING verdicts>=3");

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
                String verdict = res.getString("finalVerdict");

                results.add(new Verdict(submissionId, reviewerId, summary, typographicalErrors, verdict));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Verdict[] arrayResults = new Verdict[results.size()];
        return results.toArray(arrayResults);
    }

    public static Criticism[] getCriticisms(int submissionId, int reviewerId) {
        Statement stmt = null;
        ArrayList<Criticism> results = new ArrayList<Criticism>();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();

            // Look for author roles
            ResultSet res = stmt.executeQuery("SELECT * FROM criticisms\n" +
                    "    WHERE submissionID = " + submissionId + " AND reviewerID = " + reviewerId);

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
        Statement stmt = null;
        ArrayList<Criticism> results = new ArrayList<Criticism>();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();

            // Look for author roles
            ResultSet res = stmt.executeQuery("SELECT * FROM criticisms\n" +
                    "    WHERE submissionID = " + submissionId);

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

    public static Review[] getReviews(int submissionId) {
        Statement stmt = null;
        ArrayList<Review> results = new ArrayList<Review>();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();

            // Look for author roles
            ResultSet res = stmt.executeQuery("SELECT * FROM reviews\n" +
                    "    WHERE submissionID = " + submissionId);

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
        Statement stmt = null;

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();

            int res = stmt.executeUpdate("INSERT INTO volumes (vol,year,issn) " +
                    "VALUES (" + vol + ", " + year + ", '" + issn + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static int getVolumeNumber(String issn) {
        Statement stmt = null;

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
            addVolume(issn, lastVolume.getVol()+1, lastVolume.getYear()+1);
            return lastVolume.getVol()+1;
        }
    }

    public static void publishEdition(String issn) {
        Statement stmt = null;
        Statement stmt2 = null;

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
            stmt = con.createStatement();
            stmt2 = con.createStatement();

            // Add the new edition
            int dbUpdate = stmt.executeUpdate("INSERT INTO editions (number, issn, vol) " +
                    "VALUES (" + newEditionNumber + ", '" + issn + "', " + targetVolume + ")");

            // Get accepted submission for that issn
            ResultSet res = stmt.executeQuery("SELECT s.submissionID, rs.title, rs.abstract, rs.pdf, s.mainAuthorsEmail FROM submissions s\n" +
                    "    LEFT JOIN revisedSubmissions rs on s.submissionID = rs.submissionID" +
                    "    LEFT JOIN consideredSubmissions cs on s.submissionID = cs.submissionID\n" +
                    "    WHERE s.issn = '" + issn + "' AND cs.decision = 'accepted'");

            // Add submissions to submitArticles with appropriate issn, vol and edition numbers
            while (res.next()) {
                int submissionId = res.getInt("submissionID");
                String title = res.getString("title");
                String abs = res.getString("abstract");
                String pdf = res.getString("pdf");
                String mainAuthorsEmail = res.getString("mainAuthorsEmail");

                //work out start page and end page
                int startPage = previousLastPage + 1;
                //TODO: change endpage according to pdf length
                int endPage = startPage + 1;
                previousLastPage = endPage;

                dbUpdate = stmt2.executeUpdate("INSERT INTO publishedArticles (submissionID, vol, number, startPage, endPage) " +
                        "VALUES (" + submissionId + ", " + targetVolume + ", " + newEditionNumber + ", " + startPage + ", " + endPage + ")");

                // Delete the submission from consideredSubmissions table
                dbUpdate = stmt2.executeUpdate("DELETE FROM consideredSubmissions WHERE submissionID = " + submissionId);

                // Delete the reviews for the submission
                dbUpdate = stmt2.executeUpdate("DELETE FROM criticisms1 WHERE submissionID = " + submissionId);
                dbUpdate = stmt2.executeUpdate("DELETE FROM reviews WHERE submissionID = " + submissionId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addRevisedSubmission(RevisedSubmission revisedSubmission) {
        Statement stmt = null;

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();

            PreparedStatement pstmt = con.prepareStatement(
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
        Statement stmt = null;

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();

            PreparedStatement pstmt = con.prepareStatement(
                    "UPDATE criticisms SET response = ? WHERE criticismID = " + criticismID
            );
            pstmt.setString(1, response);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static SelectedArticles[] getSelectedArticles() {
        Statement stmt = null;
        ArrayList<SelectedArticles> results = new ArrayList<SelectedArticles>();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT issn, name FROM selectedArticles");

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
        Statement stmt = null;

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT name FROM selectedArticles WHERE issn LIKE '" + issn + "'");

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
}
