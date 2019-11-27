package com2008;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AuthorView {
    private JPanel mainPanel;
    private JTabbedPane submissionPane;
    private JTabbedPane reviewPane;
    private JTextField titleField;
    private JTextField authorsField;
    private JLabel titleLabel;
    private JTextArea abstractArea;
    private JTextArea abstractArea2;
    private JTextField authorsField2;
    private JTextField titleField2;
    private JLabel abstractLabel2;
    private JLabel abstractLabel;
    private JLabel authorsLabel2;
    private JLabel titleLabel2;
    private JPanel review1Panel;
    private JPanel submissionPanel;
    private JPanel revisedSubmissionPanel;
    private JLabel authorsLabel;
    private JScrollPane summaryScrollPane;
    private JScrollPane errorsScrollPane;
    private JPanel verdictsPanel;
    private JScrollPane criticismsScrollPane;
    private JTable criticismsTable;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JTextField initialVerdictField;
    private JTextField finalVerdictField;
    private JLabel initialVerdictLabel;
    private JLabel finalVerdictLabel;
    private JPanel review2Panel;
    private JPanel review3Panel;
    private JScrollPane summaryScrollPane2;
    private JTextArea summaryArea2;
    private JScrollPane errorsScrollPane2;
    private JTextArea errorsArea2;
    private JPanel verdictsPanel2;
    private JTextField initialVerdictField2;
    private JTextField finalVerdictField2;
    private JLabel initialVerdictLabel2;
    private JLabel finalVerdictLabel2;
    private JScrollPane criticismsScrollPane2;
    private JTable criticismsTable2;
    private JScrollPane summaryScrollPane3;
    private JTextArea summaryArea3;
    private JScrollPane errorsScrollPane3;
    private JTextArea errorsArea3;
    private JPanel verdictsPanel3;
    private JTextField initialVerdictField3;
    private JTextField finalVerdictField3;
    private JLabel initialVerdictLabel3;
    private JLabel finalVerdictLabel3;
    private JScrollPane criticismsScrollPane3;
    private JTable criticismsTable3;
    private DefaultTableModel criticismsTableModel = new DefaultTableModel(new String[]{"Criticism"}, 0);
    private DefaultTableModel criticismsTable2Model = new DefaultTableModel(new String[]{"Criticism"}, 0);
    private DefaultTableModel criticismsTable3Model = new DefaultTableModel(new String[]{"Criticism"}, 0);

    private int submissionId;

    private static JFrame frame = new JFrame("Submission View");

    public AuthorView(int submissionId) {
        this.submissionId = submissionId;

        loadCriticismsTable(submissionId);
    }

    public static void showAuthorView(int submissionId) {
        frame.setContentPane(new AuthorView(submissionId).mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenDimensions = toolkit.getScreenSize();
        frame.setSize(screenDimensions.width, screenDimensions.height);
        frame.setVisible(true);
    }

    private void createUIComponents() {
        criticismsTable = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };

        criticismsTable2 = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };

        criticismsTable3 = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };
    }

    private void loadCriticismsTable(int submissionId) {
        criticismsTable.setModel(criticismsTableModel);
        criticismsTable2.setModel(criticismsTable2Model);
        criticismsTable3.setModel(criticismsTable3Model);

        for(Criticism criticism: PublicationsController.getCriticisms(submissionId,1)) {
            criticismsTableModel.addRow(new Object[]{criticism.getCriticism()});
        }
        for(Criticism criticism: PublicationsController.getCriticisms(submissionId,2)) {
            criticismsTable2Model.addRow(new Object[]{criticism.getCriticism()});
        }
        for(Criticism criticism: PublicationsController.getCriticisms(submissionId,3)) {
            criticismsTable3Model.addRow(new Object[]{criticism.getCriticism()});
        }
    }

    public static void main(String[] args) {
        showAuthorView(1);
    }
}
