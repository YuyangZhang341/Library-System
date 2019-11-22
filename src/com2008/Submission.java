package com2008;

public class Submission {
    private int submissionId;
    private String title;
    private String abs;
    private String pdfLink;
    private String mainAuthorsEmail;

    public Submission(int submissionId, String title, String abs, String pdfLink, String mainAuthorsEmail) {
        this.submissionId = submissionId;
        this.title = title;
        this.abs = abs;
        this.pdfLink = pdfLink;
        this.mainAuthorsEmail = mainAuthorsEmail;
    }

    public int getSubmissionId() {
        return submissionId;
    }

    public String getTitle() {
        return title;
    }

    public String getAbs() {
        return abs;
    }

    public String getPdfLink() {
        return pdfLink;
    }

    public String getMainAuthorsEmail() {
        return mainAuthorsEmail;
    }

    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAbs(String abs) {
        this.abs = abs;
    }

    public void setPdfLink(String pdfLink) {
        this.pdfLink = pdfLink;
    }

    public void setMainAuthorsEmail(String mainAuthorsEmail) {
        this.mainAuthorsEmail = mainAuthorsEmail;
    }
}
