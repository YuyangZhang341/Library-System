package com2008;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ArticleActionsDialog extends JDialog {
    private JPanel contentPane;
    private JButton viewButton;
    private JButton acceptButton;
    private JButton delayButton;
    private JButton rejectButton;

    private int submissionId;
    private String journalIssn;
    private String userEmail;
    static ArticleActionsDialog d;

    public ArticleActionsDialog(int submissionId, String journalIssn, String userEmail) {
        this.submissionId = submissionId;
        this.journalIssn = journalIssn;
        this.userEmail = userEmail;

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
                makeDecision("accepted");
            }
        });

        rejectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeDecision("rejected");
            }
        });

        delayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeDecision("delayed");
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
        ConsideredSubmissionView.showConsideredSubmissionsView(submissionId, journalIssn, userEmail);
        dispose();
        ((Window)d.getParent()).dispose();
    }

    private void makeDecision(String decision) {
        PublicationsController.setDecision(submissionId, decision);

        dispose();
        ((Window)d.getParent()).dispose();
        EditorView.showEditorView(journalIssn, userEmail);
    }

    public static void showArticleActionsDialog(int submissionId, String journalIssn, String userEmail) {
        d = new ArticleActionsDialog(submissionId, journalIssn, userEmail);
        d.pack();
        d.setLocationRelativeTo(null);
        d.setVisible(true);
    }
}
