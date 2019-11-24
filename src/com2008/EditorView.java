package com2008;

import javax.swing.*;
import java.awt.*;

public class EditorView {
    private JPanel mainPanel;
    private JPanel navPanel;
    private JPanel contentPanel;
    private JButton backButton;
    private JButton changePasswordButton;
    private JPanel articlesPanel;
    private JPanel infoPanel;
    private JTable articlesTable;
    private JTextField titleTextField;
    private JTextField ISSNTextField;
    private JButton changeEditorButton;
    private JButton publishButton;
    private JScrollPane contentScrollPane;
    private JPanel buttonsPanel;
    private JButton retireButton;

    private static JFrame frame = new JFrame("Journal Dashboard");

    public EditorView(Editor editor) {
        buttonsPanel.remove(publishButton);
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

    public static void main(String[] args) {
        showEditorView("1234-4321", true);
    }
}
