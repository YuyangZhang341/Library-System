package com2008;

public class Editor extends Stakeholder {
    boolean isChiefEditor;
    String journalIssn;

    public Editor(String title, String forenames, String surname, String universityAffiliation, String email, String password, boolean isChiefEditor, String journalIssn) {
        super(title, forenames, surname, universityAffiliation, email, password);
        this.isChiefEditor = isChiefEditor;
        this.journalIssn = journalIssn;
    }

    public boolean isChiefEditor() {
        return isChiefEditor;
    }

    public String getJournalIssn() {
        return journalIssn;
    }

    public void setChiefEditor(boolean chiefEditor) {
        isChiefEditor = chiefEditor;
    }

    public void setJournalIssn(String journalIssn) {
        this.journalIssn = journalIssn;
    }
}
