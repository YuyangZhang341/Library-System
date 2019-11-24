package com2008;

import javax.swing.*;
import java.awt.event.*;

public class ArticleActionsDialog extends JDialog {
    private JPanel contentPane;
    private JButton viewButton;
    private JButton acceptButton;
    private JButton delayButton;

    public ArticleActionsDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(viewButton);

        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewArticle();
            }
        });

        acceptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                acceptArticle();
            }
        });

        delayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                delayArticle();
            }
        });

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
    }

    private void onCancel() {
        dispose();
    }

    private void viewArticle() {

    }

    private void acceptArticle() {

    }

    private void delayArticle() {

    }

    public static void showArticleActionsDialog() {
        ArticleActionsDialog d = new ArticleActionsDialog();
        d.pack();
        d.setLocationRelativeTo(null);
        d.setVisible(true);
    }

    public static void main(String[] args) {
        showArticleActionsDialog();
    }
}
