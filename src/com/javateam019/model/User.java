package com.javateam019.model;

/**
 *  user instances
 */
public class User extends Stakeholder{

    private String title;
    private String forenames;
    private String surname;
    private String universityAffiliation;
    private String email;
    private String password;



    public User(String title, String forenames, String surname, String universityAffiliation, String email, String password) {
        super(title, forenames, surname, universityAffiliation, email, password);
    }

    public User() {
        super();
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }


    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getForenames() {
        return forenames;
    }

    @Override
    public void setForenames(String forenames) {
        this.forenames = forenames;
    }

    @Override
    public String getSurname() {
        return surname;
    }

    @Override
    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public String getUniversityAffiliation() {
        return universityAffiliation;
    }

    @Override
    public void setUniversityAffiliation(String universityAffiliation) {
        this.universityAffiliation = universityAffiliation;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }
}
