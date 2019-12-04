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

    private String email;

    private static JFrame frame = new JFrame("Articles to review");

    public ChooseReviewsView(String email) {
        this.email = email;
        loadArticlesTable();

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                App.showMainApp();
                frame.dispose();
            }
        });
    }

    public static void showChooseReviewsView(String email) {
        frame.setContentPane(new ChooseReviewsView(email).mainPanel);
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

        for(Submission article : PublicationsController.getUnreviewedSubmission()) {
            System.out.println("hello");
            boolean conflictOfInterest = false;
            String usersAffilitation = PublicationsController.getAffiliation(email);
            for(Author author : PublicationsController.getArticleAuthors(article.getSubmissionId())) {
                System.out.println("bye");
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
//                    ChooseArticleForReviewDialog.
                }
            }
        });
    }

    public static void main(String[] args) {
        showChooseReviewsView("marcin@ok.pl");
    }
}
