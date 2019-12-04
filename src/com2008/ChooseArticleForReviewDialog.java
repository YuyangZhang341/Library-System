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

    public ChooseArticleForReviewDialog(int submissionId) {
        this.submissionId = submissionId;

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
                viewArticle(submissionId);
            }
        });

        chooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseArticle();
            }
        });
    }

    private void viewArticle(int submissionId) {
//        ArticleView.showArticleView(submissionId);
        dispose();
        ((Window)d.getParent()).dispose();
    }

    private void chooseArticle() {

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void showChooseArticleForReviewDialog(int submissionId) {
        d = new ChooseArticleForReviewDialog(submissionId);
        d.pack();
        d.setLocationRelativeTo(null);
        d.setVisible(true);
    }

    public static void main(String[] args) {
        ChooseArticleForReviewDialog dialog = new ChooseArticleForReviewDialog(23);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
