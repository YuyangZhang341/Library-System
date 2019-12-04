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
    private JButton confirm2;
    private JButton back;
    private JScrollPane leftJournalsScrollPane;
    private JScrollPane rightJournalsScrollPane;
    private JTable leftTable;
    private JTable rightTable;
    private static JFrame frame = new JFrame("Review");

    public ReviewArticles(){
        loadRightTable();
        loadLeftTable();

        confirm2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                App.showMainApp();
                frame.dispose();
            }
        });
    }

    public static void showReviewArticles(){
        frame.setContentPane(new ReviewArticles().main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenDimension = toolkit.getScreenSize();
        frame.setSize(screenDimension.width, screenDimension.height);
        frame.setVisible(true);
    }

    private void loadRightTable() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Title", "Abstract"}, 0);
        rightTable.setModel(model);

        for (Submission submission : PublicationsController.getSubmissions()) {
            model.addRow(new Object[]{submission.getTitle(), submission.getAbs()});
        }
    }

    private void loadLeftTable(){
        DefaultTableModel model = new DefaultTableModel(new String[]{"Title", "Abstract"}, 0);
        rightTable.setModel(model);
    }

    private void createUIComponents(){

        rightTable = new JTable(){
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        rightTable = new JTable(){
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

    }
}


