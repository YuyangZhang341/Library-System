package com2008;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class AddSubmissionView {
    private JPanel mainPanel;
    private JScrollPane contentScrollPane;
    private JButton backButton;
    private JButton submitButton;
    private JPanel topPanel;
    private JPanel mainAuthorPanel;
    private JTextField emailField;
    private JTextField titleField;
    private JLabel emailLabel;
    private JLabel titleLabel;
    private JTextField forenamesField;
    private JLabel forenamesLabel;
    private JTextField surnameField;
    private JTextField universityAffiliationFIeld;
    private JLabel universityAffiliationLabel;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JScrollPane coauthorsScrollPane;
    private JTable coauthorsTable;
    private JPanel titlePanel;
    private JLabel articleTitleLabel;
    private JTextField articleTitleField;
    private JButton pdfButton;
    private JButton addRowButton;
    private JPanel abstractPanel;
    private JPanel backPanel;
    private JLabel mainAuthorLabel;
    private JPanel scrollPanel;
    private JTextArea abstractTextArea;
    private DefaultTableModel coauthorsTableModel = new DefaultTableModel(new String[]{"Email", "Title", "Forenames", "Surname", "University Affiliation", "Password"}, 0);

    private static JFrame frame = new JFrame("Add Submission");


    public AddSubmissionView() {
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new App().main(new String[0]);
                frame.dispose();
            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO:: open the article's pdf
            }
        });

        addRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                coauthorsTable.setModel(coauthorsTableModel);
                coauthorsTableModel.addRow(new Object[]{"","","","",""});
            }
        });
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (verifyFields()) {


                    // add main author
                    PublicationsController.addUser(emailField.getText(), titleField.getText(), forenamesField.getText(),
                            surnameField.getText(), universityAffiliationLabel.getText(), passwordLabel.getText());

                    // add coauthors

                    // add article
                    PublicationsController.addSubmission(articleTitleLabel.getText(), abstractTextArea.getText(), "", emailField.getText());

                    // connect authors to the article
                } else {
                    //TODO:: not correct
                }
            }
        });
    }

    public static void showAddSubmissionView() {
        frame.setContentPane(new AddSubmissionView().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenDimensions = toolkit.getScreenSize();
        frame.setSize(screenDimensions.width, screenDimensions.height);
        frame.setVisible(true);
    }

    public boolean verifyFields() {
        //TODO::
        return false;
    }

    private void createUIComponents() {
        coauthorsTable = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return true;
            };
        };
    }

    public static void main (String[] args) {
        showAddSubmissionView();
    }
}
