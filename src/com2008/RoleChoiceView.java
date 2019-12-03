package com2008;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class RoleChoiceView {
    private JButton backButton;
    private JButton chooseButton;
    private JTable rolesTable;
    private JPanel mainPanel;
    private JPanel navPanel;
    private JScrollPane rolesScrollPane;

    private String email;
    private static JFrame frame = new JFrame("Available roles");

    public RoleChoiceView(String email) {
        this.email = email;

        loadRolesTable();

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                App.showMainApp();
            }
        });

        chooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseRole();
            }
        });
    }

    public static void showRoleChoiceView(String userEmail) {
        frame.setContentPane(new RoleChoiceView(userEmail).mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenDimensions = toolkit.getScreenSize();
        frame.setSize(screenDimensions.width, screenDimensions.height);
        frame.setVisible(true);
    }

    private void loadRolesTable() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Role", "ISSN / SubmissionID", "Journal name / Article Title"}, 0);
        rolesTable.setModel(model);

        for(Role role: PublicationsController.getRoles(email)) {
            model.addRow(new Object[]{role.getRole(), role.getIssnOrSubmissionId(), role.getNameOrTitle()});
        }
    }

    private void chooseRole() {
        String role = rolesTable.getValueAt(rolesTable.getSelectedRow(), 0).toString();
        int submissionId;
        String issn;

        switch (role) {
            case "author":
                submissionId = Integer.parseInt(rolesTable.getValueAt(rolesTable.getSelectedRow(), 1).toString());
                AuthorView.showAuthorView(submissionId, email);
                break;
            case "editor":
            case "chief editor":
                issn = rolesTable.getValueAt(rolesTable.getSelectedRow(), 1).toString();
                EditorView.showEditorView(issn, email);
                break;
            case "reviewer":
                submissionId = Integer.parseInt(rolesTable.getValueAt(rolesTable.getSelectedRow(), 1).toString());
                ReviewerView.showReviewerView(submissionId, email);
                break;
        }

        frame.dispose();
    }

    private void createUIComponents() {
        // disable editing cells in the table
        rolesTable = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };

        // add a listener for double click
        rolesTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    chooseRole();
                }
            }
        });
    }

    public static void main(String[] args) {
        showRoleChoiceView("co@jest.kurde.123");
    }
}
