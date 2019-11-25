package com2008;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class ChangeMainEditorDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable editorsTable;

    private String journalIssn;
    private String userEmail;

    static ChangeMainEditorDialog d;

    public ChangeMainEditorDialog(String journalIssn, String userEmail) {
        this.journalIssn = journalIssn;
        this.userEmail = userEmail;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        loadEditorsTable(journalIssn);

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
    }

    private void onOK() {
        changeChiefEditor(journalIssn);
        dispose();
        ((Window)d.getParent()).dispose();
        EditorView.showEditorView(journalIssn, userEmail);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void loadEditorsTable(String journalIssn) {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Title", "Forenames", "Surname", "Email"}, 0);
        editorsTable.setModel(model);

        for(Editor editor: PublicationsController.getEditors(journalIssn)) {
            if(!userEmail.equals(editor.getEmail())) {
                model.addRow(new Object[]{editor.getTitle(), editor.getForenames(), editor.getSurname(), editor.getEmail()});
            }
        }
    }

    public static void showChangeMainEditorDialog(String journalIssn, String userEmail) {
        d = new ChangeMainEditorDialog(journalIssn, userEmail);
        d.pack();
        d.setSize(400,300);
        d.setLocationRelativeTo(null);
        d.setVisible(true);
    }

    private void changeChiefEditor(String journalIssn) {
        Statement stmt = null;

        try (Connection con = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team019", "team019", "fd0751c6")) {
            stmt = con.createStatement();
            stmt.executeUpdate("UPDATE journals SET chiefEditorEmail = '" +
                    editorsTable.getValueAt(editorsTable.getSelectedRow(), 3).toString() + "'" +
                    " WHERE issn = '" + journalIssn + "'");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createUIComponents() {
        editorsTable = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };
    }
}
