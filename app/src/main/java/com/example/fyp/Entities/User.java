package com.example.fyp.Entities;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String id;
    private String email;
    private String fname;
    private String lastName;
    private List<Journey> journeys = new ArrayList<Journey>();


    public User(String id,String email, String fname, String lastName, List<Journey> journeys) {
        this.id = id;
        this.email = email;
        this.fname = fname;
        this.lastName = lastName;
        this.journeys = journeys;
    }


    public User(String email, String fname, String lastName) {
        this.id = id;
        this.email = email;
        this.fname = fname;
        this.lastName = lastName;

    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Journey> getJourneys() {
        return journeys;
    }

    public void setJourneys(List <Journey> journeys) {
        this.journeys = journeys;
    }
}
