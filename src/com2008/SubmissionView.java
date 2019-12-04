package com2008;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SubmissionView {
    private JScrollPane contentScrollPane;
    private JPanel contentPanel;
    private JPanel titleAndPdfPanel;
    private JLabel articleTitleLabel;
    private JButton openButton;
    private JTextField articleTitleField;
    private JTextArea abstractTextArea;
    private JButton backButton;
    private JPanel mainPanel;
    private JScrollPane abstractScrollPane;
    private JButton chooseToReviewTheButton;

    private int submissionId;
    private String userEmail;

    private static JFrame frame = new JFrame("Article");

    public SubmissionView(int submissionId, String userEmail) {
        this.submissionId = submissionId;
        this.userEmail = userEmail;

        Submission submission = PublicationsController.getSubmission(submissionId);
        Review[] reviews = PublicationsController.getReviews(submissionId);

        articleTitleField.setText(submission.getTitle());
        abstractTextArea.setText(submission.getAbs());

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChooseReviewsView.showChooseReviewsView(userEmail);
                frame.dispose();
            }
        });

        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // open the submission's pdf
                try {
                    if(submission.getPdf().exists()) {
                        if(Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().open(submission.getPdf());
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

        chooseToReviewTheButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PublicationsController.chooseSubmissionToReview(submissionId, userEmail);
                JOptionPane.showMessageDialog(null, "Article chosen for review");
            }
        });
    }

    public static void showSubmissionsView(int submissionId, String userEmail) {
        frame.setContentPane(new SubmissionView(submissionId, userEmail).mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenDimensions = toolkit.getScreenSize();
        frame.setSize(screenDimensions.width, screenDimensions.height);
        frame.setVisible(true);
    }
}
