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

    public ChooseArticleForReviewDialog(int submissionId, String userEmail) {
        this.submissionId = submissionId;
        this.userEmail = userEmail;

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
                viewArticle(submissionId, userEmail);
            }
        });

        chooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseArticle(submissionId, userEmail);
            }
        });
    }

    private void viewArticle(int submissionId, String userEmail) {
        SubmissionView.showSubmissionsView(submissionId, userEmail);
        System.out.println(d.getParent().getName());
        ((Window)d.getParent()).dispose();
        dispose();
    }

    private void chooseArticle(int submissionId, String userEmail) {
        PublicationsController.chooseSubmissionToReview(submissionId, userEmail);
        dispose();
        JOptionPane.showMessageDialog(d, "Article chosen for review");
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void showChooseArticleForReviewDialog(int submissionId, String userEmail) {
        d = new ChooseArticleForReviewDialog(submissionId, userEmail);
        d.pack();
        d.setLocationRelativeTo(null);
        d.setVisible(true);
    }

    public static void main(String[] args) {
        ChooseArticleForReviewDialog dialog = new ChooseArticleForReviewDialog(23, "marcin@ok.pl");
        dialog.pack();
        dialog.setVisible(true);
    }
}
