package com2008;

public class Volume {
    private String issn;
    private int vol;
    private int year;

    public Volume(String issn, int vol, int year) {
        this.issn = issn;
        this.vol = vol;
        this.year = year;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public void setVol(int vol) {
        this.vol = vol;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getIssn() {
        return issn;
    }

    public int getVol() {
        return vol;
    }

    public int getYear() {
        return year;
    }
}
