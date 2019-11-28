package com2008;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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

    private static JFrame frame = new JFrame("Add Submission");
    private File pdfFile = null;
    private int submissionId;
    private String userEmail;
    private Submission submission;

    public ReviseArticleView(int submissionId, String userEmail) {
        this.submissionId = submissionId;
        this.userEmail = userEmail;
        this.submission = PublicationsController.getSubmission(submissionId);

        articleTitleField.setText(submission.getTitle());
        abstractTextArea.setText(submission.getAbs());

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
                    //TODO: reply to the criticisms

                    // add revised article
                    // if new pdf not chosen, add the old one.
                    File pdfToAdd = (pdfFile == null? submission.getPdf() : pdfFile);
                    RevisedSubmission revisedSubmission = new RevisedSubmission(submissionId, articleTitleField.getText(), abstractTextArea.getText(), pdfToAdd);
                    PublicationsController.addRevisedSubmission(revisedSubmission);

                    JOptionPane.showMessageDialog(null,"Submitted.");
                    App.showMainApp();
                    frame.dispose();
                } else {
                    //TODO:: not correct
                }
            }
        });
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
        //TODO:: check emails, check if no empty rows ets.
        return true;
    }
}
