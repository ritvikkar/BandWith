package com.ritvikkar.bandwith;


import android.location.Location;
import android.media.Image;

import java.util.Date;
import java.util.List;

public class User {

    private String id;
    private String name;
    private String email;
    private Date dob;
    private Image photo;
    private List<String> genres;
    private List<String> talents;
    private Location location;
    private String phoneNumber;
    private String bio;
    private List<String> experiences; // make custom experience or gig class to save here


    public User() {

    }

    public User(String id, String name, String email) {
        this.name = name;
        this.email = email;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public Date getDob() {
        return dob;
    }

    public Image getPhoto() {
        return photo;
    }

    public List<String> getTalents() {
        return talents;
    }

    public Location getLocation() {
        return location;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getBio() {
        return bio;
    }

    public List<String> getExperiences() {
        return experiences;
    }
}
