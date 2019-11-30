package com2008;

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
                    ChangeMainEditorDialog.showChangeMainEditorDialog(journalIssn, userEmail);
                }
                retireButtonPressed(userEmail);
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

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenDimensions = toolkit.getScreenSize();
        frame.setSize(screenDimensions.width, screenDimensions.height);
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
        System.out.println(userEmail);
        Statement stmt = null;

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();

            int res = stmt.executeUpdate("DELETE FROM editors WHERE email = '" + userEmail + "'");
            System.out.println(res);

            if (PublicationsController.getRoles(userEmail).length == 0) {
                res = stmt.executeUpdate("DELETE FROM users WHERE email = '" + userEmail + "'");
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
            JOptionPane.showMessageDialog(null,"Maximum number of articles you can publish at once is 8.");
        } else {
            PublicationsController.publishEdition(journalIssn);
            loadConsideredSubmissionsTable();
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
        showEditorView("1234-4321", "abc@abc.co.uk");
    }
}
