package model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;

public class Exercise {
    private long id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("date")
    private Date dateNumber;
    private int userId;

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
    @Override
    public String toString() {
        return "{" +
                "\"title\":\"" + title + "\"" +
                ",\"date\":\"" + dateNumber +
                "\",\"userId\":" + userId +
                "}";
    }
}

