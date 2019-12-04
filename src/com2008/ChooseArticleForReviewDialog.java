package com2008;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChooseArticleForReviewDialog extends JDialog {
    private JPanel contentPane;
    private JButton viewButton;
    private JButton chooseButton;
    static ChooseArticleForReviewDialog d;

    private int submissionId;
    private String userEmail;
    private int reviewerSubmissionId;

    public ChooseArticleForReviewDialog(int submissionId, String userEmail, int reviewerSubmissionId) {
        this.submissionId = submissionId;
        this.userEmail = userEmail;
        this.reviewerSubmissionId = reviewerSubmissionId;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(viewButton);

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewArticle(submissionId, userEmail, reviewerSubmissionId);
                ((Window)d.getParent()).dispose();
                dispose();
            }
        });

        chooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseArticle(submissionId, userEmail);
                ((Window)d.getParent()).dispose();
                dispose();
                ChooseReviewsView.showChooseReviewsView(reviewerSubmissionId, userEmail);
            }
        });
    }

    private void viewArticle(int submissionId, String userEmail, int reviewerSubmissionId) {
        SubmissionView.showSubmissionsView(submissionId, userEmail, reviewerSubmissionId);
    }

    private void chooseArticle(int submissionId, String userEmail) {
        PublicationsController.chooseSubmissionToReview(submissionId, userEmail);
//        JOptionPane.showMessageDialog(d, "Article chosen for review");
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void showChooseArticleForReviewDialog(int submissionId, String userEmail, int reviewerSubmissionId) {
        d = new ChooseArticleForReviewDialog(submissionId, userEmail, reviewerSubmissionId);
        d.pack();
        d.setLocationRelativeTo(null);
        d.setVisible(true);
    }
}
