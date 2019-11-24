package com2008;

public class ConsideredSubmission extends Submission {
    String decision;

    public ConsideredSubmission(int submissionId, String title, String abs, String pdfLink, String mainAuthorsEmail, String decision) {
        super(submissionId, title, abs, pdfLink, mainAuthorsEmail);
        this.decision = decision;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }
}
