package com2008;

import javax.swing.*;

public class JournalsView {
    private JTable journalsTable;
    private JPanel journalsPanel;
    private JScrollPane journalsScrollPane;

    public JournalsView() {
        // Data to be displayed in the JTable
        String[][] data = {
                { "Kundan Kumar Jha", "4031", "CSE" },
                { "Anand Jha", "6014", "IT" }
        };

        // Column Names
        String[] columnNames = { "Name", "Roll Number", "Department" };

        // Initializing the JTable
        journalsTable = new JTable(data, columnNames);
        journalsTable.setBounds(30, 40, 200, 300);

        // adding it to JScrollPane
        journalsScrollPane = new JScrollPane(journalsTable);
    }

    public JFrame getFrame() {
        JFrame frame = new JFrame("Journals");
        frame.setContentPane(new JournalsView().journalsScrollPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        return frame;
    }
}