package com2008;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class VolumesView {
    private JPanel volumesPanel;
    private JButton backButton;
    private JButton openButton;
    private JTable volumesTable;
    private JScrollPane volumesScrollPane;
    private JButton homeButton;

    private String issn;

    //TODO: set frame title to journal's name
    private static JFrame frame = new JFrame("Volumes");

    public VolumesView(String issn) {
        this.issn = issn;
        loadVolumesTable();

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                App.showMainApp();
                frame.dispose();
            }
        });

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
        int targetVol = Integer.parseInt(volumesTable.getValueAt(volumesTable.getSelectedRow(), 1).toString());

        EditionsView.showEditionsView(issn, targetVol);
        frame.dispose();
    }

    private void loadVolumesTable() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ISSN", "Vol", "Year"}, 0);
        volumesTable.setModel(model);

        for(Volume volume : PublicationsController.getVolumes(issn)) {
            model.addRow(new Object[]{volume.getIssn(),volume.getVol(),volume.getYear()});
        }
    }

    private void createUIComponents() {
        // disable editing cells in the table
        volumesTable = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };

        // add a listener for double click
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
