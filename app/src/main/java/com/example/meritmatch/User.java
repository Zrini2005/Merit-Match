package com.example.meritmatch;

import com.google.gson.annotations.SerializedName;

public class User {
    private int id;
    private String username;
    private String password;
    @SerializedName("karma_points")
    private int karmaPoints;
    private float reputation;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getReputation(){
        return reputation;
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

    public int getKarmaPoints() {
        return karmaPoints;
    }

    public void setKarmaPoints(int karmaPoints) {
        this.karmaPoints = karmaPoints;
    }
}
