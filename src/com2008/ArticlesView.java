package com2008;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
                int targetSubmissionID = Integer.parseInt(articlesTable.getValueAt(articlesTable.getSelectedRow(), 0).toString());
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


    private void createUIComponents() {
        articlesTable = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };

        PublicationsController.fetchArticles(articlesTable, issn, vol, number);
    }
}
