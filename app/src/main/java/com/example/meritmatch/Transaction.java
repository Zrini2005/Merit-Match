package com.example.meritmatch;

import com.google.gson.annotations.SerializedName;

public class Transaction {
    private int id;
    private String timestamp;
    @SerializedName("user_id")
    private int user;
    @SerializedName("chosen_by_id")
    private int chosenById;
    private int taskId;
    @SerializedName("karma_points")
    private int karmaPoints;
    private String description;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getUserId() {
        return user;
    }

    public void setUserId(int userId) {
        this.user = userId;
    }

    public int getChosenById() {
        return chosenById;
    }

    public void setChosenById(int chosenById) {
        this.chosenById = chosenById;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getKarmaPoints() {
        return karmaPoints;
    }

    public void setKarmaPoints(int karmaPoints) {
        this.karmaPoints = karmaPoints;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
