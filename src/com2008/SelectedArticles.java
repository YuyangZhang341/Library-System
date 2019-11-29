package com2008;

public class SelectedArticles {
    private String issn;
    private String name;

    public SelectedArticles(String issn, String name, String chiefEditorEmail) {
        this.issn = issn;
        this.name = name;
    }

    public String getIssn() {
        return issn;
    }

    public String getName() {
        return name;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public void setName(String name) {
        this.name = name;
    }

}
