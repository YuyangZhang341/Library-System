package com2008;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ArticleView {
    private JPanel mainPanel;
    private JScrollPane authorsScrollPane;
    private JPanel topPanel;
    private JPanel infoPanel;
    private JPanel titleAndIssnPanel;
    private JPanel VolNoPageRangePanel;
    private JLabel titleLabel;
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

    private int submissionId;

    private static JFrame frame = new JFrame("Article");

    public ArticleView(int submissionId) {
        this.submissionId = submissionId;

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

    public static void main(String[] args) {
        showArticleView(1);
    }
}
