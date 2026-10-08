package com.smartcanteen.model;

import java.io.Serializable;

/**
 * ABSTRACTION + ENCAPSULATION: a User is never created directly, only as a Student or an Admin.
 * All fields are private and are reached through getters and setters.
 */
public abstract class User implements Serializable {

    public static final String ROLE_STUDENT = "STUDENT";
    public static final String ROLE_ADMIN = "ADMIN";

    private int id;
    private String name;
    private String collegeId;
    private String password;   // stores the HASH, never the plain password
    private String email;
    private String role;

    public User() {
    }

    public User(int id, String name, String collegeId, String password, String email, String role) {
        this.id = id;
        this.name = name;
        this.collegeId = collegeId;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    // Abstract methods: every subclass MUST provide its own version (polymorphism).
    public abstract String getDashboardPath();

    public abstract String getRoleTitle();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(String collegeId) {
        this.collegeId = collegeId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return getRoleTitle() + "[" + collegeId + " - " + name + "]";
    }
}
