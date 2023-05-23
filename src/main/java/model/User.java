package model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    @JsonProperty("Id")
    private long id;
    @JsonProperty("Username")
    private String username;
    @JsonProperty("Password")
    private String password;
    public User() {
    }
    public User(long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
