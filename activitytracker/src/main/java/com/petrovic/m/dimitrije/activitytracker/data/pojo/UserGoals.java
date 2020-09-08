package com.petrovic.m.dimitrije.activitytracker.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserGoals {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("current_weight")
    @Expose
    private Integer currentWeight;
    @SerializedName("goal_weight")
    @Expose
    private Integer goalWeight;

    /**
     * No args constructor for use in serialization
     *
     */
    public UserGoals() {
    }

    /**
     *
     * @param currentWeight
     * @param goalWeight
     * @param id
     */
    public UserGoals(Integer id, Integer currentWeight, Integer goalWeight) {
        super();
        this.id = id;
        this.currentWeight = currentWeight;
        this.goalWeight = goalWeight;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(Integer currentWeight) {
        this.currentWeight = currentWeight;
    }

    public Integer getGoalWeight() {
        return goalWeight;
    }

    public void setGoalWeight(Integer goalWeight) {
        this.goalWeight = goalWeight;
    }

}