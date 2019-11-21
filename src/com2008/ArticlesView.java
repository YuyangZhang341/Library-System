package com2008;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ArticlesView {
    private JPanel backPanel;
    private JScrollPane articlesScrollPane;
    private JTable articlesTable;
    private JPanel articlesPanel;
    private JButton backButton;
    private JButton openButton;

    private int submissionID;
    private String issn;
    private int vol;
    private int number;
    private int startPage;
    private int endPage;

    //TODO: set frame title to be the current journal's names with article number
    private static JFrame frame = new JFrame("Articles");

    public ArticlesView(String issn, int vol, int number) {
        this.issn = issn;
        this.vol = vol;
        this.number = number;

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

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenDimensions = toolkit.getScreenSize();
        frame.setSize(screenDimensions.width, screenDimensions.height);
        frame.setVisible(true);
    }

    private void showSelectedArticle() {
        int targetSubmissionID = Integer.parseInt(articlesTable.getValueAt(articlesTable.getSelectedRow(), 0).toString());

        ArticleView.showArticleView(targetSubmissionID);
        frame.dispose();
    }

    private void createUIComponents() {
        // disable editing cells in the table
        articlesTable = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };

        // fill the table with data
        PublicationsController.fetchArticles(articlesTable, issn, vol, number);

        // add listeners for enter press and for double click
        articlesTable.setSurrendersFocusOnKeystroke(true); //make it work for the first press as well
        articlesTable.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                showSelectedArticle();
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

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
