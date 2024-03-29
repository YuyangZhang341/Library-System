package com2008;

import com.javateam019.view.ChangePaswd;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class EditorView {
    private JPanel mainPanel;
    private JPanel navPanel;
    private JPanel contentPanel;
    private JButton backButton;
    private JButton changePasswordButton;
    private JPanel articlesPanel;
    private JPanel infoPanel;
    private JTable submissionsTable;
    private JTextField nameTextField;
    private JTextField issnTextField;
    private JButton changeEditorButton;
    private JButton publishButton;
    private JScrollPane contentScrollPane;
    private JPanel buttonsPanel;
    private JButton retireButton;
    private JTextField chiefEditorTextField;
    private JLabel counterLabel;
    private JButton addEditorsButton;

    private String journalIssn;
    private String userEmail;
    private Boolean isChiefEditor;
    private Journal journal;
    private int counter = 0;

    private static JFrame frame = new JFrame("Journal Dashboard");

    public EditorView(String journalIssn, String userEmail) {
        this.journalIssn = journalIssn;
        this.userEmail = userEmail;
        this.journal = PublicationsController.getJournal(journalIssn);
        this.isChiefEditor = userEmail.equals(journal.getChiefEditorEmail());

        loadConsideredSubmissionsTable();

        nameTextField.setText(journal.getName());
        issnTextField.setText(journalIssn);
        chiefEditorTextField.setText(journal.getChiefEditorEmail());

        // Change interface depending on who's viewing it
        if (!isChiefEditor) {
            buttonsPanel.remove(changeEditorButton);
            buttonsPanel.remove(publishButton);
            buttonsPanel.remove(addEditorsButton);
        }

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                App.showMainApp();
                frame.dispose();
            }
        });

        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChangePaswd.showChangeP(userEmail);
            }
        });

        changeEditorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChangeMainEditorDialog.showChangeMainEditorDialog(journalIssn, userEmail);
            }
        });

        retireButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isChiefEditor) {
                    ChangeMainEditorDialog.showChangeMainEditorDialog(journalIssn, userEmail, true);
                } else {
                    retireButtonPressed(userEmail);
                }
            }
        });

        publishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                publishButtonPressed();
            }
        });

        addEditorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddEditorsDialog.showAddEditorsDialog(journalIssn);
            }
        });
    }

    public static void showEditorView(String journalIssn, String userEmail) {
        frame.setContentPane(new EditorView(journalIssn, userEmail).mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(Util.WIDTH, Util.HEIGHT);
        frame.setVisible(true);
    }

    private void loadConsideredSubmissionsTable() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Decision", "Submission ID", "Title", "Abstract", "Authors' University Affiliations", "Conflicts"}, 0);
        submissionsTable.setModel(model);

        for(ConsideredSubmission cSubmission : PublicationsController.getConsideredSubmissions(journalIssn)) {
            //ArrayLists to check if already added, String to display in the table
            ArrayList<String> affiliations = new ArrayList<String>();
            String affiliationsCell = "";
            ArrayList<String> conflicts = new ArrayList<String>();
            String conflictsCell = "";

            // make automatic decisions to articles which have no decision.
            if(cSubmission.getDecision().equals("")) {
                Review[] reviews = PublicationsController.getReviews(cSubmission.getSubmissionId());

                String verdict1 = reviews[0].getFinalVerdict();
                String verdict2 = reviews[1].getFinalVerdict();
                String verdict3 = reviews[2].getFinalVerdict();

                if(
                        (verdict1.equals("Strong Accept") && !verdict2.equals("Strong Reject") && !verdict3.equals("Strong Reject"))
                                || (verdict2.equals("Strong Accept") && !verdict1.equals("Strong Reject") && !verdict3.equals("Strong Reject"))
                                || (verdict3.equals("Strong Accept") && !verdict2.equals("Strong Reject") && !verdict1.equals("Strong Reject"))
                                || (verdict1.equals("Weak Accept") && verdict2.equals("Weak Accept") && !verdict3.equals("Strong Reject"))
                                || (verdict2.equals("Weak Accept") && verdict3.equals("Weak Accept") && !verdict1.equals("Strong Reject"))
                                || (verdict3.equals("Weak Accept") && verdict1.equals("Weak Accept") && !verdict2.equals("Strong Reject"))
                ) {
                    PublicationsController.setDecision(cSubmission.getSubmissionId(), "accepted");
                    cSubmission.setDecision("accepted");
                } else if(
                        (verdict1.equals("Strong Reject") && !verdict2.equals("Strong Accept") && !verdict3.equals("Strong Accept"))
                                || (verdict2.equals("Strong Reject") && !verdict1.equals("Strong Accept") && !verdict3.equals("Strong Accept"))
                                || (verdict3.equals("Strong Reject") && !verdict2.equals("Strong Accept") && !verdict1.equals("Strong Accept"))
                                || (verdict1.equals("Weak Reject") && verdict2.equals("Weak Reject") && !verdict3.equals("Strong Accept"))
                                || (verdict2.equals("Weak Reject") && verdict3.equals("Weak Reject") && !verdict1.equals("Strong Accept"))
                                || (verdict3.equals("Weak Reject") && verdict1.equals("Weak Reject") && !verdict2.equals("Strong Accept"))
                ) {
                    PublicationsController.setDecision(cSubmission.getSubmissionId(), "rejected");
                    cSubmission.setDecision("rejected");
                }
            }

            // look for conflicts
            for(Author author : PublicationsController.getArticleAuthors(cSubmission.getSubmissionId())) {
                if(!affiliations.contains(author.getUniversityAffiliation())) {
                    affiliations.add(author.getUniversityAffiliation());
                    affiliationsCell += author.getUniversityAffiliation() + ", ";
                }
                for(Editor editor : PublicationsController.getEditors(journalIssn)) {
                    if (author.getUniversityAffiliation().equals(editor.getUniversityAffiliation()) && !conflicts.contains(editor.toString())) {
                        conflicts.add(editor.toString());
                        conflictsCell += editor.toString() + ", ";
                    }
                }
            }

            // delete last commas
            if(!affiliationsCell.equals(""))
                affiliationsCell = affiliationsCell.substring(0, affiliationsCell.length()-2);

            if(!conflictsCell.equals(""))
                conflictsCell = conflictsCell.substring(0, conflictsCell.length()-2);

            // increase the counter if the article was accepted
            if(cSubmission.getDecision().equals("accepted"))
                counter += 1;

            model.addRow(new Object[]{cSubmission.getDecision(), cSubmission.getSubmissionId(), cSubmission.getTitle(), cSubmission.getAbs(), affiliationsCell, conflictsCell});
        }

        counterLabel.setText(counter + "/8");
    }

    private void retireButtonPressed(String userEmail) {
        String query = "DELETE FROM editors WHERE email = ?";
        PreparedStatement pstmt = null;

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, userEmail);

            int res = pstmt.executeUpdate();
            System.out.println(res);

            if (PublicationsController.getRoles(userEmail).length == 0) {
                query = "DELETE FROM users WHERE email = ?";
                pstmt = con.prepareStatement(query);
                pstmt.setString(1,userEmail);
                res = pstmt.executeUpdate();
                System.out.println(res);
            }

            App.showMainApp();
            frame.dispose();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void publishButtonPressed() {
        if(counter > 8) {
            JOptionPane.showMessageDialog(null,"Maximum number of articles you can publish at once is eight.");
        } else if (counter == 0) {
            JOptionPane.showMessageDialog(null,"Accept at least one article.");
        } else {
            String conflicts = "";
            for(int i = 0; i < submissionsTable.getRowCount(); i++) {
                if(submissionsTable.getValueAt(i, 0).toString().equals("accepted")) {
                    conflicts += submissionsTable.getValueAt(i, 5).toString();
                }
            }

            if(conflicts.length() != 0) {
                JOptionPane.showMessageDialog(null,"Conflict of interest detected.");
            } else {
                PublicationsController.publishEdition(journalIssn);
                loadConsideredSubmissionsTable();
                counter = 0;
                counterLabel.setText(counter + "/8");
            }
        }
    }

    private void createUIComponents() {
        // disable editing cells in the table
        submissionsTable = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };

        // add a listener for double click
        submissionsTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    String affiliationConflict = submissionsTable.getValueAt(submissionsTable.getSelectedRow(), 5).toString();
                    if(affiliationConflict.length() != 0) {
                        JOptionPane.showMessageDialog(null,"Conflict of interest detected.\n" + affiliationConflict);
                    } else {
                        int submissionId = Integer.parseInt(submissionsTable.getValueAt(submissionsTable.getSelectedRow(), 1).toString());
                        ArticleActionsDialog.showArticleActionsDialog(submissionId, journalIssn, userEmail);
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        showEditorView("9081-8120", "rafal@gmail.com");
    }
}
