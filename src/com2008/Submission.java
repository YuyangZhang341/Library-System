package com2008;

public class Submission {
    private int submissionId;
    private String title;
    private String abs;
    private String pdfLink;

    public Submission(int submissionId, String title, String abs, String pdfLink) {
        this.submissionId = submissionId;
        this.title = title;
        this.abs = abs;
        this.pdfLink = pdfLink;
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
}
