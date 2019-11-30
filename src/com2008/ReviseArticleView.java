package com2008;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class ReviseArticleView {
    private JPanel mainPanel;
    private JScrollPane contentScrollPane;
    private JButton backButton;
    private JButton submitButton;
    private JPanel titlePanel;
    private JLabel articleTitleLabel;
    private JTextField articleTitleField;
    private JButton pdfButton;
    private JPanel abstractPanel;
    private JPanel scrollPanel;
    private JTextArea abstractTextArea;
    private JPanel articlePanel;
    private JPanel criticismsPanel;
    private JTable criticismsTable;

    private static JFrame frame = new JFrame("Add Submission");
    private File pdfFile = null;
    private int submissionId;
    private String userEmail;
    private Submission submission;
    private Criticism[] criticisms;

    public ReviseArticleView(int submissionId, String userEmail) {
        this.submissionId = submissionId;
        this.userEmail = userEmail;
        this.submission = PublicationsController.getSubmission(submissionId);
        this.criticisms = PublicationsController.getAllCriticisms(submissionId);

        articleTitleField.setText(submission.getTitle());
        abstractTextArea.setText(submission.getAbs());

        loadCriticismsTable();

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AuthorView.showAuthorView(submissionId, userEmail);
                frame.dispose();
            }
        });

        pdfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "PDF files", "pdf");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(frame);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    pdfFile = chooser.getSelectedFile();
                }
            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (verifyFields()) {
                    // respond to criticisms
                    for(int i = 0; i < criticisms.length; i++) {
                        PublicationsController.respondToCriticism(criticisms[i].getCriticismID(), criticismsTable.getValueAt(i, 2).toString());
                    }

                    // add revised article
                    // if new pdf not chosen, add the old one.
                    File pdfToAdd = (pdfFile == null? submission.getPdf() : pdfFile);
                    RevisedSubmission revisedSubmission = new RevisedSubmission(submissionId, articleTitleField.getText(), abstractTextArea.getText(), pdfToAdd);
                    PublicationsController.addRevisedSubmission(revisedSubmission);

                    JOptionPane.showMessageDialog(null,"Article revised.");
                    AuthorView.showAuthorView(submissionId, userEmail);
                    frame.dispose();
                }
            }
        });
    }

    private void loadCriticismsTable() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Reviewer", "Criticism", "Response"}, 0);
        criticismsTable.setModel(model);
        criticismsTable.getColumnModel().getColumn(0).setMaxWidth(250);

        for(int i = 0; i < criticisms.length; i++) {
            model.addRow(new Object[]{"Reviewer " + criticisms[i].getReviewerID(), criticisms[i].getCriticism(), ""});
        }
    }

    public static void showReviseArticleView(int submissionId, String userEmail) {
        frame.setContentPane(new ReviseArticleView(submissionId, userEmail).mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenDimensions = toolkit.getScreenSize();
        frame.setSize(screenDimensions.width, screenDimensions.height);
        frame.setVisible(true);
    }

    public boolean verifyFields() {
        // set everything to white (if previously was red)
        articleTitleField.setBackground(Color.white);
        abstractTextArea.setBackground(Color.white);

        if(! Util.checkForbiddenCharacters(articleTitleField.getText())) {
            articleTitleField.setBackground(Color.red);
            JOptionPane.showMessageDialog(null,"Characters ; : / \\ are forbidden.");
            return false;
        }

        if(! Util.checkForbiddenCharacters(abstractTextArea.getText())) {
            abstractTextArea.setBackground(Color.red);
            JOptionPane.showMessageDialog(null,"Characters ; : / \\ are forbidden.");
            return false;
        }

        if(articleTitleField.getText().equals("")) {
            articleTitleField.setBackground(Color.red);
            JOptionPane.showMessageDialog(null,"Fill out the article's title.");
            return false;
        }

        if(abstractTextArea.getText().equals("")) {
            abstractTextArea.setBackground(Color.red);
            JOptionPane.showMessageDialog(null,"Fill out the article's abstract.");
            return false;
        }

        // check if table full and doesn't contain forbidden characters
        if(! Util.verifyTable(criticismsTable))
            return false;

        return true;
    }

    public static void main(String[] args) {
        showReviseArticleView(15, "pdf@op.pl");
    }

    private void createUIComponents() {
        criticismsTable = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return (column == 2);
            };
        };
    }
}
