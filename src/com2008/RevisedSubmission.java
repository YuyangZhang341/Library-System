package com2008;

import java.io.File;

public class RevisedSubmission {
    private int submissionId;
    private String title;
    private String abs;
    private File pdf;

    public RevisedSubmission(int submissionId, String title, String abs, File pdf) {
        this.submissionId = submissionId;
        this.title = title;
        this.abs = abs;
        this.pdf = pdf;
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

    public void setPdf(File pdf) {
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

    public File getPdf() {
        return pdf;
    }
}
