package com2008;

public class Verdict {
    int submissionId;
    int reviewerId;
    String summary;
    String typographicalErrors;
    String verdict;

    public Verdict(int submissionId, int reviewerId, String summary, String typographicalErrors, String verdict) {
        this.submissionId = submissionId;
        this.reviewerId = reviewerId;
        this.summary = summary;
        this.typographicalErrors = typographicalErrors;
        this.verdict = verdict;
    }

    public int getSubmissionId() {
        return submissionId;
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

    public String getVerdict() {
        return verdict;
    }

    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
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

    public void setVerdict(String verdict) {
        this.verdict = verdict;
    }
}
