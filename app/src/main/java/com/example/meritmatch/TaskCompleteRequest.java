package com.example.meritmatch;

import com.google.gson.annotations.SerializedName;

public class TaskCompleteRequest {
    @SerializedName("task_id")
    private int taskId;
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("reputation_rating")
    private Integer reputationRating;

    public int getTaskId() {
        return taskId;
    }
    public void setTaskId(int taskId) {
        this.taskId = taskId;
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
    public Integer getReputationRating() {
        return reputationRating;
    }
    public void setReputationRating(Integer reputationRating) {
        this.reputationRating = reputationRating;
    }
}
