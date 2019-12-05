package com2008;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
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
    private JButton selectButton;
    private JButton deleteButton;

    private JTextField leftSubmissionIDTextField;
    private JTextField leftTitleTextField;
    private JTextField leftAbsTextField;

    private static JFrame frame = new JFrame("Review");

    public ReviewArticles(){
        loadTable();

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


    private void loadTable() {
        DefaultTableModel leftModel = new DefaultTableModel(new String[]{"submissionID", "Title", "Abstract"}, 0);
        leftTable.setModel(leftModel);


        DefaultTableModel rightModel = new DefaultTableModel(new String[]{"submissionID", "Title", "Abstract"}, 0);
        rightTable.setModel(rightModel);

        for (Submission submission : PublicationsController.getSubmissions()) {
            rightModel.addRow(new Object[]{submission.getSubmissionId(), submission.getTitle(), submission.getAbs()});
        }

        rightTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  //Single select
        rightTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                selectButton.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        int selectedRow = rightTable.getSelectedRow();
                        int leftRows = leftTable.getRowCount();
                        int id = (int) rightTable.getValueAt(selectedRow, 0);
                        String title = (String) rightTable.getValueAt(selectedRow, 1);
                        String abs = (String) rightTable.getValueAt(selectedRow, 2);
                         if (selectedRow != -1 && leftRows < 3){
                            leftModel.addRow(new Object[]{id, title, abs});
                        }
                    }
                });
            }
        });

        leftTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        leftTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                deleteButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        int selectedRow = leftTable.getSelectedRow();
                        if (selectedRow != -1){
                            leftModel.removeRow(selectedRow);
                        }
                    }
                });
            }
        });
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

//        rightTable.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mousePressed(MouseEvent e) {
//                JTable rightTable = (JTable) e.getSource();
//                Point point = e.getPoint();
//                int row = rightTable.rowAtPoint(point);
//                int leftRows = leftTable.getRowCount();
//                if (e.getClickCount() == 2 && rightTable.getSelectedRow() != -1 && leftRows <3){
//                    int id = (int) rightTable.getValueAt(rightTable.getSelectedRow(),0);
//                   /* for (Submission submission : PublicationsController.getSubmissions()) {
//                        model.addRow(new Object[]{submission.getSubmissionId(), submission.getTitle(), submission.getAbs()});
//                    }*/
//                   System.out.println(id);
//                   System.out.println(PublicationsController.getSubmissionsByID(id));
//
//                }
//            }
//        });
    }
}


