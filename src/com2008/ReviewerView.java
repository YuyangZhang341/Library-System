package com2008;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReviewerView {
    private JButton button1;
    private JButton submitButton;
    private JTabbedPane tabbedPane1;
    private JTextArea summaryTextArea;
    private JPanel reviewPanel;
    private JTextArea errorsTextArea;
    private JTable criticismsTable;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JButton deleteCriticismButton;
    private JButton addCriticismButton;
    private JScrollPane submissionScrollPane;
    private JScrollPane reviewScrollPane;
    private JTextField titleTextField;
    private JPanel submissionPanel;
    private JTextArea abstractTextArea;
    private JButton pdfButton;
    private JPanel revisedPanel;
    private JScrollPane revisedScrollPane;
    private JTextField revisedTitleTextField;
    private JLabel revisedAbstractTextField;
    private JButton revisedPdfButton;
    private JPanel mainPanel;

    private int submissionId;
    private String userEmail;

    private static JFrame frame = new JFrame("Article");

    public ReviewerView(int submissionId, String userEmail) {
        this.submissionId = submissionId;
        this.userEmail = userEmail;

        addCriticismButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });

        deleteCriticismButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        // block the review if it's been submitted already


        // fill in the submission fields.
        Submission submission = PublicationsController.getSubmission(submissionId);
        titleTextField.setText(submission.getTitle());
        abstractTextArea.setText(submission.getAbs());
        pdfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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

        // delete the revised tab if it doesn't exist, fill everything in otherwise.
        RevisedSubmission revisedSubmission = PublicationsController.getRevisedSubmission(submissionId);
        if (revisedSubmission==null) {
            tabbedPane1.remove(revisedScrollPane);
        } else {
            revisedTitleTextField.setText(revisedSubmission.getTitle());
            revisedAbstractTextField.setText(revisedSubmission.getAbs());
            revisedPdfButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
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
    }

    public static void showReviewerView(int submissionId, String userEmail) {
        frame.setContentPane(new ReviewerView(submissionId, userEmail).mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenDimensions = toolkit.getScreenSize();
        frame.setSize(screenDimensions.width, screenDimensions.height);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        showReviewerView(22, "marcin@ok.pl");
    }
}
