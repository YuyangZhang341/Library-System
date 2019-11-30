package com2008;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConsideredSubmissionView {
    private JPanel mainPanel;
    private JScrollPane authorsScrollPane;
    private JPanel topPanel;
    private JPanel infoPanel;
    private JPanel verdictsPanel;
    private JLabel verdictsLabel;
    private JPanel abstractPanel;
    private JScrollPane contentScrollPane;
    private JPanel contentPanel;
    private JLabel articleTitleLabel;
    private JButton openButton;
    private JButton backButton;
    private JTextArea abstractTextArea;
    private JTable authorsTable;
    private JTextField verdictTextField1;
    private JTextField verdictTextField2;
    private JTextField verdictTextField3;

    private int submissionId;
    private String journalIssn;
    private String userEmail;

    private static JFrame frame = new JFrame("Article");

    public ConsideredSubmissionView(int submissionId, String journalIssn, String userEmail) {
        this.submissionId = submissionId;
        this.journalIssn = journalIssn;
        this.userEmail = userEmail;

        RevisedSubmission revisedSubmission = PublicationsController.getRevisedSubmission(submissionId);
        Verdict verdicts[] = PublicationsController.getVerdicts(submissionId);

        articleTitleLabel.setText("Article title: " + revisedSubmission.getTitle());
        abstractTextArea.setText(revisedSubmission.getAbs());
        if(verdicts.length == 3) {
            verdictTextField1.setText(verdicts[0].getVerdict());
            verdictTextField2.setText(verdicts[1].getVerdict());
            verdictTextField3.setText(verdicts[2].getVerdict());
        }
        loadAuthorsTable();

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditorView.showEditorView(journalIssn, userEmail);
                frame.dispose();
            }
        });

        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // open the submission's pdf
                try {
                    if(revisedSubmission.getPdf().exists()) {
                        if(Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().open(revisedSubmission.getPdf());
                        } else {
                            System.out.println("Awt Desktop not supported.");
                        }
                    } else {
                        System.out.println("File doesn't exist.");
                    }
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public static void showConsideredSubmissionsView(int sumbissionId, String journalIssn, String userEmail) {
        frame.setContentPane(new ConsideredSubmissionView(sumbissionId, journalIssn, userEmail).mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenDimensions = toolkit.getScreenSize();
        frame.setSize(screenDimensions.width, screenDimensions.height);
        frame.setVisible(true);
    }

    private void loadAuthorsTable() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Title", "Forenames", "Surname", "University Affiliation"}, 0);
        authorsTable.setModel(model);

        for(Author author: PublicationsController.getArticleAuthors(submissionId)) {
            model.addRow(new Object[]{author.getTitle(), author.getForenames(), author.getSurname(), author.getUniversityAffiliation()});
        }
    }

    private void createUIComponents() {
        authorsTable = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };
    }
}
