package com.example.meritmatch;

import com.google.gson.annotations.SerializedName;

public class TaskChooseRequest {
    @SerializedName("task_id")
    private int taskId;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

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
}