package com.example.musicplayer.model;

import java.util.Date;

public class User {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private Date birthdate;
    private String country;
    private String userID;

    public User() {
    }

    public User(String username, String password, String email, String fullName, Date birthdate, String country, String userID) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.birthdate = birthdate;
        this.country = country;
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", birthdate=" + birthdate +
                ", country='" + country + '\'' +
                ", userID='" + userID + '\'' +
                '}';
    }
}
