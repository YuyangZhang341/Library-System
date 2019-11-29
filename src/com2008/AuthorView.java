package com2008;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AuthorView {
    private JPanel mainPanel;
    private JTabbedPane submissionPane;
    private JTabbedPane reviewPane;
    private JTextField titleField;
    private JTextField authorsField;
    private JLabel titleLabel;
    private JTextArea abstractArea;
    private JTextArea abstractArea2;
    private JTextField authorsField2;
    private JTextField titleField2;
    private JLabel abstractLabel2;
    private JLabel abstractLabel;
    private JLabel authorsLabel2;
    private JLabel titleLabel2;
    private JPanel review1Panel;
    private JPanel submissionPanel;
    private JPanel revisedSubmissionPanel;
    private JLabel authorsLabel;
    private JScrollPane summaryScrollPane;
    private JScrollPane errorsScrollPane;
    private JPanel verdictsPanel;
    private JScrollPane criticismsScrollPane;
    private JTable criticismsTable;
    private JTextArea summaryArea;
    private JTextArea errorsArea;
    private JTextField initialVerdictField;
    private JTextField finalVerdictField;
    private JLabel initialVerdictLabel;
    private JLabel finalVerdictLabel;
    private JPanel review2Panel;
    private JPanel review3Panel;
    private JScrollPane summaryScrollPane2;
    private JTextArea summaryArea2;
    private JScrollPane errorsScrollPane2;
    private JTextArea errorsArea2;
    private JPanel verdictsPanel2;
    private JTextField initialVerdictField2;
    private JTextField finalVerdictField2;
    private JLabel initialVerdictLabel2;
    private JLabel finalVerdictLabel2;
    private JScrollPane criticismsScrollPane2;
    private JTable criticismsTable2;
    private JScrollPane summaryScrollPane3;
    private JTextArea summaryArea3;
    private JScrollPane errorsScrollPane3;
    private JTextArea errorsArea3;
    private JPanel verdictsPanel3;
    private JTextField initialVerdictField3;
    private JTextField finalVerdictField3;
    private JLabel initialVerdictLabel3;
    private JLabel finalVerdictLabel3;
    private JScrollPane criticismsScrollPane3;
    private JTable criticismsTable3;
    private JButton pdfButton;
    private JButton pdfButton2;
    private JButton logoutButton;
    private JButton reviseButton;

    private int submissionId;
    private String userEmail;


    private static JFrame frame = new JFrame("Submission View");

    public AuthorView(int submissionId, String userEmail) {
        this.submissionId = submissionId;
        this.userEmail = userEmail;

        Submission submission = PublicationsController.getSubmission(submissionId);
        String title = submission.getTitle();
        Author[] authors = PublicationsController.getArticleAuthors(submissionId);
        String abs = submission.getAbs();

        titleField.setText(title);
        String authorsFieldText = "";
        for (Author a : authors) {
            authorsFieldText += a.getForenames() + " " + a.getSurname() + " ";
        }
        authorsField.setText(authorsFieldText);
        abstractArea.setText(abs);

        RevisedSubmission revisedSubmission = PublicationsController.getRevisedSubmission(submissionId);

        if (revisedSubmission!=null) {
            String revisedTitle = revisedSubmission.getTitle();
            String revisedAbs = revisedSubmission.getAbs();

            titleField2.setText(revisedTitle);
            authorsField2.setText(authorsFieldText);
            abstractArea2.setText(revisedAbs);

        } else {
            submissionPane.remove(revisedSubmissionPanel);
        }

        Review[] reviews = PublicationsController.getReviews(submissionId);

        if (reviews.length==0) {
            reviewPane.remove(review1Panel);
            reviewPane.remove(review2Panel);
            reviewPane.remove(review3Panel);


        } else if (reviews.length==1) {
            reviewPane.remove(review2Panel);
            reviewPane.remove(review3Panel);

            summaryArea.setText(reviews[0].getSummary());
            errorsArea.setText(reviews[0].getTypographicalErrors());
            initialVerdictField.setText(reviews[0].getInitialVerdict());
            finalVerdictField.setText(reviews[0].getFinalVerdict());
        } else if (reviews.length==2) {
            reviewPane.remove(review3Panel);

            summaryArea.setText(reviews[0].getSummary());
            errorsArea.setText(reviews[0].getTypographicalErrors());
            initialVerdictField.setText(reviews[0].getInitialVerdict());
            finalVerdictField.setText(reviews[0].getFinalVerdict());

            summaryArea2.setText(reviews[1].getSummary());
            errorsArea2.setText(reviews[1].getTypographicalErrors());
            initialVerdictField2.setText(reviews[1].getInitialVerdict());
            finalVerdictField2.setText(reviews[1].getFinalVerdict());
        } else if (reviews.length==3) {
            summaryArea.setText(reviews[0].getSummary());
            errorsArea.setText(reviews[0].getTypographicalErrors());
            initialVerdictField.setText(reviews[0].getInitialVerdict());
            finalVerdictField.setText(reviews[0].getFinalVerdict());

            summaryArea2.setText(reviews[1].getSummary());
            errorsArea2.setText(reviews[1].getTypographicalErrors());
            initialVerdictField2.setText(reviews[1].getInitialVerdict());
            finalVerdictField2.setText(reviews[1].getFinalVerdict());

            summaryArea3.setText(reviews[2].getSummary());
            errorsArea3.setText(reviews[2].getTypographicalErrors());
            initialVerdictField3.setText(reviews[2].getInitialVerdict());
            finalVerdictField3.setText(reviews[2].getFinalVerdict());
        }

        //if no revisedSubmission, the user is the main author and the number of reviews = 3 -> make it possible to submitRevised version
        String mainAuthorsEmail = submission.getMainAuthorsEmail();

        if (revisedSubmission==null && mainAuthorsEmail.equals(userEmail) && reviews.length==3) {
            reviseButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ReviseArticleView.showReviseArticleView(submissionId, userEmail);
                }
            });
        } else {
            mainPanel.remove(reviseButton);
        }

        loadCriticismsTable(submissionId);

        pdfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(submission.getPdf().exists()) {
                        if(Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().open(submission.getPdf());
                        } else {
                            System.out.println("Awt Desktop not supported.");
                        }
                    } else {
                        System.out.println("File doesn't exist.");
                    }
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        pdfButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(revisedSubmission.getPdf().exists()) {
                        if(Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().open(revisedSubmission.getPdf());
                        } else {
                            System.out.println("Awt Desktop not supported.");
                        }
                    } else {
                        System.out.println("File doesn't exist.");
                    }
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                App.showMainApp();
                frame.dispose();
            }
        });
    }

    public static void showAuthorView(int submissionId, String userEmail) {
        frame.setContentPane(new AuthorView(submissionId, userEmail).mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenDimensions = toolkit.getScreenSize();
        frame.setSize(screenDimensions.width, screenDimensions.height);
        frame.setVisible(true);
    }

    private void createUIComponents() {
        criticismsTable = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };

        criticismsTable2 = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };

        criticismsTable3 = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };
    }

    private void loadCriticismsTable(int submissionId) {
        DefaultTableModel criticismsTableModel = new DefaultTableModel(new String[]{"Criticism", "Response"}, 0);;
        DefaultTableModel criticismsTable2Model = new DefaultTableModel(new String[]{"Criticism", "Response"}, 0);;
        DefaultTableModel criticismsTable3Model = new DefaultTableModel(new String[]{"Criticism", "Response"}, 0);;

        for(Criticism criticism: PublicationsController.getCriticisms(submissionId,1)) {
            if(criticism.getResponse() != null) {
                criticismsTableModel.addRow(new Object[]{criticism.getCriticism(), criticism.getResponse()});
            }
            else {
                criticismsTableModel.addRow(new Object[]{criticism.getCriticism(), ""});
            }
        }

        for(Criticism criticism: PublicationsController.getCriticisms(submissionId,2)) {
            if(criticism.getResponse() != null) {
                criticismsTable2Model.addRow(new Object[]{criticism.getCriticism(), criticism.getResponse()});
            }
            else {
                criticismsTableModel.addRow(new Object[]{criticism.getCriticism(), ""});
            }
        }

        for(Criticism criticism: PublicationsController.getCriticisms(submissionId,3)) {
            if(criticism.getResponse() != null) {
                criticismsTable3Model.addRow(new Object[]{criticism.getCriticism(), criticism.getResponse()});
            }
            else {
                criticismsTableModel.addRow(new Object[]{criticism.getCriticism(), ""});
            }
        }

        criticismsTable.setModel(criticismsTableModel);
        criticismsTable2.setModel(criticismsTable2Model);
        criticismsTable3.setModel(criticismsTable3Model);
    }

    public static void main(String[] args) {
        showAuthorView(15, "pdf@op.pl");
    }
}
