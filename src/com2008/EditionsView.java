package com2008;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenDimensions = toolkit.getScreenSize();
        frame.setSize(screenDimensions.width, screenDimensions.height);
        frame.setVisible(true);
    }

    private void showSelectedEdition() {
        int targetNumber = Integer.parseInt(editionsTable.getValueAt(editionsTable.getSelectedRow(), 1).toString());

        ArticlesView.showArticlesView(issn, vol, targetNumber);
        frame.dispose();
    }

    private void createUIComponents() {
        // disable editing cells in the table
        editionsTable = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };

        // fill the table with data
        PublicationsController.fetchEditions(editionsTable, issn, vol);

        // add listeners for enter press and for double click
        editionsTable.setSurrendersFocusOnKeystroke(true); //make it work for the first press as well
        editionsTable.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                showSelectedEdition();
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

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
