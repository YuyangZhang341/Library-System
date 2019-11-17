package com2008;

public class Journal {
    private String issn;
    private String name;

    public Journal(String issn, String name) {
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
