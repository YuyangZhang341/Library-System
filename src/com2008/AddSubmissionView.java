package com2008;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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
    private JTextField universityAffiliationField;
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
    private JLabel surnameLabel;
    private JTextField targetIssnTextField;
    private JButton removeRowButton;
    private DefaultTableModel coauthorsTableModel = new DefaultTableModel(new String[]{"Email", "Title", "Forenames", "Surname", "University Affiliation", "Password"}, 0);

    private static JFrame frame = new JFrame("Add Submission");
    private File pdfFile = null;

    public AddSubmissionView() {
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new App().main(new String[0]);
                frame.dispose();
            }
        });

        pdfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "PDF files", "pdf");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(frame);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    pdfFile = chooser.getSelectedFile();
                }
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
                // stop editing the table
                coauthorsTable.getSelectionModel().clearSelection();

                if (verifyFields()) {
                    Author authors[] = new Author[coauthorsTable.getRowCount() + 1];
                    authors[0] = new Author(emailField.getText(), titleField.getText(), forenamesField.getText(), surnameField.getText(),
                                            universityAffiliationField.getText(), String.valueOf(passwordField.getPassword()));

                    for(int i = 0; i < coauthorsTable.getRowCount(); i++) {
                        authors[i+1] = new Author(coauthorsTable.getValueAt(i, 0).toString(), coauthorsTable.getValueAt(i,1).toString(),
                                                  coauthorsTable.getValueAt(i, 2).toString(), coauthorsTable.getValueAt(i, 3).toString(),
                                                  coauthorsTable.getValueAt(i, 4).toString(), coauthorsTable.getValueAt(i, 5).toString());
                    }

                    Submission submission = new Submission(-1, articleTitleField.getText(), abstractTextArea.getText(), pdfFile, emailField.getText(), targetIssnTextField.getText());

                    PublicationsController.addSubmission(submission, authors);

                    JOptionPane.showMessageDialog(null,"Submitted.");
                    App.showMainApp();
                    frame.dispose();
                }
            }
        });
        removeRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                coauthorsTableModel.removeRow(coauthorsTableModel.getRowCount() - 1);
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
        JTextField[] fields = {articleTitleField, emailField, forenamesField, passwordField, surnameField, targetIssnTextField, titleField, universityAffiliationField};

        // set everything to white (if previously was red)
        for(JTextField field : fields) {
            field.setBackground(Color.white);
        }
        abstractTextArea.setBackground(Color.white);

        // check fields for forbidden characters
        for(JTextField field : fields) {
            if(! Util.checkForbiddenCharacters(field.getText())) {
                field.setBackground(Color.red);
                JOptionPane.showMessageDialog(null,"Characters ; : / \\ are forbidden.");
                return false;
            }

            if(field.getText().equals("")) {
                field.setBackground(Color.red);
                JOptionPane.showMessageDialog(null,"Fill out all the fields.");
                return false;
            }
        }

        // verify email
        if(! Util.verifyEmail(emailField.getText())) {
            emailField.setBackground(Color.red);
            JOptionPane.showMessageDialog(null,"Incorrect email.");
            return false;
        }

        // check if table full and doesn't contain forbidden characters
        if(! Util.verifyTable(coauthorsTable))
            return false;

        // check emails in the table
        if(! Util.verifyEmailInTable(coauthorsTable, 0))
            return false;

        if(pdfFile == null) {
            JOptionPane.showMessageDialog(null,"Choose a PDF file.");
            return false;
        }

        if(abstractTextArea.getText().equals("")) {
            abstractTextArea.setBackground(Color.red);
            JOptionPane.showMessageDialog(null,"Fill out the abstract.");
            return false;
        }

        // check if issn exisits
        if(! Util.issnExists(targetIssnTextField.getText())) {
            targetIssnTextField.setBackground(Color.red);
            JOptionPane.showMessageDialog(null,"No journal with such ISSN.");
            return false;
        }

        return true;
    }

    private void createUIComponents() {
        coauthorsTable = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return true;
            };
        };
    }

    public static void main(String[] args) {
        showAddSubmissionView();
    }
}
