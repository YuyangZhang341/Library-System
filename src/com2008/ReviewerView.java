package com2008;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
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
    private JLabel finalVerdictLabel;

    private int submissionId;
    private String userEmail;
    private int reviewerId;

    private static JFrame frame = new JFrame("Article");
    DefaultTableModel criticismsTableModel = new DefaultTableModel(new String[]{"Criticism"}, 0);

    public ReviewerView(int submissionId, String userEmail) {
        this.submissionId = submissionId;
        this.userEmail = userEmail;
        this.reviewerId = PublicationsController.getReviewerId(submissionId, userEmail);

        addCriticismButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                criticismsTable.setModel(criticismsTableModel);
                criticismsTableModel.addRow(new Object[]{""});
            }
        });

        deleteCriticismButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(criticismsTableModel.getRowCount() > 0 )
                    criticismsTableModel.removeRow(criticismsTableModel.getRowCount() - 1);
            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(verifyFields()) {
                    Review review = new Review(
                            reviewerId, summaryTextArea.getText(), errorsTextArea.getText(),
                            initialVerdictComboBox.getSelectedItem().toString(), submissionId, null);

                    Criticism[] criticisms = new Criticism[criticismsTable.getRowCount()];
                    for(int i = 0; i < criticismsTable.getRowCount(); i++) {
                        criticisms[i] = new Criticism(-1, submissionId, reviewerId, criticismsTable.getValueAt(i, 0).toString(), null);
                    }

                    PublicationsController.addInitialReview(review, criticisms);
                    frame.dispose();
                    ReviewerView.showReviewerView(reviewerId, userEmail);
                }
            }
        });


        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        // block the review fields if it's been submitted already
        Review review = PublicationsController.getReview(submissionId, userEmail);
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
        Submission submission = PublicationsController.getSubmission(submissionId);
        titleTextField.setText(submission.getTitle());
        abstractTextArea.setText(submission.getAbs());
        pdfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(submission.getPdf().exists()) {
                        if(Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().open(submission.getPdf());
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

        // delete the revised tab if it doesn't exist, fill everything in otherwise.
        RevisedSubmission revisedSubmission = PublicationsController.getRevisedSubmission(submissionId);
        if (revisedSubmission==null) {
            tabbedPane1.remove(revisedScrollPane);
            reviewPanel.remove(finalVerdictLabel);
            reviewPanel.remove(finalVerdictComboBox);
        } else {
            revisedTitleTextField.setText(revisedSubmission.getTitle());
            revisedAbstractTextField.setText(revisedSubmission.getAbs());
            revisedPdfButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        if(revisedSubmission.getPdf().exists()) {
                            if(Desktop.isDesktopSupported()) {
                                Desktop.getDesktop().open(revisedSubmission.getPdf());
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
        }
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
        boolean displayingResponses = criticisms.length >= 1 && criticisms[0].getResponse() != null;

        if(displayingResponses) {
            criticismsTableModel = new DefaultTableModel(new String[]{"Criticism", "Response"}, 0);
        }

        criticismsTable.setModel(criticismsTableModel);

        for(Criticism criticism : criticisms) {
            if(displayingResponses) {
                criticismsTableModel.addRow(new Object[]{criticism.getCriticism(), criticism.getResponse()});
            } else {
                criticismsTableModel.addRow(new Object[]{criticism.getCriticism()});
            }
        }
    }

    private boolean verifyFields() {
        JTextArea[] areas = {summaryTextArea, errorsTextArea};

        // set everything to white (if previously was red)
        for(JTextArea area : areas) {
            area.setBackground(Color.white);
        }

        // check fields for forbidden characters
        for(JTextArea area : areas) {
            if(! Util.checkForbiddenCharacters(area.getText())) {
                area.setBackground(Color.red);
                JOptionPane.showMessageDialog(null,"Characters ; : / \\ are forbidden.");
                return false;
            }

            if(area.getText().equals("")) {
                area.setBackground(Color.red);
                JOptionPane.showMessageDialog(null,"Fill out all the fields.");
                return false;
            }
        }

        // check if table full and doesn't contain forbidden characters
        if(! Util.verifyTable(criticismsTable))
            return false;

        return true;
    }

    public static void main(String[] args) {
        showReviewerView(22, "rafal@ok.pl");
    }
}
