package com2008;

import com.javateam019.view.ChangePaswd;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ChooseReviewsView {
    private JPanel mainPanel;
    private JPanel backPanel;
    private JButton logOutButton;
    private JScrollPane articlesScrollPane;
    private JTable articlesTable;
    private JLabel counterLabel;
    private JButton changePasswordButton;
    private JButton chooseForReviewButton;
    private JButton viewButton;

    private String email;
    private int reviewerSubmissionId;
    private int reviewCount;

    private static JFrame frame = new JFrame("Articles to review");

    public ChooseReviewsView(int reviewerSubmissionId, String email) {
        this.email = email;
        this.reviewerSubmissionId = reviewerSubmissionId;
        this.reviewCount = PublicationsController.getSubmission(reviewerSubmissionId).getReviewCount();
        loadArticlesTable();
        counterLabel.setText("" + reviewCount + "/3 chosen by all authors");

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                App.showMainApp();
                frame.dispose();
            }
        });

        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChangePaswd.showChangeP(email);
            }
        });

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int submissionId = Integer.parseInt(articlesTable.getValueAt(articlesTable.getSelectedRow(), 0).toString());
                SubmissionView.showSubmissionsView(submissionId, email, reviewerSubmissionId);
                frame.dispose();
            }
        });

        chooseForReviewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int submissionId = Integer.parseInt(articlesTable.getValueAt(articlesTable.getSelectedRow(), 0).toString());
                PublicationsController.chooseSubmissionToReview(submissionId, email, reviewerSubmissionId);

                int reviewCount = PublicationsController.getSubmission(reviewerSubmissionId).getReviewCount();
                if(reviewCount < 3) {
                    JOptionPane.showMessageDialog(null, "Article chosen for review. You still need to review " + (3 - reviewCount) + " submissions.");
                    frame.dispose();
                    ChooseReviewsView.showChooseReviewsView(reviewerSubmissionId, email);
                } else {
                    JOptionPane.showMessageDialog(null, "Article chosen for review. Thank you.");
                    frame.dispose();
                    App.showMainApp();
                }
            }
        });
    }

    public static void showChooseReviewsView(int reviewerSubmissionId, String email) {
        frame.setContentPane(new ChooseReviewsView(reviewerSubmissionId, email).mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(Util.WIDTH, Util.HEIGHT);
        frame.setVisible(true);
    }

    private void loadArticlesTable() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Submission ID", "Title", "Abstract"}, 0);
        articlesTable.setModel(model);

        for(Submission article : PublicationsController.getUnreviewedSubmission(email)) {
            boolean conflictOfInterest = false;
            String usersAffilitation = PublicationsController.getAffiliation(email);
            for(Author author : PublicationsController.getArticleAuthors(article.getSubmissionId())) {
                if(author.getUniversityAffiliation().equals(usersAffilitation))
                    conflictOfInterest = true;
            }
            if(!conflictOfInterest)
                model.addRow(new Object[]{article.getSubmissionId(), article.getTitle(), article.getAbs()});
        }
    }

    private void createUIComponents() {
        // disable editing cells in the table
        articlesTable = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };
    }

    public static void main(String[] args) {
        showChooseReviewsView(25, "newnew@op.pl");
    }
}
