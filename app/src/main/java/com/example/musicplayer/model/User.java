package com.example.musicplayer.model;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private String username;
    private String email;
    private String fullName;
    private Date birthdate;
    private String country;
    private String user_id;

    public User() {
    }

    public User(String username, String email, String fullName, Date birthdate, String country, String userID) {
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.birthdate = birthdate;
        this.country = country;
        this.user_id = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
        return user_id;
    }

    public void setUserID(String userID) {
        this.user_id = userID;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", birthdate=" + birthdate +
                ", country='" + country + '\'' +
                ", userID='" + user_id + '\'' +
                '}';
    }
}
