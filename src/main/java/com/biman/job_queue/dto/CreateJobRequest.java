package com.biman.job_queue.dto;

import java.util.Map;

public class CreateJobRequest {
    private String type;
    private Map<String, Object> payload;


    // Empty constructor but if it is not defined by default Java will 
    // provide one. But it is mandatory to have it when we add a parameterized 
    // constructor
    public CreateJobRequest() {}
    
    // ---- GETTERS ----
    public String getType() {
        return type;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    // ---- SETTERS ----
    public void setType(String type) {
        this.type = type;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }
}
