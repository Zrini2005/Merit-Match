package com.example.meritmatch;

import com.google.gson.annotations.SerializedName;

public class TaskCreateRequest {
    @SerializedName("description")
    private String description;
    @SerializedName("karma_cost")
    private int karmaCost;
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;


    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getKarmaCost() {
        return karmaCost;
    }
    public void setKarmaCost(int karmaCost) {
        this.karmaCost = karmaCost;
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

}
