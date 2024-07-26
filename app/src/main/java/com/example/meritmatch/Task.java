package com.example.meritmatch;

public class Task {
    private int id;
    private String description;
    private int karma_cost;
    private int owner_id;
    private Integer chosen_by_id;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getKarmaCost() {
        return karma_cost;
    }

    public void setKarmaCost(int karma_cost) {
        this.karma_cost = karma_cost;
    }

    public int getOwnerId() {
        return owner_id;
    }

    public void setOwnerId(int owner_id) {
        this.owner_id = owner_id;
    }

    public Integer getChosenById() {
        return chosen_by_id;
    }

    public void setChosenById(Integer chosen_by_id) {
        this.chosen_by_id = chosen_by_id;
    }
}
