package com2008;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

public class AddEditorsDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton addRowButton;
    private JTable editorsTable;
    private JScrollPane editorsScrollPane;
    private JButton removeRowButton;

    static AddEditorsDialog d;

    private DefaultTableModel editorsTableModel = new DefaultTableModel(new String[]{"Email", "Title", "Forenames", "Surname", "University Affiliation", "Password"}, 0);
    private String journalIssn;

    public AddEditorsDialog(String journalIssn) {
        this.journalIssn = journalIssn;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        addRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorsTable.setModel(editorsTableModel);
                editorsTableModel.addRow(new Object[]{"", "", "", "", ""});
            }
        });

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
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

        removeRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorsTableModel.removeRow(editorsTableModel.getRowCount() - 1);
            }
        });
    }

    private void onOK() {
        if (editorsTable.getRowCount() == 0) {
            dispose();
        } else if (verifyFields()) {
            Editor[] editors = new Editor[editorsTable.getRowCount()];

            for (int i = 0; i < editorsTable.getRowCount(); i++) {
                editors[i] = new Editor(editorsTable.getValueAt(i, 1).toString(),
                        editorsTable.getValueAt(i, 2).toString(),
                        editorsTable.getValueAt(i, 3).toString(),
                        editorsTable.getValueAt(i, 4).toString(),
                        editorsTable.getValueAt(i, 0).toString(),
                        editorsTable.getValueAt(i, 5).toString(),
                        false,
                        journalIssn);


                PublicationsController.addUser(
                        editors[i].getEmail(),
                        editors[i].getTitle(),
                        editors[i].getForenames(),
                        editors[i].getSurname(),
                        editors[i].getUniversityAffiliation(),
                        editors[i].getPassword());

                PublicationsController.addEditor(
                        journalIssn,
                        editors[i].getEmail()
                );
            }
            JOptionPane.showMessageDialog(d, "Editors Added.");
            dispose();
        }
    }

    public boolean verifyFields() {
        // check if table full and doesn't contain forbidden characters
        if(! Util.verifyTable(editorsTable, d))
            return false;

        // check emails in the table
        if(! Util.verifyEmailInTable(editorsTable, 0))
            return false;

        return true;
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void showAddEditorsDialog(String journalIssn) {
        d = new AddEditorsDialog(journalIssn);
        d.pack();
        d.setSize(400,300);
        d.setLocationRelativeTo(null);
        d.setVisible(true);
    }

    public static void main(String[] args) {
        showAddEditorsDialog("1234-4321");
    }
}
