package com2008;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditionsView {
    private JPanel backPanel;
    private JScrollPane editionsScrollPane;
    private JTable editionsTable;
    private JPanel editionsPanel;
    private JButton backButton;
    private JButton openButton;

    private String issn;
    private int vol;

    //TODO: set frame title to be the current journal's names with edition number
    private static JFrame frame = new JFrame("Editions");

    public EditionsView(String issn, int vol) {
        this.issn = issn;
        this.vol = vol;

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VolumesView().showVolumesView();
                frame.dispose();
            }
        });

        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                String targetIssn = editionsTable.getValueAt(editionsTable.getSelectedRow(), 0).toString();
            }
        });
    }

    public static void showEditionsView() {
        frame.setContentPane(new EditionsView().editionsPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenDimensions = toolkit.getScreenSize();
        frame.setSize(screenDimensions.width, screenDimensions.height);
        frame.setVisible(true);
    }


    private void createUIComponents() {
        editionsTable = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };

        PublicationsController.fetchEditions(editionsTable);
    }
}
