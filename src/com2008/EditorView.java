package com2008;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
    private Boolean isChiefEditor;
    private Journal journal;

    private static JFrame frame = new JFrame("Journal Dashboard");

    public EditorView(String journalIssn, Boolean isChiefEditor) {
        this.journalIssn = journalIssn;
        this.isChiefEditor = isChiefEditor;
        this.journal = PublicationsController.getJournal(journalIssn);

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
    }

    public static void showEditorView(String journalIssn, Boolean isChiefEditor) {
        frame.setContentPane(new EditorView(journalIssn, isChiefEditor).mainPanel);
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

    public static void main(String[] args) {
        showEditorView("1234-4321", false);
    }

    private void createUIComponents() {
        // disable editing cells in the table
        submissionsTable = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };

        // add listeners for enter press and for double click
        submissionsTable.setSurrendersFocusOnKeystroke(true); //make it work for the first press as well
        submissionsTable.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        submissionsTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {

                }
            }
        });
    }
}
