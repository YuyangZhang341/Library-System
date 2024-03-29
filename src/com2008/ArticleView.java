package com2008;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class ArticleView {
    private JPanel mainPanel;
    private JPanel topPanel;
    private JPanel infoPanel;
    private JPanel titleAndIssnPanel;
    private JPanel VolNoPageRangePanel;
    private JLabel journalNameLabel;
    private JLabel issnLabel;
    private JLabel volLabel;
    private JLabel pageRangeLabel;
    private JPanel abstractPanel;
    private JScrollPane contentScrollPane;
    private JPanel contentPanel;
    private JLabel articleTitleLabel;
    private JButton openButton;
    private JButton backButton;
    private JTextArea abstractTextArea;
    private JTextField journalNameField;
    private JTextField issnField;
    private JTextField volField;
    private JTextField pageRangeField;
    private JTextField articleTitleField;
    private JLabel noLabel;
    private JTextField noField;
    private JButton homeButton;

    private int submissionId;

    private static JFrame frame = new JFrame("Article");

    public ArticleView(int submissionId) {
        this.submissionId = submissionId;

        Article article = PublicationsController.getArticle(submissionId);
        String issn = article.getIssn();
        Journal journal = PublicationsController.getJournal(issn);

        int vol = article.getVol();
        int no = article.getNumber();

        journalNameField.setText(journal.getName());
        issnField.setText(issn);
        volField.setText("" + vol);
        noField.setText("" + no);
        pageRangeField.setText(article.getStartPage() + " - " + article.getEndPage());
        articleTitleField.setText(article.getTitle());
        abstractTextArea.setText(article.getAbs());

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArticlesView.showArticlesView(issn, vol, no);
                frame.dispose();
            }
        });

        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // open the submission's pdf
                try {
                    if(article.getPdf().exists()) {
                        if(Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().open(article.getPdf());
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

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                App.showMainApp();
                frame.dispose();
            }
        });
    }

    public static void showArticleView(int sumbissionId) {
        frame.setContentPane(new ArticleView(sumbissionId).mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(Util.WIDTH, Util.HEIGHT);
        frame.setVisible(true);
    }
}
