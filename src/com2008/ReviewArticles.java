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
        DefaultTableModel rightModel = new DefaultTableModel(new String[]{"submissionID", "Title", "Abstract"}, 0);
        rightTable.setModel(rightModel);

        for (Submission submission : PublicationsController.getSubmissions()) {
            rightModel.addRow(new Object[]{submission.getSubmissionId(), submission.getTitle(), submission.getAbs()});
        }
    }

    private void loadLeftTable(){
        DefaultTableModel leftModel = new DefaultTableModel(new String[]{"submissionID", "Title", "Abstract"}, 0);
        leftTable.setModel(leftModel);


    }

    private void createUIComponents(){

        rightTable = new JTable(){
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        leftTable = new JTable(){
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        rightTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JTable rightTable = (JTable) e.getSource();
                Point point = e.getPoint();
                int row = rightTable.rowAtPoint(point);
                int leftRows = leftTable.getRowCount();
                if (e.getClickCount() == 2 && rightTable.getSelectedRow() != -1 && leftRows <3){
                    int id = (int) rightTable.getValueAt(rightTable.getSelectedRow(),0);
                   /* for (Submission submission : PublicationsController.getSubmissions()) {
                        model.addRow(new Object[]{submission.getSubmissionId(), submission.getTitle(), submission.getAbs()});
                    }*/
                   System.out.println(id);
                   System.out.println(PublicationsController.getSubmissionsByID(id));

                }
            }
        });
    }
}


