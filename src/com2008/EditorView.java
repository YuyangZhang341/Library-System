package com2008;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditorView {
    private JPanel mainPanel;
    private JPanel navPanel;
    private JPanel contentPanel;
    private JButton backButton;
    private JButton changePasswordButton;
    private JPanel articlesPanel;
    private JPanel infoPanel;
    private JTable submissionsTable;
    private JTextField nameTextField;
    private JTextField issnTextField;
    private JButton changeEditorButton;
    private JButton publishButton;
    private JScrollPane contentScrollPane;
    private JPanel buttonsPanel;
    private JButton retireButton;
    private JTextField chiefEditorTextField;

    private String journalIssn;
    private String userEmail;
    private Boolean isChiefEditor;
    private Journal journal;

    private static JFrame frame = new JFrame("Journal Dashboard");

    public EditorView(String journalIssn, String userEmail) {
        this.journalIssn = journalIssn;
        this.userEmail = userEmail;
        this.journal = PublicationsController.getJournal(journalIssn);
        this.isChiefEditor = userEmail.equals(journal.getChiefEditorEmail());

        loadConsideredSubmissionsTable();

        nameTextField.setText(journal.getName());
        issnTextField.setText(journalIssn);
        chiefEditorTextField.setText(journal.getChiefEditorEmail());

        // Change interface depending on who's viewing it
        if (isChiefEditor) {
            buttonsPanel.remove(retireButton);
        } else {
            buttonsPanel.remove(changeEditorButton);
            buttonsPanel.remove(publishButton);
        }

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        changeEditorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(journalIssn);
                ChangeMainEditorDialog.showChangeMainEditorDialog(journalIssn, userEmail);
            }
        });

        retireButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        publishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public static void showEditorView(String journalIssn, String userEmail) {
        frame.setContentPane(new EditorView(journalIssn, userEmail).mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenDimensions = toolkit.getScreenSize();
        frame.setSize(screenDimensions.width, screenDimensions.height);
        frame.setVisible(true);
    }

    private void loadConsideredSubmissionsTable() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Decision", "Submission ID", "Title", "Abstract"}, 0);
        submissionsTable.setModel(model);

        for(ConsideredSubmission cSubmission : PublicationsController.getConsideredSubmissions(journalIssn)) {
            model.addRow(new Object[]{cSubmission.getDecision(), cSubmission.getSubmissionId(), cSubmission.getTitle(), cSubmission.getAbs()});
        }
    }

    private void createUIComponents() {
        // disable editing cells in the table
        submissionsTable = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };

        // add a listener for double click
        submissionsTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    int submissionId = Integer.parseInt(submissionsTable.getValueAt(submissionsTable.getSelectedRow(), 1).toString());
                    ArticleActionsDialog.showArticleActionsDialog(submissionId);
                }
            }
        });
    }

    public static void main(String[] args) {
        showEditorView("1234-4321", "");
    }
}
