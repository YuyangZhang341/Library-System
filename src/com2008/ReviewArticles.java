package com2008;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

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

    public ReviewArticles() {
        //loadLeftTable();
        loadRightTable();

        confirm2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                App.showMainApp();
                frame.dispose();
            }
        });

    }

    private void selectedId(){

    }

    private void loadRightTable() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"submissionID", "Titlt", "Abstract"}, 0);
        rightTable.setModel(model);

        for (Submission submission : PublicationsController.getSubmissions()) {
            model.addRow(new Object[]{submission.getSubmissionId(), submission.getTitle(), submission.getAbs()});
        }
    }

    private void loadLeftTable() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"submissionID", "Titlt", "Abstract"}, 0);
        leftTable.setModel(model);
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

    private void createUIComponents() {
        //disable editing cells in the table
        leftTable = new JTable() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        rightTable = new JTable() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        leftTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable leftTable =(JTable) mouseEvent.getSource();
                Point leftPoint = mouseEvent.getPoint();
                int leftRow = leftTable.rowAtPoint(leftPoint);
                int leftRowCount = leftTable.getRowCount();
                int selectedRow = leftTable.getSelectedRow();
                if (mouseEvent.getClickCount() == 2 &&
                        selectedRow != -1 &&
                        leftRowCount <= 3) {
                }else if (mouseEvent.getClickCount() == 3 &&
                        selectedRow != -1 &&
                        leftRow <= 3){
                    leftTable.remove(selectedRow);
                }
            }
        });

        rightTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {

                JTable rightTable =(JTable) mouseEvent.getSource();
                Point rightPoint = mouseEvent.getPoint();
                int rightRow = rightTable.rowAtPoint(rightPoint);
                if (mouseEvent.getClickCount() == 2 &&
                        rightTable.getSelectedRow() != -1 &&
                        leftTable.getRowCount() <= 3) {
                    JournalsView.showJournalsView();
                }else if(mouseEvent.getClickCount() == 3 &&
                        rightTable.getSelectedRow() != -1 &&
                        leftTable.getRowCount() <= 3){
                    int id = (int) rightTable.getValueAt(rightTable.getSelectedRow(), 1);

                    DefaultTableModel model = new DefaultTableModel(new String[]{"submissionID", "Titlt", "Abstract"}, 0);
                    leftTable.setModel(model);


                }
            }
        });
    }
}
