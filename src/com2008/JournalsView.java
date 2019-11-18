package com2008;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JournalsView {
    private JTable journalsTable;
    private JPanel journalsPanel;
    private JScrollPane journalsScrollPane;
    private JButton backButton;
    private JPanel backPanel;

    private static JFrame frame = new JFrame("Journals");

    public JournalsView() {
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new App().main(new String[0]);
                frame.dispose();
            }
        });
    }

    public JFrame getFrame() {
        frame.setContentPane(new JournalsView().journalsPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        return frame;
    }

    private void createUIComponents() {
        // Initializing the JTable
        journalsTable = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };
        ViewArticles.fetchJournals(journalsTable);
    }
}