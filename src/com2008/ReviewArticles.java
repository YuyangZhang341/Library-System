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
        DefaultTableModel model = new DefaultTableModel(new String[]{"Titlt", "Abstract"}, 0);
        rightTable.setModel(model);

        for (Submission submission : PublicationsController.getSubmissions()) {
            model.addRow(new Object[]{submission.getTitle(), submission.getAbs()});
        }
    }

    private void loadLeftTable() {}

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

                //get selected row
                int rightIndex = rightTable.getSelectedRow();
                //get total number of columns
                int rightColumn = rightTable.getColumnCount();
                //Create a saved collection
                Vector<Vector<Object>> vector =new Vector<Vector<Object>>();
                //Create another collection to join to the first collection
                Vector<Object> object =new Vector<Object>();
                //Pull all data from the index row into the second collection
                for(int n= 0; n < rightColumn; n ++){
                    object.add(rightTable.getValueAt(rightIndex,n));
                }
                //Adds data from the second collection to the first collection
                vector.add(object);
                //Create a collection for creating column names
                Vector<Object> object1 = new Vector<Object>();
                //get model from leftTable
                DefaultTableModel model = (DefaultTableModel)leftTable.getModel();
                //Add the template to table2
                model.setDataVector(vector, object1);


                JTable rightTable =(JTable) mouseEvent.getSource();
                Point rightPoint = mouseEvent.getPoint();
                int rightRow = rightTable.rowAtPoint(rightPoint);
                if (mouseEvent.getClickCount() == 2 &&
                        rightTable.getSelectedRow() != -1 &&
                        leftTable.getRowCount() <= 3) {
                    leftTable.setModel(model);
                }
            }
        });
    }
}
