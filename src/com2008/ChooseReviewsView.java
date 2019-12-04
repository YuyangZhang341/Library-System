package com2008;

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
    }

    public static void showChooseReviewsView(int reviewerSubmissionId, String email) {
        frame.setContentPane(new ChooseReviewsView(reviewerSubmissionId, email).mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenDimensions = toolkit.getScreenSize();
        frame.setSize(screenDimensions.width, screenDimensions.height);
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

        // add a listener for double click
        articlesTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    int submissionId = Integer.parseInt(articlesTable.getValueAt(articlesTable.getSelectedRow(), 0).toString());
                    ChooseArticleForReviewDialog.showChooseArticleForReviewDialog(submissionId, email, reviewerSubmissionId);
                }
            }
        });
    }

    public static void main(String[] args) {
        showChooseReviewsView(25, "newnew@op.pl");
    }
}
