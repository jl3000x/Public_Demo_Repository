package com.zh.ppholic_server_demo.model;

public class APIUser {
    
    private String username;
    
    private String password;

    public APIUser() {
    }

    public APIUser(String username, String password) {
        this.username = username;
        this.password = password;
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

    @Override
    public String toString() {
        return "APIUser [password=" + password + ", username=" + username + "]";
    }
}
