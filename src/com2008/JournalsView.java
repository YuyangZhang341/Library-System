package com2008;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class JournalsView {
    private JTable journalsTable;
    private JPanel journalsPanel;
    private JScrollPane journalsScrollPane;
    private JButton backButton;
    private JPanel backPanel;
    private JButton openButton;

    private static JFrame frame = new JFrame("Journals");

    public JournalsView() {
        loadJournalsTable();

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                App.showMainApp();
                frame.dispose();
            }
        });

        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSelectedJournal();
            }
        });
    }

    public static void showJournalsView() {
        frame.setContentPane(new JournalsView().journalsPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenDimensions = toolkit.getScreenSize();
        frame.setSize(screenDimensions.width, screenDimensions.height);
        frame.setVisible(true);
    }

    private void showSelectedJournal() {
        String targetIssn = journalsTable.getValueAt(journalsTable.getSelectedRow(), 0).toString();

        VolumesView.showVolumesView(targetIssn);
        frame.dispose();
    }

    private void loadJournalsTable() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ISSN", "Name"}, 0);
        journalsTable.setModel(model);

        for(Journal journal : PublicationsController.getJournals()) {
            model.addRow(new Object[]{journal.getIssn(), journal.getName()});
        }
    }

    private void createUIComponents() {
        // disable editing cells in the table
        journalsTable = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };

        // add a listener for double click
        journalsTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    showSelectedJournal();
                }
            }
        });
    }
}