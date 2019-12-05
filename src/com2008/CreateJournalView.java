package com2008;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateJournalView {
    private JPanel mainPanel;
    private JPanel backPanel;
    private JButton backButton;
    private JButton createButton;
    private JScrollPane contentScrollPane;
    private JPanel scrollPanel;
    private JPanel detailsPanel;
    private JPanel chiefEditorPanel;
    private JPanel journalPanel;
    private JLabel chiefEditorLabel;
    private JLabel emailLabel;
    private JTextField emailField;
    private JTextField titleField;
    private JTextField forenamesField;
    private JTextField surnameField;
    private JTextField universityAffiliationField;
    private JPasswordField passwordField;
    private JLabel titleLabel;
    private JLabel forenamesLabel;
    private JLabel surnameLabel;
    private JLabel universityAffiliationLabel;
    private JLabel passwordLabel;
    private JTable editorsTable;
    private JButton addRowButton;
    private JScrollPane editorsScrollPane;
    private JLabel journalTitleLabel;
    private JTextField journalTitleField;
    private JTextField journalIssnField;
    private JLabel journalIssnLabel;
    private JButton removeRowButton;
    private DefaultTableModel editorsTableModel = new DefaultTableModel(new String[]{"Email", "Title", "Forenames", "Surname", "University Affiliation", "Password"}, 0);

    private static JFrame frame = new JFrame("Create Journal");

    public CreateJournalView() {
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new App().main(new String[0]);
                frame.dispose();
            }
        });

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent e){
                if (verifyFields()) {
                    Editor[] editors = new Editor[editorsTable.getRowCount() + 1];
                    editors[0] = new Editor(titleField.getText(), forenamesField.getText(), surnameField.getText(),
                            universityAffiliationField.getText(), emailField.getText(), String.valueOf(passwordField.getPassword()), true, journalIssnField.getText());

                    for (int i = 0; i < editorsTable.getRowCount(); i++) {
                        editors[i + 1] = new Editor(editorsTable.getValueAt(i, 1).toString(),
                                editorsTable.getValueAt(i, 2).toString(),
                                editorsTable.getValueAt(i, 3).toString(),
                                editorsTable.getValueAt(i, 4).toString(),
                                editorsTable.getValueAt(i, 0).toString(),
                                editorsTable.getValueAt(i, 5).toString(),
                                false,
                                journalIssnField.getText());
                    }

                    Journal journal = new Journal(journalIssnField.getText(), journalTitleField.getText(), emailField.getText());

                    PublicationsController.createJournal(journal, editors);

                    JOptionPane.showMessageDialog(null, "Submitted.");
                    App.showMainApp();
                    frame.dispose();
                }
            }
        });

        addRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorsTable.setModel(editorsTableModel);
                editorsTableModel.addRow(new Object[]{"", "", "", "", ""});
            }
        });

        journalIssnField.setText(PublicationsController.generateIssn());

        removeRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(editorsTableModel.getRowCount() > 0 ) {
                    editorsTableModel.removeRow(editorsTableModel.getRowCount() - 1);
                }
            }
        });
    }

    public static void showCreateJournalView() {
        frame.setContentPane(new CreateJournalView().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(Util.WIDTH, Util.HEIGHT);
        frame.setVisible(true);
    }

    public boolean verifyFields() {
        JTextField[] fields = {forenamesField, emailField, journalIssnField, journalTitleField, passwordField, surnameField, titleField, universityAffiliationField};

        // set everything to white (if previously was red)
        for(JTextField field : fields) {
            field.setBackground(Color.white);
        }

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
        if(! Util.verifyTable(editorsTable))
            return false;

        // check emails in the table
        if(! Util.verifyEmailInTable(editorsTable, 0))
            return false;

        return true;
    }


    private void createUIComponents() {
        editorsTable = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return true;
            };
        };
    }

    public static void main(String[] args) {
        showCreateJournalView();
    }
}
