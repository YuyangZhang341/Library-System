package com2008;

public class Journal {
    private String issn;
    private String name;
    private String chiefEditorEmail;

    public Journal(String issn, String name, String chiefEditorEmail) {
        this.issn = issn;
        this.name = name;
        this.chiefEditorEmail = chiefEditorEmail;
    }

    public String getIssn() {
        return issn;
    }

    public String getName() {
        return name;
    }

    public String getChiefEditorEmail() {
        return chiefEditorEmail;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChiefEditorEmail(String chiefEditorEmail) {
        this.chiefEditorEmail = chiefEditorEmail;
    }
}
