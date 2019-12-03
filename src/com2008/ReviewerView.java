package com2008;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReviewerView {
    private JButton button1;
    private JButton submitButton;
    private JTabbedPane tabbedPane1;
    private JTextArea summaryTextArea;
    private JPanel reviewPanel;
    private JTextArea errorsTextArea;
    private JTable criticismsTable;
    private JComboBox initialVerdictComboBox;
    private JComboBox finalVerdictComboBox;
    private JButton deleteCriticismButton;
    private JButton addCriticismButton;
    private JScrollPane submissionScrollPane;
    private JScrollPane reviewScrollPane;
    private JTextField titleTextField;
    private JPanel submissionPanel;
    private JTextArea abstractTextArea;
    private JButton pdfButton;
    private JPanel revisedPanel;
    private JScrollPane revisedScrollPane;
    private JTextField revisedTitleTextField;
    private JLabel revisedAbstractTextField;
    private JButton revisedPdfButton;
    private JPanel mainPanel;

    private int submissionId;
    private String userEmail;

    private static JFrame frame = new JFrame("Article");

    public ReviewerView(int submissionId, String userEmail) {
        this.submissionId = submissionId;
        this.userEmail = userEmail;

        pdfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        revisedPdfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        addCriticismButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        deleteCriticismButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        // block the review fields if it's been submitted already
        Review review = PublicationsController.getReview(submissionId, userEmail);
        System.out.println(review == null);
        if(review != null) {
            summaryTextArea.setText(review.getSummary());
            summaryTextArea.setEditable(false);
            errorsTextArea.setText(review.getTypographicalErrors());
            errorsTextArea.setEditable(false);
            reviewPanel.remove(addCriticismButton);
            reviewPanel.remove(deleteCriticismButton);
            loadCriticismsTable(review.getReviewerId());
            switch(review.getInitialVerdict()) {
                case "Weak Accept":
                    initialVerdictComboBox.setSelectedIndex(1);
                    break;
                case "Weak Reject":
                    initialVerdictComboBox.setSelectedIndex(2);
                    break;
                case "Strong Reject":
                    initialVerdictComboBox.setSelectedIndex(3);
                    break;
                default:
                    initialVerdictComboBox.setSelectedIndex(0);
            }
            initialVerdictComboBox.setEnabled(false);
        }

        // fill in the submission fields.


        // delete the revised tab if it doesn't exist, fill everything in otherwise.
    }

    public static void showReviewerView(int submissionId, String userEmail) {
        frame.setContentPane(new ReviewerView(submissionId, userEmail).mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenDimensions = toolkit.getScreenSize();
        frame.setSize(screenDimensions.width, screenDimensions.height);
        frame.setVisible(true);
    }

    private void loadCriticismsTable(int reviewerId) {
        Criticism[] criticisms = PublicationsController.getCriticisms(submissionId, reviewerId);
        boolean displayingResponses = criticisms[0] != null && criticisms[0].getResponse() != null;

        DefaultTableModel model = new DefaultTableModel(new String[]{"Criticism"}, 0);

        if(displayingResponses) {
            model = new DefaultTableModel(new String[]{"Criticism", "Response"}, 0);
        }

        criticismsTable.setModel(model);

        for(Criticism criticism : criticisms) {
            if(displayingResponses) {
                model.addRow(new Object[]{criticism.getCriticism(), criticism.getResponse()});
            } else {
                model.addRow(new Object[]{criticism.getCriticism()});
            }
        }
    }

    public static void main(String[] args) {
        showReviewerView(22, "marcin@ok.pl");
    }
}
