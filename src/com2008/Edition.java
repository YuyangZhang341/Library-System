package com2008;

public class Edition {
    private String issn;
    private int vol;
    private int number;

    public Edition(String issn, int vol, int number) {
        this.issn = issn;
        this.vol = vol;
        this.number = number;
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

    public String getIssn() {
        return issn;
    }

    public int getVol() {
        return vol;
    }

    public int getNumber() {
        return number;
    }


}
