package com.example.planner.dto;

import com.example.planner.dao.EventInstance;
import com.google.gson.annotations.Expose;

public class EventPatternResponse {

    @Expose
    private int count;

    @Expose
    private EventInstance[] data;

    @Expose
    private String message;

    @Expose
    private Long offset;

    @Expose
    private int status;

    @Expose
    private boolean success;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public EventInstance[] getData() {
        return data;
    }

    public void setData(EventInstance[] data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getOffset() {
        return offset;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


}
