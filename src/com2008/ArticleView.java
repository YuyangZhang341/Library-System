package com2008;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class ArticleView {
    private JPanel mainPanel;
    private JScrollPane authorsScrollPane;
    private JPanel topPanel;
    private JPanel infoPanel;
    private JPanel titleAndIssnPanel;
    private JPanel VolNoPageRangePanel;
    private JLabel journalNameLabel;
    private JLabel issnLabel;
    private JLabel volNoLabel;
    private JLabel pageRangeLabel;
    private JPanel abstractPanel;
    private JScrollPane contentScrollPane;
    private JPanel contentPanel;
    private JLabel articleTitleLabel;
    private JButton openButton;
    private JButton backButton;
    private JTextArea abstractTextArea;
    private JTable authorsTable;

    private int submissionId;

    private static JFrame frame = new JFrame("Article");

    public ArticleView(int submissionId) {
        this.submissionId = submissionId;

        Map<String, String> articleInfo = PublicationsController.getArticleInfo(submissionId);

        journalNameLabel.setText("Journal: " + articleInfo.get("name"));
        issnLabel.setText("ISSN: " + articleInfo.get("issn"));
        volNoLabel.setText("vol. " + articleInfo.get("vol") + ", no. " + articleInfo.get("no"));
        pageRangeLabel.setText("Page range: " + articleInfo.get("startPage") + " - " + articleInfo.get("endPage"));
        articleTitleLabel.setText("Article title: " + articleInfo.get("title"));
        abstractTextArea.setText(articleInfo.get("abstract"));

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public static void showArticleView(int sumbissionId) {
        frame.setContentPane(new ArticleView(sumbissionId).mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenDimensions = toolkit.getScreenSize();
        frame.setSize(screenDimensions.width, screenDimensions.height);
        frame.setVisible(true);
    }

    private void createUIComponents() {
        authorsTable = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };

        PublicationsController.fetchArticleAuthors(authorsTable, submissionId);
    }
}
