package com2008;

public class Article extends Submission {
    private String issn;
    private int vol;
    private int number;
    private int startPage;
    private int endPage;

    public Article(int submissionId, String title, String abs, String pdfLink, String mainAuthorsEmail, String issn, int vol, int number, int startPage, int endPage) {
        super(submissionId, title, abs, pdfLink, mainAuthorsEmail);
        this.issn = issn;
        this.vol = vol;
        this.number = number;
        this.startPage = startPage;
        this.endPage = endPage;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public void setVol(int vol) {
        this.vol = vol;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }

    public String getIssn() {
        return issn;
    }

    public int getVol() {
        return vol;
    }

    public int getNumber() {
        return number;
    }

    public int getStartPage() {
        return startPage;
    }

    public int getEndPage() {
        return endPage;
    }
}
