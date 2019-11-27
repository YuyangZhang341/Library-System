package com2008;

import java.io.File;

public class Article extends Submission {
    private int vol;
    private int number;
    private int startPage;
    private int endPage;

    public Article(int submissionId, String title, String abs, File pdf, String mainAuthorsEmail, String issn, int vol, int number, int startPage, int endPage) {
        super(submissionId, title, abs, pdf, mainAuthorsEmail, issn);
        this.vol = vol;
        this.number = number;
        this.startPage = startPage;
        this.endPage = endPage;
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
