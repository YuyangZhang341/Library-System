package com2008;

abstract class Stakeholder {
    private String title;
    private String forenames;
    private String surname;
    private String universityAffiliation;
    private String email;
    private String password;

    public Stakeholder(String title, String forenames, String surname, String universityAffiliation, String email, String password) {
        this.title = title;
        this.forenames = forenames;
        this.surname = surname;
        this.universityAffiliation = universityAffiliation;
        this.email = email;
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public String getForenames() {
        return forenames;
    }

    public String getSurname() {
        return surname;
    }

    public String getUniversityAffiliation() {
        return universityAffiliation;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setForenames(String forenames) {
        this.forenames = forenames;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setUniversityAffiliation(String universityAffiliation) {
        this.universityAffiliation = universityAffiliation;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
