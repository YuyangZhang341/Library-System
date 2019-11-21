package com2008;

public class Submission {
    int submissionId;
    String title;
    String abs;
    String pdfLink;
    String pdf;

    public Submission(int submissionId, String title, String abs, String pdfLink, String pdf) {
        this.submissionId = submissionId;
        this.title = title;
        this.abs = abs;
        this.pdfLink = pdfLink;
        this.pdf = pdf;
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

    public String getPdf() {
        return pdf;
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

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }
}
