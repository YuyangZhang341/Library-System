package com2008;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class ArticlesView {
    private JPanel backPanel;
    private JScrollPane articlesScrollPane;
    private JTable articlesTable;
    private JPanel articlesPanel;
    private JButton backButton;
    private JButton openButton;
    private JButton homeButton;

    private int submissionID;
    private String issn;
    private int vol;
    private int number;
    private int startPage;
    private int endPage;

    private static JFrame frame = new JFrame("Articles");

    public ArticlesView(String issn, int vol, int number) {
        this.issn = issn;
        this.vol = vol;
        this.number = number;

        loadArticlesTable();

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                App.showMainApp();
                frame.dispose();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditionsView.showEditionsView(issn, vol);
                frame.dispose();
            }
        });

        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSelectedArticle();
            }
        });
    }

    public static void showArticlesView(String issn, int vol, int number) {
        frame.setContentPane(new ArticlesView(issn, vol, number).articlesPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(Util.WIDTH, Util.HEIGHT);
        frame.setVisible(true);
    }

    private void showSelectedArticle() {
        int targetSubmissionID = Integer.parseInt(articlesTable.getValueAt(articlesTable.getSelectedRow(), 0).toString());

        ArticleView.showArticleView(targetSubmissionID);
        frame.dispose();
    }

    private void loadArticlesTable() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Submission ID", "Title", "Abstract", "Start Page", "End Page"}, 0);
        articlesTable.setModel(model);

        for(Article article: PublicationsController.getArticles(issn,vol,number)) {
            model.addRow(new Object[]{article.getSubmissionId(), article.getTitle(), article.getAbs(), article.getStartPage(), article.getEndPage()});
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
                    showSelectedArticle();
                }
            }
        });
    }
}
