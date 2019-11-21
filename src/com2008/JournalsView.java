package com2008;

import javax.swing.*;
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
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new App().main(new String[0]);
                frame.dispose();
            }
        });

        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showJournalsView();
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

    private void createUIComponents() {
        // disable editing cells in the table
        journalsTable = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };

        // fill the table with data
        PublicationsController.fetchJournals(journalsTable);

        // add listeners for enter press and for double click
        journalsTable.setSurrendersFocusOnKeystroke(true); //make it work for the first press as well
        journalsTable.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                showSelectedJournal();
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

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