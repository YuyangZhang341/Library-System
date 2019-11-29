package com2008;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class ReviewArticles {
    private JPanel main;
    private JPanel left;
    private JPanel right;
    private JPanel middle;
    private JPanel title;
    private JPanel select;
    private JPanel delete;
    private JPanel confirm;

    private JLabel titleLable;
    private JButton select2;
    private JButton delete2;
    private JButton confirm2;
    private JButton back;
    private JScrollPane leftJournalsScrollPane;
    private JScrollPane rightJournalsScrollPane;
    private JTable leftTable;
    private JTable rightTable;
    private JButton backButton;
    boolean a = false;
    private static JFrame frame = new JFrame("Review");

    public ReviewArticles() {
        loadLeftTable();
        loadRightTable();

/*
        select2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                frame.dispose();
            }
        });

        delete2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                App.showMainApp();
                frame.dispose();
            }
        });

        confirm2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                App.showMainApp();
                frame.dispose();
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                App.showMainApp();
                frame.dispose();
            }
        });
*/
    }

    private void loadRightTable() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ISSN", "Name"}, 0);
        rightTable.setModel(model);

        for (Journal journal : PublicationsController.getJournals()) {
            model.addRow(new Object[]{journal.getIssn(), journal.getName()});
        }
    }

    private void loadLeftTable() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ISSN", "Name"}, 0);
        leftTable.setModel(model);

        for (SelectedArticles selectedArticles : PublicationsController.getSelectedArticles()) {
            model.addRow(new Object[]{selectedArticles.getIssn(), selectedArticles.getName()});
        }
    }

    public static void showReviewArticle() {
        frame.setContentPane(new ReviewArticles().main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenDimensions = toolkit.getScreenSize();
        frame.setSize(screenDimensions.width, screenDimensions.height);
        frame.setVisible(true);
    }

    private void createComponents(){
        leftTable = new JTable(){
          public boolean isCellEditable(int lCow, int lColum){
              return false;
          } ;
        };

        rightTable = new JTable(){
            public boolean isRightCellEditable(int rRow, int rColum){
                return false;
            }
        };
    }

}
