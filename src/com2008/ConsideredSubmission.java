package com2008;

import java.io.File;

public class ConsideredSubmission extends Submission {
    String decision;

    public ConsideredSubmission(int submissionId, String title, String abs, File pdf, String mainAuthorsEmail, String issn, String decision) {
        super(submissionId, title, abs, pdf, mainAuthorsEmail, issn);
        this.decision = decision;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }
}
