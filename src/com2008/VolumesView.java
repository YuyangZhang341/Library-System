package com2008;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VolumesView {
    private JPanel volumesPanel;
    private JButton backButton;
    private JButton openButton;
    private JTable volumesTable;
    private JScrollPane volumesScrollPane;

    private String issn;

    //TODO: set frame title to journal's name
    private static JFrame frame = new JFrame("Volumes");

    public VolumesView(String issn) {
        this.issn = issn;

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JournalsView.showJournalsView();

                frame.dispose();
            }
        });

        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSelectedVolume();
            }
        });
    }

    public static void showVolumesView(String issn) {
        frame.setContentPane(new VolumesView(issn).volumesPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenDimensions = toolkit.getScreenSize();

        frame.setSize(screenDimensions.width, screenDimensions.height);
        frame.setVisible(true);
    }

    private void showSelectedVolume() {
        int targetVol = Integer.parseInt(volumesTable.getValueAt(volumesTable.getSelectedRow(), 0).toString());

        EditionsView.showEditionsView(issn, targetVol);
        frame.dispose();
    }

    private void createUIComponents() {
        // disable editing cells in the table
        volumesTable = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };

        // fill the table with data
        PublicationsController.fetchVolumes(volumesTable, issn);

        // add listeners for enter press and for double click
        volumesTable.setSurrendersFocusOnKeystroke(true); //make it work for the first press as well
        volumesTable.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                showSelectedVolume();
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        volumesTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    showSelectedVolume();
                }
            }
        });
    }
}
