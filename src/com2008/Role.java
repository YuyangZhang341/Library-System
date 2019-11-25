package com2008;

public class Role {
    String email;
    String role;
    String issnOrSubmissionId;
    String nameOrTitle;

    public Role(String email, String role, String issnOrSubmissionId, String nameOrTitle) {
        this.email = email;
        this.role = role;
        this.issnOrSubmissionId = issnOrSubmissionId;
        this.nameOrTitle = nameOrTitle;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getIssnOrSubmissionId() {
        return issnOrSubmissionId;
    }

    public String getNameOrTitle() {
        return nameOrTitle;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setIssnOrSubmissionId(String issnOrSubmissionId) {
        this.issnOrSubmissionId = issnOrSubmissionId;
    }

    public void setNameOrTitle(String nameOrTitle) {
        this.nameOrTitle = nameOrTitle;
    }
}
