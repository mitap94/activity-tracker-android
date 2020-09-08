package com.petrovic.m.dimitrije.activitytracker.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseFood {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("calories")
    @Expose
    private Integer calories;
    @SerializedName("serving_size")
    @Expose
    private Integer servingSize;
    @SerializedName("is_recipe")
    @Expose
    private Boolean isRecipe;
    @SerializedName("image")
    @Expose
    private String image;

    /**
     * No args constructor for use in serialization
     *
     */
    public BaseFood() {
    }

    /**
     *
     * @param image
     * @param name
     * @param id
     * @param calories
     * @param isRecipe
     * @param servingSize
     */
    public BaseFood(Integer id, String name, Integer calories, Integer servingSize, Boolean isRecipe, String image) {
        super();
        this.id = id;
        this.name = name;
        this.calories = calories;
        this.servingSize = servingSize;
        this.isRecipe = isRecipe;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Integer getServingSize() {
        return servingSize;
    }

    public void setServingSize(Integer servingSize) {
        this.servingSize = servingSize;
    }

    public Boolean getIsRecipe() {
        return isRecipe;
    }

    public void setIsRecipe(Boolean isRecipe) {
        this.isRecipe = isRecipe;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}