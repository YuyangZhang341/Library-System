package com2008;

public class Review {
    private int reviewerId;
    private String summary;
    private String typographicalErrors;
    private String initialVerdict;
    private int submissionId;
    private String finalVerdict;

    public Review(int reviewerId, String summary, String typographicalErrors, String initialVerdict, int submissionId, String finalVerdict) {
        this.reviewerId = reviewerId;
        this.summary = summary;
        this.typographicalErrors = typographicalErrors;
        this.initialVerdict = initialVerdict;
        this.submissionId = submissionId;
        this.finalVerdict = finalVerdict;
    }

    public void setReviewerId(int reviewerId) {
        this.reviewerId = reviewerId;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setTypographicalErrors(String typographicalErrors) {
        this.typographicalErrors = typographicalErrors;
    }

    public void setInitialVerdict(String initialVerdict) {
        this.initialVerdict = initialVerdict;
    }

    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
    }

    public void setFinalVerdict(String finalVerdict) {
        this.finalVerdict = finalVerdict;
    }

    public int getReviewerId() {
        return reviewerId;
    }

    public String getSummary() {
        return summary;
    }

    public String getTypographicalErrors() {
        return typographicalErrors;
    }

    public String getInitialVerdict() {
        return initialVerdict;
    }

    public int getSubmissionId() {
        return submissionId;
    }

    public String getFinalVerdict() {
        return finalVerdict;
    }
}
