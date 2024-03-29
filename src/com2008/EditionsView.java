package com2008;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class EditionsView {
    private JPanel backPanel;
    private JScrollPane editionsScrollPane;
    private JTable editionsTable;
    private JPanel editionsPanel;
    private JButton backButton;
    private JButton openButton;
    private JButton homeButton;

    private String issn;
    private int vol;

    private static JFrame frame = new JFrame("Editions");

    public EditionsView(String issn, int vol) {
        this.issn = issn;
        this.vol = vol;

        loadEditionsTable();

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
                VolumesView.showVolumesView(issn);
                frame.dispose();
            }
        });

        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSelectedEdition();
            }
        });
    }

    public static void showEditionsView(String issn, int vol) {
        frame.setContentPane(new EditionsView(issn, vol).editionsPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(Util.WIDTH, Util.HEIGHT);
        frame.setVisible(true);
    }

    private void showSelectedEdition() {
        int targetNumber = Integer.parseInt(editionsTable.getValueAt(editionsTable.getSelectedRow(), 2).toString());

        ArticlesView.showArticlesView(issn, vol, targetNumber);
        frame.dispose();
    }

    private void loadEditionsTable() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ISSN", "Vol", "Number"}, 0);
        editionsTable.setModel(model);

        for(Edition edition : PublicationsController.getEditions(issn,vol)) {
            model.addRow(new Object[]{edition.getIssn(),edition.getVol(),edition.getNumber()});
        }
    }

    private void createUIComponents() {
        // disable editing cells in the table
        editionsTable = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };

        // add a listener for double click
        editionsTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    showSelectedEdition();
                }
            }
        });
    }
}
