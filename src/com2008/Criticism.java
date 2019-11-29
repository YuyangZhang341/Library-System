package com2008;

public class Criticism {
    private int criticismID;
    private int submissionID;
    private int reviewerID;
    private String criticism;
    private String response;

    public Criticism(int criticismID, int submissionID, int reviewerID, String criticism, String response) {
        this.criticismID = criticismID;
        this.submissionID = submissionID;
        this.reviewerID = reviewerID;
        this.criticism = criticism;
        this.response = response;
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

    public void setResponse(String response) {
        this.response = response;
    }

    public int getCriticismID() {
        return criticismID;
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

    public String getResponse() {
        return response;
    }
}
