package model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;

public class Exercise {
    private long id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("date")
    private Date dateNumber;
    @JsonProperty("userId")
    private int userId;
    @JsonProperty("weights")
    private String weights;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("categoryId")
    private int categoryId;

    public Exercise(long id, String title, Date dateNumber, String weights, String amount, int categoryId, int userId) {
        this.id = id;
        this.title = title;
        this.dateNumber = dateNumber;
        this.weights = weights;
        this.amount = amount;
        this.categoryId = categoryId;
        this.userId = userId;
    }

    public Exercise(long id, String title, Date dateNumber, int userId) {
        this.id = id;
        this.title = title;
        this.dateNumber = dateNumber;
        this.userId = userId;
    }

    public Exercise() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDateNumber() {
        return dateNumber;
    }

    public void setDateNumber(Date dateNumber) {
        this.dateNumber = dateNumber;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getWeights() {
        return weights;
    }

    public void setWeights(String weights) {
        this.weights = weights;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    @Override

    public String toString() {
        return "{" + "\"id\":" + id +
                ",\"title\":\"" + title + "\"" +
                ",\"date\":\"" + dateNumber +
                "\",\"weights\":\"" + weights + "\"" +
                ",\"amount\":\"" + amount + "\"" +
                ",\"categoryId\":" + categoryId +
                ",\"userId\":" + userId +
                "}";
    }
}

