package com.zh.ppholic_server_demo.rest;

public class CustomErrorResponse {
    
    private int status;

    private String message;

    private long timeStamp;

    public CustomErrorResponse() {

    }

    public CustomErrorResponse(int status, String message, long timeStamp) {
        
        this.status = status;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
