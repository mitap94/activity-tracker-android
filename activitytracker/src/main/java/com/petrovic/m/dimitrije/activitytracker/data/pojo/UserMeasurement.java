package com.petrovic.m.dimitrije.activitytracker.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserMeasurement {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("weight")
    @Expose
    private Integer weight;
    @SerializedName("height")
    @Expose
    private Integer height;
    @SerializedName("neck")
    @Expose
    private Integer neck;
    @SerializedName("chest")
    @Expose
    private Integer chest;
    @SerializedName("biceps")
    @Expose
    private Integer biceps;
    @SerializedName("forearm")
    @Expose
    private Integer forearm;
    @SerializedName("abdomen")
    @Expose
    private Integer abdomen;
    @SerializedName("hips")
    @Expose
    private Integer hips;
    @SerializedName("thigh")
    @Expose
    private Integer thigh;
    @SerializedName("image")
    @Expose
    private String image;

    /**
     * No args constructor for use in serialization
     *
     */
    public UserMeasurement() {
    }

    /**
     *
     * @param date
     * @param image
     * @param chest
     * @param hips
     * @param thigh
     * @param abdomen
     * @param weight
     * @param id
     * @param forearm
     * @param neck
     * @param biceps
     * @param height
     */
    public UserMeasurement(Integer id, String date, Integer weight, Integer height, Integer neck, Integer chest, Integer biceps, Integer forearm, Integer abdomen, Integer hips, Integer thigh, String image) {
        super();
        this.id = id;
        this.date = date;
        this.weight = weight;
        this.height = height;
        this.neck = neck;
        this.chest = chest;
        this.biceps = biceps;
        this.forearm = forearm;
        this.abdomen = abdomen;
        this.hips = hips;
        this.thigh = thigh;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getNeck() {
        return neck;
    }

    public void setNeck(Integer neck) {
        this.neck = neck;
    }

    public Integer getChest() {
        return chest;
    }

    public void setChest(Integer chest) {
        this.chest = chest;
    }

    public Integer getBiceps() {
        return biceps;
    }

    public void setBiceps(Integer biceps) {
        this.biceps = biceps;
    }

    public Integer getForearm() {
        return forearm;
    }

    public void setForearm(Integer forearm) {
        this.forearm = forearm;
    }

    public Integer getAbdomen() {
        return abdomen;
    }

    public void setAbdomen(Integer abdomen) {
        this.abdomen = abdomen;
    }

    public Integer getHips() {
        return hips;
    }

    public void setHips(Integer hips) {
        this.hips = hips;
    }

    public Integer getThigh() {
        return thigh;
    }

    public void setThigh(Integer thigh) {
        this.thigh = thigh;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}