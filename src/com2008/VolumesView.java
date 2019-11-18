package com2008;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VolumesView {
    private JPanel volumesPanel;
    private JButton backButton;
    private JButton openButton;
    private JTable volumesTable;
    private JScrollPane volumesScrollPane;

    //TODO: set frame title to journal's name
    private static JFrame frame = new JFrame("Volumes");

    public VolumesView() {
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new JournalsView().showJournalsView();

                frame.dispose();
            }
        });

        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public void showVolumesView() {
        frame.setContentPane(new VolumesView().volumesPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenDimensions = toolkit.getScreenSize();

        frame.setSize(screenDimensions.width, screenDimensions.height);
        frame.setVisible(true);
    }

    private void createUIComponents() {
        volumesTable = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };

        //TODO:: populate the volumesTable with a function defined in PublicationsController
    }
}
