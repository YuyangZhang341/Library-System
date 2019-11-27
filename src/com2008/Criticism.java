package com2008;

public class Criticism {
    private int submissionID;
    private int reviewerID;
    private String criticism;

    public Criticism(int submissionID, int reviewerID, String criticism) {
        this.submissionID = submissionID;
        this.reviewerID = reviewerID;
        this.criticism = criticism;
    }

    public void setSubmissionID(int submissionID) {
        this.submissionID = submissionID;
    }

    public void setReviewerID(int reviewerID) {
        this.reviewerID = reviewerID;
    }

    public void setCriticism(String criticism) {
        this.criticism = criticism;
    }

    public int getSubmissionID() {
        return submissionID;
    }

    public int getReviewerID() {
        return reviewerID;
    }

    public String getCriticism() {
        return criticism;
    }
}
